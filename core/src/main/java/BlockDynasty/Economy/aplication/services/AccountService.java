package BlockDynasty.Economy.aplication.services;

import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.services.IAccountService;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AccountService implements IAccountService {
    private final Map<String,List<Account>> accountsTopList;
    private final Map<UUID,Account> accountsOnlineUuid;
    private final Map<String,Account> accountsOnlineName;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final int expireCacheTopMinutes ;

    public AccountService(int expireCacheTopMinutes) {
        this.accountsOnlineUuid = new HashMap<>();
        this.accountsOnlineName = new HashMap<>();
        this.accountsTopList = new HashMap<>();
        this.expireCacheTopMinutes = expireCacheTopMinutes;
        startCacheClearingTask();
    }

    public void removeAccountFromCache(UUID uuid) {  //removeAccountFromCache
        Account account = accountsOnlineUuid.remove(uuid);
        if (account != null) {
            accountsOnlineName.remove(account.getNickname());
        }
    }
    public void removeAccountFromCache(String name) {
        Account account = accountsOnlineName.remove(name);
        if (account != null) {
            accountsOnlineUuid.remove(account.getUuid());
        }
    }

    public void addAccountToCache(Account account) {
        this.accountsOnlineUuid.put(account.getUuid(), account);
        this.accountsOnlineName.put(account.getNickname(), account);
    }

    public Collection<Account> getAccountsCache() {
        return this.accountsOnlineUuid.values();
    }
    public Account getAccountCache(String name){
        return  accountsOnlineName.getOrDefault(name,null);
    }
    public Account getAccountCache(UUID uuid){
        return accountsOnlineUuid.getOrDefault(uuid,null);
    }

    public void addAccountToTopList(Account account,String currencyName) {
        List<Account> accounts = accountsTopList.computeIfAbsent(currencyName, k -> new ArrayList<>());
        accounts.add(account);
    }
    public List<Account> getAccountsTopList(String currency) {
        return accountsTopList.getOrDefault(currency, new ArrayList<>());
    }
    public void clearTopListCache() {
        accountsTopList.clear();
    }
    private void startCacheClearingTask() {
        //System.out.println("Top list cache cleared");
        scheduler.scheduleAtFixedRate(this::clearTopListCache, 0,expireCacheTopMinutes, TimeUnit.MINUTES);
    }
}