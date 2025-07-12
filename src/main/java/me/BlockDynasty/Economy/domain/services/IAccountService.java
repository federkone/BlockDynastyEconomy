package me.BlockDynasty.Economy.domain.services;

import me.BlockDynasty.Economy.domain.entities.account.Account;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface IAccountService {
    void removeAccountFromCache(UUID uuid);
    void addAccountToCache(Account account);
    Set<Account> getAccountsCache();
    Account getAccountCache(String name);
    Account getAccountCache(UUID uuid);
    void addAccountToTopList(Account account,String currencyName);
    List<Account> getAccountsTopList(String currency);
    void clearTopListCache();
}
