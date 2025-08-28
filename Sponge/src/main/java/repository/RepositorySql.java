package repository;

import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.domain.result.TransferResult;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class RepositorySql implements IRepository {
    @Override
    public List<Currency> loadCurrencies() {
        return new ArrayList<>();
    }

    @Override
    public Result<Currency> loadCurrencyByName(String name) {
        return null;
    }

    @Override
    public Result<Currency> loadCurrencyByUuid(String uuid) {
        return null;
    }

    @Override
    public Result<Currency> loadDefaultCurrency() {
        return null;
    }

    @Override
    public void saveCurrency(Currency currency) {

    }

    @Override
    public void deleteCurrency(Currency currency) {

    }

    @Override
    public List<Account> loadAccounts() {
        return new ArrayList<>();
    }

    @Override
    public Result<Account> loadAccountByUuid(String uuid) {
        return null;
    }

    @Override
    public Result<Account> loadAccountByName(String name) {
        return null;
    }

    @Override
    public void createAccount(Account account) {

    }

    @Override
    public void saveAccount(Account account) {

    }

    @Override
    public Result<Void> deleteAccount(Account account) {
        return null;
    }

    @Override
    public List<Account> getAccountsTopByCurrency(String currencyName, int limit, int offset) {
        return new ArrayList<>();
    }

    @Override
    public boolean isTopSupported() {
        return false;
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public void close() {

    }

    @Override
    public void clearAll() {

    }

    @Override
    public Result<TransferResult> transfer(String fromUuid, String toUuid, Currency currency, BigDecimal amount) {
        return null;
    }

    @Override
    public Result<Account> withdraw(String accountUuid, Currency currency, BigDecimal amount) {
        return null;
    }

    @Override
    public Result<Account> deposit(String accountUuid, Currency currency, BigDecimal amount) {
        return null;
    }

    @Override
    public Result<Account> exchange(String fromUuid, Currency fromCurrency, BigDecimal amountFrom, Currency toCurrency, BigDecimal amountTo) {
        return null;
    }

    @Override
    public Result<TransferResult> trade(String fromUuid, String toUuid, Currency fromCurrency, Currency toCurrency, BigDecimal amountFrom, BigDecimal amountTo) {
        return null;
    }

    @Override
    public Result<Account> setBalance(String accountUuid, Currency currency, BigDecimal amount) {
        return null;
    }
}
