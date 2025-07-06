package me.BlockDynasty.Economy.aplication.useCase.account;

import me.BlockDynasty.Economy.domain.result.ErrorCode;
import me.BlockDynasty.Economy.domain.result.Result;
import me.BlockDynasty.Economy.domain.account.Account;
import me.BlockDynasty.Economy.domain.account.AccountCache;
import me.BlockDynasty.Economy.domain.account.Exceptions.AccountNotFoundException;
import me.BlockDynasty.Economy.domain.balance.Balance;
import me.BlockDynasty.Economy.domain.currency.CurrencyCache;
import me.BlockDynasty.Economy.domain.repository.Criteria.Criteria;
import me.BlockDynasty.Economy.domain.repository.Exceptions.TransactionException;
import me.BlockDynasty.Economy.domain.repository.IRepository;
import org.checkerframework.checker.units.qual.A;

import java.util.*;
import java.util.stream.Collectors;

public class GetAccountsUseCase {
    private final AccountCache accountCache;
    private final IRepository dataStore;
    private final CurrencyCache currencyCache;
    //private final CachedTopList cachedTopList;


//TODO: PENSAR EN ESTA LOGICA, DONDED SE DETECTA UN CAMBIO DE NOMBRE SEGUN LA OBTENCION DE JUGADOR POR UUID
    /* acc = getAccountsUseCase.getAccount(player.getUniqueId());
   if(!acc.getNickname().equals(player.getName()))
       acc.setNickname(player.getName());
   UtilServer.consoleLog("Account name changes detected, updating: " + player.getName());
   plugin.getDataStore().saveAccount(acc);  */
    //TODO----------------------------------------------------------------

    public GetAccountsUseCase(AccountCache accountCache, CurrencyCache currencyCache, IRepository dataStore) {
        this.accountCache = accountCache;
        this.dataStore = dataStore;
        this.currencyCache = currencyCache;
        //this.cachedTopList = cachedTopList;
    }

    public Result<Account> getAccount(String name) {
        Account account = accountCache.getAccount(name);
       if(account == null){
            Criteria criteria = Criteria.create().filter("nickname", name).limit(1); //prepare for get account with uuid
            List<Account> accounts = dataStore.loadAccounts(criteria);
            if(!accounts.isEmpty()) {
                account = accounts.get(0);
                updateAccountBalances(account);//AUTOMATICAMENTE CHEQUEA SI EL BALANCE DE LA CUENTA ESTA ACTUALIZADO CON LAS MONEDAS DEL SISTEMA
            }else{
                return Result.failure("Account not found", ErrorCode.ACCOUNT_NOT_FOUND);
            }
       }
       return Result.success(account);
    }

    public Result<Account> getAccount(UUID uuid) {
        Account account = accountCache.getAccount(uuid);
       if(account == null){

            Criteria criteria = Criteria.create().filter("uuid", uuid.toString()).limit(1); //prepare for get account with uuid
            List<Account> accounts = dataStore.loadAccounts(criteria);
            if(!accounts.isEmpty()) {
                account = accounts.get(0);
                updateAccountBalances(account);
                //accountManager.addAccountToCache(account); //añadir a cache??
            }else{
                return Result.failure("Account not found", ErrorCode.ACCOUNT_NOT_FOUND);
            }
       }
       return Result.success(account);
    }

    public void updateAccountCache(Account account) {
        UUID uuid = account.getUuid();
        Account cachedAccount = this.accountCache.getAccount(uuid);
        if (cachedAccount != null) {
            // Actualiza solo los balances que vienen en account
            for (Balance updatedBalance : account.getBalances()) {
                Balance cachedBalance = cachedAccount.getBalance(updatedBalance.getCurrency());
                if (cachedBalance != null) {
                    cachedBalance.setBalance(updatedBalance.getBalance());
                } else {
                    cachedAccount.getBalances().add(new Balance(updatedBalance.getCurrency(), updatedBalance.getBalance()));
                }
            }
        }
    }


    public void updateAccountCache(UUID uuid){
        Account accountCache = this.accountCache.getAccount(uuid);
        Account accountDb = null;
        Criteria criteria = Criteria.create().filter("uuid", uuid.toString()).limit(1); //prepare for get account with uuid
        List<Account> accounts = dataStore.loadAccounts(criteria);
        if (!accounts.isEmpty() && accountCache != null){
            accountDb = accounts.get(0);
            updateAccountBalances(accountDb);
            accountCache.setBalances(accountDb.getBalances( ));
        }else {
            throw new AccountNotFoundException("Account not found");
        }
    }

    public List<Account> getAllAccounts() {
        return dataStore.loadAccounts(Criteria.create());
    }

    public Set<Account> getOnlineAccounts() {
        return accountCache.getAccounts();
    }

    @Deprecated
    public void updateAccountsCache(){ //todo, test
        Set<Account> accounts = accountCache.getAccounts();   //todas las cuentas de cache
        for (Account account : accounts) {
            updateAccountBalances(account);    //deberian volver a sincronizarse con las monedas del sistema, esto evita quedarse con los balances de las monedas que ya no estan en el sistema
            try {
                dataStore.saveAccount(account); //guardar en db los cambios hechos sobre sus balances, ya sea haber agregado, o quitado
            } catch (TransactionException e) {
                throw new TransactionException("Error in transaction", e);
            }
        }
    }

   public Result<List<Account>> getTopAccounts(String currency, int limit, int offset) {
       if (limit <= 0) {
           //throw new IllegalArgumentException("Limit must be greater than 0");
           return Result.failure("Limit must be greater than 0", ErrorCode.INVALID_ARGUMENT);
       }
       List<Account> cache = accountCache.getAccountsTopList(currency);
       if (!cache.isEmpty() && cache.size() >= limit + offset) {
           //System.out.println("Cache hit");
           return Result.success(cache.subList(offset, Math.min(offset + limit, cache.size())));
       }
       if (!dataStore.isTopSupported()) {
           //throw new RepositoryNotSupportTopException("Repository not support top");
           return Result.failure("Repository not support top", ErrorCode.REPOSITORY_NOT_SUPPORT_TOP);
       }
       List<Account> accounts;
       try {
           //System.out.println("DATABASE HIT");
           accounts = dataStore.getAccountsTopByCurrency(currency, limit, offset);
           for (Account account : accounts) {
               accountCache.addAccountToTopList(account, currency);
           }
       } catch (TransactionException e) {
           //throw new TransactionException("Error in transaction", e);
           return Result.failure("Error in transaction", ErrorCode.DATA_BASE_ERROR);
       }
       if (accounts.isEmpty()) {
           //throw new AccountNotFoundException("No accounts found for currency: " + currency);
           return Result.failure("No accounts found for currency: "+ currency, ErrorCode.ACCOUNT_NOT_FOUND);
       }
       return Result.success(accounts);
   }

    private void updateAccountBalances(Account account) { //cada vez que traigo de la db la cuenta, le meto a la lista de balances sus balances correspondientes o le pone LAS CURRENCIES DEL SISTEMA/CAHCE/DB CON SUS VALORES POR DEFECTO
        List<Balance> updatedBalances = currencyCache.getCurrencies().stream()
                .map(systemCurrency ->
                        account.getBalances().stream()
                                .filter(balance -> balance.getCurrency().getUuid().equals(systemCurrency.getUuid()))
                                .findFirst() // Busca si ya existe el balance para esta moneda
                                .orElseGet(() -> { // Si no existe, crea un nuevo balance para esta moneda
                                    return new Balance(systemCurrency);
                                }))
                .collect(Collectors.toList());
        account.setBalances(updatedBalances);
    }
}
