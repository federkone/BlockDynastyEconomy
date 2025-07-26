package BlockDynasty.Economy.aplication.useCase.account;

import BlockDynasty.Economy.domain.result.ErrorCode;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.entities.balance.Balance;
import BlockDynasty.Economy.domain.persistence.Exceptions.TransactionException;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;
import BlockDynasty.Economy.domain.services.IAccountService;
import BlockDynasty.Economy.domain.services.ICurrencyService;

import java.util.*;
import java.util.stream.Collectors;

public class GetAccountsUseCase {
    private final IAccountService accountService;
    private final IRepository dataStore;
    private final ICurrencyService currencyService;

    public GetAccountsUseCase(IAccountService accountService, ICurrencyService currencyService, IRepository dataStore) {
        this.accountService = accountService;
        this.dataStore = dataStore;
        this.currencyService = currencyService;
    }

    public Result<Account> getAccount(String name) {
        Account account = this.accountService.getAccountCache(name);
       if(account == null){
           //System.out.println("DB HIT ->");
            Result<Account> result = this.dataStore.loadAccountByName(name);
            if(result.isSuccess()) {
                account = result.getValue();
                syncWalletWithSystemCurrencies(account);
            }else{
                return Result.failure(result.getErrorMessage(), result.getErrorCode());
            }
       }//else { System.out.println("CAHCE HIT ->");}
       return Result.success(account);
    }

    public Result<Account> getAccount(UUID uuid) {
        Account account =  this.accountService.getAccountCache(uuid);
       if(account == null){
           //System.out.println("DB HIT ->");
            Result<Account> result = this.dataStore.loadAccountByUuid(uuid.toString());
           if(result.isSuccess()) {
                account = result.getValue();
               syncWalletWithSystemCurrencies(account);
            }else{
                return Result.failure(result.getErrorMessage(), result.getErrorCode());
            }
       }//else { System.out.println("CAHCE HIT ->");}
       return Result.success(account);
    }

    public void syncCacheWithAccount(Account account) {
        UUID uuid = account.getUuid();
        Account cachedAccount = this.accountService.getAccountCache(uuid);
        if (cachedAccount != null) {
            for (Balance updatedBalance : account.getBalances()) {
                Balance cachedBalance = cachedAccount.getBalance(updatedBalance.getCurrency());
                if (cachedBalance != null) {
                    cachedBalance.setAmount(updatedBalance.getAmount());
                } else {
                    cachedAccount.getBalances().add(new Balance(updatedBalance.getCurrency(), updatedBalance.getAmount()));
                }
            }
        }
    }

    public void syncDbWithCache() {
        Collection<Account> accounts = this.accountService.getAccountsOnline();
        for (Account account : accounts) {
            syncWalletWithSystemCurrencies(account);
            try {
                dataStore.saveAccount(account);
                System.out.println("Cuenta guardada con el nuevo balance.");
            } catch (TransactionException e) {
                throw new TransactionException("Error in transaction", e);
            }
        }
    } //este metodo se debe eliminar

    public void syncCache(UUID uuid){
        Account accountCache = this.accountService.getAccountCache(uuid);
        if (accountCache != null){
            Result<Account> result =  this.dataStore.loadAccountByUuid(uuid.toString());
            if (result.isSuccess()){
                    syncWalletWithSystemCurrencies(result.getValue());
                    accountCache.setBalances(result.getValue().getBalances());
                }
        }
    }

    private void syncWalletWithSystemCurrencies(Account account) {
        List<Balance> updatedBalances =  this.currencyService.getCurrencies().stream()
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

    public Result<Void> checkNameChange( Account account , String newName) {
       if (!account.getNickname().equalsIgnoreCase(newName)) {
           account.setNickname(newName);
           try {
               this.dataStore.saveAccount(account);
           } catch (TransactionException e) {
               return Result.failure( "Error saving account after name change", ErrorCode.DATA_BASE_ERROR);
           }
       }
        return Result.success(null);
    }

    public Result<Void> checkUuidChange(Account account, UUID newUuid) {
        if (!account.getUuid().equals(newUuid)) {
            account.setUuid(newUuid);
            try {
                this.dataStore.saveAccount(account);
            } catch (TransactionException e) {
                return Result.failure("Error saving account after UUID change", ErrorCode.DATA_BASE_ERROR);
            }
        }
        return Result.success(null);
    }

   public Result<List<Account>> getTopAccounts(String currency, int limit, int offset) {
       if (limit <= 0) {
           return Result.failure("Limit must be greater than 0", ErrorCode.INVALID_ARGUMENT);
       }
       List<Account> cache =  this.accountService.getAccountsTopList(currency);
       if (!cache.isEmpty() && cache.size() >= limit + offset) {
           //System.out.println("Cache hit");
           return Result.success(cache.subList(offset, Math.min(offset + limit, cache.size())));
       }
       if (!dataStore.isTopSupported()) {
           return Result.failure("Repository not support top", ErrorCode.REPOSITORY_NOT_SUPPORT_TOP);
       }
       List<Account> accounts;
       try {
           //System.out.println("DATABASE HIT");
           accounts =  this.dataStore.getAccountsTopByCurrency(currency, limit, offset);
           for (Account account : accounts) {
               this.accountService.addAccountToTopList(account, currency);
           }
       } catch (TransactionException e) {
           return Result.failure("Error in transaction", ErrorCode.DATA_BASE_ERROR);
       }
       if (accounts.isEmpty()) {
           return Result.failure("No accounts found for currency: "+ currency, ErrorCode.ACCOUNT_NOT_FOUND);
       }
       return Result.success(accounts);
   }

}
