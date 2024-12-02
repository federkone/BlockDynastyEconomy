package me.BlockDynasty.Economy.aplication.useCase.account;

import me.BlockDynasty.Economy.domain.account.Account;
import me.BlockDynasty.Economy.domain.account.AccountManager;
import me.BlockDynasty.Economy.domain.account.Exceptions.AccountNotFoundException;
import me.BlockDynasty.Economy.domain.currency.CachedTopListEntry;
import me.BlockDynasty.Economy.domain.currency.Currency;
import me.BlockDynasty.Economy.domain.currency.CurrencyManager;
import me.BlockDynasty.Economy.domain.repository.Criteria.Criteria;
import me.BlockDynasty.Economy.domain.repository.Exceptions.RepositoryNotSupportTopException;
import me.BlockDynasty.Economy.domain.repository.Exceptions.TransactionException;
import me.BlockDynasty.Economy.domain.repository.IRepository;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class GetAccountsUseCase {
    private final AccountManager accountManager;
    private final IRepository dataStore;
    private final CurrencyManager currencyManager;
    //private final CachedTopList cachedTopList;


//TODO: PENSAR EN ESTA LOGICA, DONDED SE DETECTA UN CAMBIO DE NOMBRE SEGUN LA OBTENCION DE JUGADOR POR UUID
    /* acc = getAccountsUseCase.getAccount(player.getUniqueId());
   if(!acc.getNickname().equals(player.getName()))
       acc.setNickname(player.getName());
   UtilServer.consoleLog("Account name changes detected, updating: " + player.getName());
   plugin.getDataStore().saveAccount(acc);  */
    //TODO----------------------------------------------------------------

    public GetAccountsUseCase(AccountManager accountManager, CurrencyManager currencyManager, IRepository dataStore) {
        this.accountManager = accountManager;
        this.dataStore = dataStore;
        this.currencyManager = currencyManager;
        //this.cachedTopList = cachedTopList;
    }

    public Account getAccount(Player player) {
        return getAccount(player.getUniqueId());
    }

    public Account getAccount(String name) {
        Account account = accountManager.getAccount(name);

       if(account == null){
            Criteria criteria = Criteria.create().filter("nickname", name).limit(1); //prepare for get account with uuid
            List<Account> accounts = dataStore.loadAccounts(criteria);
            if(!accounts.isEmpty()) {
                account = accounts.get(0);
                updateAccountBalances(account);
                accountManager.addAccountToCache(account); //añadir a cache
            }else{
                throw new AccountNotFoundException("Account not found");
            }
       }

        return account;
    }

        public Account getAccount(UUID uuid) {
        Account account = accountManager.getAccount(uuid);

       if(account == null){
            Criteria criteria = Criteria.create().filter("uuid", uuid.toString()).limit(1); //prepare for get account with uuid
            List<Account> accounts = dataStore.loadAccounts(criteria);
            if(!accounts.isEmpty()) {
                account = accounts.get(0);
                updateAccountBalances(account);
                accountManager.addAccountToCache(account); //añadir a cache
            }else{
                throw new AccountNotFoundException("Account not found");
            }
       }

        return account;
    }


    public List<Account> getAllAccounts() {
        return dataStore.loadAccounts(Criteria.create());
    }

    public List<Account> getOnlineAccounts() {
        return accountManager.getAccounts();
    }

   //TODO: TOP 10 ACC, añadir uso de la cache de CachedTopList class
    //todo: puedo tomar como criterio que la lista de tops sean los que mas suma de todas las monedas tienen
    public LinkedList<CachedTopListEntry> getTopAccounts(String currency) {
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
                    cache.add(new CachedTopListEntry(account.getDisplayName(), account.getBalance(currency))); //añado a la cache los datos encontrados

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


    private void updateAccountBalances(Account account) {
        Map<Currency, Double> updatedBalances = currencyManager.getCurrencies().stream()
                .collect(Collectors.toMap(
                        systemCurrency -> systemCurrency,
                        systemCurrency -> account.getBalances().entrySet().stream()
                                .filter(entry -> entry.getKey().getUuid().equals(systemCurrency.getUuid()))
                                .map(Map.Entry::getValue)
                                .findFirst()
                                .orElse(systemCurrency.getDefaultBalance())
                ));
        account.setBalances(updatedBalances);
    }

}
