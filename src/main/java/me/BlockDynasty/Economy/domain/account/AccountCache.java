package me.BlockDynasty.Economy.domain.account;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AccountCache {
    private final Set<Account> accountsOnline;
    private final Map<String,List<Account>> accountsTopList;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final int expireCacheTopMinutes ;

    public AccountCache(int expireCacheTopMinutes) {
        this.accountsOnline = new HashSet<>(); //para no repetir cuentas
        this.accountsTopList = new HashMap<>();
        this.expireCacheTopMinutes = expireCacheTopMinutes;
        startCacheClearingTask();
    }
    public void removeAccount(UUID uuid) {  //removeAccountFromCache
        accountsOnline.removeIf(account -> account.getUuid().equals(uuid));
    }

    public void addAccountToCache(Account account) {
        if (this.accountsOnline.contains(account)) return;

        this.accountsOnline.add(account);
    }

    public Set<Account> getAccounts() {
        return accountsOnline;
    }

    public Account getAccount(String name){
        return  accountsOnline.stream()
                .filter(a -> name.equals(a.getNickname()))
                .findFirst()
                .orElse(null);
    }
    public Account getAccount(UUID uuid){
        return accountsOnline.stream()
                .filter(a -> uuid.equals(a.getUuid()))
                .findFirst()
                .orElse(null);
    }


    public void addAccountToTopList(Account account,String currencyName) {
        List<Account> accounts = accountsTopList.computeIfAbsent(currencyName, k -> new ArrayList<>());
        accounts.add(account);
    }

    public List<Account> getAccountsTopList(String currency) {
        return accountsTopList.getOrDefault(currency, new ArrayList<>());
    }

    private void startCacheClearingTask() { //todo: permitir traer la configuracion de tiempo de limpieza desde el archivo config.yaml
        //System.out.println("Top list cache cleared");
        scheduler.scheduleAtFixedRate(this::clearTopListCache, 0,expireCacheTopMinutes, TimeUnit.MINUTES);
    }

    public void clearTopListCache() {
        accountsTopList.clear();
    }



}