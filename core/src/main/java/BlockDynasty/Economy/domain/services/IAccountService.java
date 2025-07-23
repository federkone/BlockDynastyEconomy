package BlockDynasty.Economy.domain.services;

import BlockDynasty.Economy.domain.entities.account.Account;

import java.util.*;

public interface IAccountService {
    void removeAccountFromCache(UUID uuid);
    void addAccountToCache(Account account);
    Collection<Account> getAccountsCache();
    Account getAccountCache(String name);
    Account getAccountCache(UUID uuid);
    void addAccountToTopList(Account account,String currencyName);
    List<Account> getAccountsTopList(String currency);
    void clearTopListCache();
}
