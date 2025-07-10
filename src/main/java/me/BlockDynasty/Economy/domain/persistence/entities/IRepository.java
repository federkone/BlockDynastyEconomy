package me.BlockDynasty.Economy.domain.persistence.entities;

import me.BlockDynasty.Economy.domain.persistence.transaction.ITransactions;
import me.BlockDynasty.Economy.domain.account.Account;
import me.BlockDynasty.Economy.domain.currency.Currency;
import me.BlockDynasty.Economy.Infrastructure.repository.Criteria.Criteria;
import me.BlockDynasty.Economy.domain.result.Result;

import java.util.List;

//todo: evaluar hacer solo operaciones CRUD
public interface IRepository extends ITransactions {
    void loadCurrencies(); //todas las monedas all()
    List<Currency> loadCurrencies(Criteria criteria);
    void saveCurrency(Currency currency); //hace de update tambien
    void deleteCurrency(Currency currency);

    List<Account> loadAccounts(Criteria criteria); //una cuenta de un criterio
    Result<Account> loadAccountByUuid(String uuid);
    public Result<Account> loadAccountByName(String name);
    void createAccount(Account account);
    void saveAccount(Account account);
    //void deleteAccount(Account account);

    List<Account> getAccountsTopByCurrency(String currencyName, int limit, int offset);

    boolean isTopSupported();
    String getName();
    void close();
    void clearAll();

}