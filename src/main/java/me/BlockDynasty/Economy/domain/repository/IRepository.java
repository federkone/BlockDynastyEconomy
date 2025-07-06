package me.BlockDynasty.Economy.domain.repository;

import me.BlockDynasty.Economy.domain.account.Account;
import me.BlockDynasty.Economy.domain.currency.Currency;
import me.BlockDynasty.Economy.domain.repository.Criteria.Criteria;
import me.BlockDynasty.Economy.domain.result.Result;
import me.BlockDynasty.Economy.domain.result.TransferResult;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

//todo: evaluar hacer solo operaciones CRUD
public interface IRepository extends ITransactions {
    void loadCurrencies(); //todas las monedas all()
    List<Currency> loadCurrencies(Criteria criteria);
    void saveCurrency(Currency currency); //hace de update tambien
    void deleteCurrency(Currency currency);

    List<Account> loadAccounts(Criteria criteria); //una cuenta de un criterio
    //void loadAccounts(Criteria criteria, Callback<Account> callback);
    void createAccount(Account account);
    void saveAccount(Account account);
    //void deleteAccount(Account account);

    List<Account> getAccountsTopByCurrency(String currencyName, int limit, int offset);

    boolean isTopSupported();
    String getName();
    void close();
    void clearAll();

}