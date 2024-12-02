package me.BlockDynasty.Economy.domain.repository;

import me.BlockDynasty.Economy.domain.account.Account;
import me.BlockDynasty.Economy.domain.currency.CachedTopListEntry;
import me.BlockDynasty.Economy.domain.currency.Currency;
import me.BlockDynasty.Economy.domain.repository.Criteria.Criteria;

import java.util.LinkedList;
import java.util.List;

public interface IRepository {

    void loadCurrencies(); //todas las monedas all()
    List<Currency> loadCurrencies(Criteria criteria);
    void saveCurrency(Currency currency); //hace de update tambien
    void deleteCurrency(Currency currency);

    List<Account> loadAccounts(Criteria criteria); //una cuenta de un criterio
    //void loadAccounts(Criteria criteria, Callback<Account> callback);
    void createAccount(Account account);
    void saveAccount(Account account);
    //void deleteAccount(Account account);
    public void transfer(Account userFrom, Account userTo);

    //get top list se va a eliminar ude jar
    void getTopList(Currency currency, int offset, int amount, Callback<LinkedList<CachedTopListEntry>> callback);
    List<Account> getAccountsByCurrency(String currencyName,int limit,int offset);

    boolean isTopSupported();
    String getName();
    void close();

}