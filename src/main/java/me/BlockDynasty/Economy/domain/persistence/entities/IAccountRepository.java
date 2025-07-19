package me.BlockDynasty.Economy.domain.persistence.entities;

import me.BlockDynasty.Economy.domain.entities.account.Account;

import java.util.List;

public interface IAccountRepository {
    List<Account> findAll();
    Account findByUuid(String uuid);
    Account findByNickname(String nickname);
    void save(Account account);
    void delete(Account account);
    void update(Account account);
    void create(Account account);
    List<Account> getAccountsTopByCurrency(String currencyName, int limit, int offset);

}
