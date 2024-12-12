package me.BlockDynasty.Economy.aplication.useCase.account;

import me.BlockDynasty.Economy.domain.account.Account;
import me.BlockDynasty.Economy.domain.account.AccountCache;
import me.BlockDynasty.Economy.domain.account.Exceptions.AccountNotFoundException;
import me.BlockDynasty.Economy.domain.balance.Balance;
import me.BlockDynasty.Economy.domain.currency.CurrencyCache;
import me.BlockDynasty.Economy.domain.repository.Criteria.Criteria;
import me.BlockDynasty.Economy.domain.repository.IRepository;
import org.bukkit.entity.Player;

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

    public Account getAccount(Player player) {
        return getAccount(player.getUniqueId());
    }

    public Account getAccount(String name) {
        Account account = accountCache.getAccount(name);

       if(account == null){
           //System.out.println("CUENTA NO ENCONTRAD EN CACHE");
            Criteria criteria = Criteria.create().filter("nickname", name).limit(1); //prepare for get account with uuid
            List<Account> accounts = dataStore.loadAccounts(criteria);
            if(!accounts.isEmpty()) {
                account = accounts.get(0);
                updateAccountBalances(account);
                //accountManager.addAccountToCache(account); //TODO :añadir a cache, CUIDADO, ANALIZAR HACER UN LOADACCOUNTUSERCASE, YA QUE LOS COMANDOS DE CONSULTA ESTAN CARGANDO EN LA CACHE DE MANERA INNECESARIA
            }else{
                throw new AccountNotFoundException("Account not found");
            }
       }

        return account;
    }

        public Account getAccount(UUID uuid) {
        Account account = accountCache.getAccount(uuid);

       if(account == null){
            Criteria criteria = Criteria.create().filter("uuid", uuid.toString()).limit(1); //prepare for get account with uuid
            List<Account> accounts = dataStore.loadAccounts(criteria);
            if(!accounts.isEmpty()) {
                account = accounts.get(0);
                updateAccountBalances(account);
                //accountManager.addAccountToCache(account); //añadir a cache
            }else{
                throw new AccountNotFoundException("Account not found");
            }
       }

        return account;
    }

    public void updateAccountCache(UUID uuid){
        Account accountCache = this.accountCache.getAccount(uuid);
        Account accountDb = null;

        Criteria criteria = Criteria.create().filter("uuid", uuid.toString()).limit(1); //prepare for get account with uuid
        List<Account> accounts = dataStore.loadAccounts(criteria);
                //accountManager.addAccountToCache(account); //añadir a cache
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

   //TODO: TOP 10 ACC, añadir uso de la cache de CachedTopList class
    //todo: puedo tomar como criterio que la lista de tops sean los que mas suma de todas las monedas tienen
   /*lic LinkedList<CachedTopListEntry> getTopAccounts(String currency) {
        List<Account> accounts = new ArrayList<>();
        LinkedList<CachedTopListEntry> cache = new LinkedList<>();

        if (!dataStore.isTopSupported()){
            throw new RepositoryNotSupportTopException("Repository not support top");
        }

        //cache = cachedTopList.getResults(); //busco en la cache

        if(cache.isEmpty()){ //si la cache esta vacia, buscar en la db
            try{
                if (currency.equals("default")){
                    accounts = dataStore.getAccountsByCurrency(currencyManager.getDefaultCurrency().getSingular() ,10,0);

                }else{
                    accounts = dataStore.getAccountsByCurrency(currency,10,0);

                }
                //AGREGAR A CACHE PARA LUEGO RETORNAR
                for(Account account : accounts){
                    cache.add(new CachedTopListEntry(account.getNickname(), account.getBalance(currency))); //añado a la cache los datos encontrados

                }
            }catch (TransactionException e){
                throw new TransactionException("Error in transaction");
            }
        }



        //if(CachedTopList != null && CachedTopList.getTopList() != null){
        //    return CachedTopList.getTopList();
        //}

        if (cache.isEmpty()){
            throw new AccountNotFoundException("not found ");
        }


        return cache;
    }

/*
    */
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

// Actualiza los balances del usuario con la lista construida
        account.setBalances(updatedBalances);
    }

}
