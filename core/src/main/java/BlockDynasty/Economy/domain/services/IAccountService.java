package BlockDynasty.Economy.domain.services;

import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.result.Result;

import java.util.*;

public interface IAccountService {
    void removeAccountOnline(UUID uuid);
    void removeAccountOnline(String name);
    void addAccountToOnline(Account account);
    List<Account> getAccountsOnline();
    List<Account> getAccountsOffline();
    Account getAccount(String name);
    Account getAccount(UUID uuid);
    Account getAccountOnline(String name);
    Account getAccountOnline(UUID uuid);

    Result<Void> checkNameChange(Account account , String newName);
    Result<Void> checkUuidChange(Account account, UUID newUuid);
    void addAccountToTopList(Account account,String currencyName);
    List<Account> getAccountsTopList(String currency);
    void clearTopList();

    void syncOnlineAccount (Account account);
    void syncDbWithOnlineAccounts();
    void syncOnlineAccount(UUID uuid);


}
