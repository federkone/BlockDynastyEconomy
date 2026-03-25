package com.blockdynasty.economy.apiImplement;

import com.BlockDynasty.api.DynastyEconomy;
import com.BlockDynasty.api.EconomyResponse;
import com.BlockDynasty.api.entity.Account;
import com.BlockDynasty.api.entity.Currency;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

class DynastyEconomyProxy implements DynastyEconomy{
    private final InternalProvider apiCustomSupplier;

    public DynastyEconomyProxy(InternalProvider apiCustomSupplier) {
        this.apiCustomSupplier = apiCustomSupplier;
    }

    @Override
    public EconomyResponse deposit(UUID uuid, BigDecimal amount) {
        return this.apiCustomSupplier.getInternal().deposit(uuid, amount);
    }

    @Override
    public EconomyResponse deposit(String name, BigDecimal amount) {
        return this.apiCustomSupplier.getInternal().deposit(name, amount);
    }

    @Override
    public Currency getDefaultCurrency() {
        return this.apiCustomSupplier.getInternal().getDefaultCurrency();
    }

    @Override
    public List<Account> getAccountsOffline() {
        return this.apiCustomSupplier.getInternal().getAccountsOffline();
    }

    @Override
    public EconomyResponse withdraw(UUID uuid, BigDecimal amount) {
        return this.apiCustomSupplier.getInternal().withdraw(uuid, amount);
    }

    @Override
    public EconomyResponse withdraw(String name, BigDecimal amount) {
        return this.apiCustomSupplier.getInternal().withdraw(name, amount);
    }

    @Override
    public EconomyResponse setBalance(UUID uuid, BigDecimal amount) {
        return this.apiCustomSupplier.getInternal().setBalance(uuid, amount);
    }

    @Override
    public EconomyResponse setBalance(String name, BigDecimal amount, String currency) {
        return this.apiCustomSupplier.getInternal().setBalance(name, amount, currency);
    }

    @Override
    public EconomyResponse setBalance(UUID uuid, BigDecimal amount, String currency) {
        return this.apiCustomSupplier.getInternal().setBalance(uuid, amount, currency);
    }

    @Override
    public EconomyResponse setBalance(String name, BigDecimal amount) {
        return this.apiCustomSupplier.getInternal().setBalance(name, amount);
    }

    @Override
    public EconomyResponse deposit(UUID uuid, BigDecimal amount, String currency) {
        return this.apiCustomSupplier.getInternal().deposit(uuid, amount, currency);
    }

    @Override
    public EconomyResponse deposit(String name, BigDecimal amount, String currency) {
        return this.apiCustomSupplier.getInternal().deposit(name, amount, currency);
    }

    @Override
    public EconomyResponse withdraw(UUID uuid, BigDecimal amount, String currency) {
        return this.apiCustomSupplier.getInternal().withdraw(uuid, amount, currency);
    }

    @Override
    public EconomyResponse withdraw(String name, BigDecimal amount, String currency) {
        return this.apiCustomSupplier.getInternal().withdraw(name, amount, currency);
    }

    @Override
    public BigDecimal getBalance(UUID uuid) {
        return this.apiCustomSupplier.getInternal().getBalance(uuid);
    }

    @Override
    public BigDecimal getBalance(String name) {
        return this.apiCustomSupplier.getInternal().getBalance(name);
    }

    @Override
    public BigDecimal getBalance(UUID uuid, String currency) {
        return this.apiCustomSupplier.getInternal().getBalance(uuid, currency);
    }

    @Override
    public BigDecimal getBalance(String name, String currency) {
        return this.apiCustomSupplier.getInternal().getBalance(name, currency);
    }

    @Override
    public EconomyResponse exchange(UUID uuid, String currencyFrom, String currencyTo, BigDecimal amountFrom, BigDecimal amountTo) {
        return this.apiCustomSupplier.getInternal().exchange(uuid, currencyFrom, currencyTo, amountFrom, amountTo);
    }

    @Override
    public EconomyResponse transfer(UUID userFrom, UUID userTo, String currency, BigDecimal amount) {
        return this.apiCustomSupplier.getInternal().transfer(userFrom, userTo, currency, amount);
    }

    @Override
    public EconomyResponse transfer(String userFrom, String userTo, String currency, BigDecimal amount) {
        return this.apiCustomSupplier.getInternal().transfer(userFrom, userTo, currency, amount);
    }

    @Override
    public EconomyResponse trade(UUID userFrom, UUID userTo, String currencyFrom, String currencyTo, BigDecimal amountFrom, BigDecimal amountTo) {
        return this.apiCustomSupplier.getInternal().trade(userFrom, userTo, currencyFrom, currencyTo, amountFrom, amountTo);
    }

    @Override
    public boolean hasAmount(UUID uuid, BigDecimal amount) {
        return this.apiCustomSupplier.getInternal().hasAmount(uuid, amount);
    }

    @Override
    public boolean hasAmount(String name, BigDecimal amount) {
        return this.apiCustomSupplier.getInternal().hasAmount(name, amount);
    }

    @Override
    public boolean hasAmount(UUID uuid, BigDecimal amount, String currency) {
        return this.apiCustomSupplier.getInternal().hasAmount(uuid, amount, currency);
    }

    @Override
    public boolean hasAmount(String name, BigDecimal amount, String currency) {
        return this.apiCustomSupplier.getInternal().hasAmount(name, amount, currency);
    }

    @Override
    public boolean existCurrency(String nameCurrency) {
        return this.apiCustomSupplier.getInternal().existCurrency(nameCurrency);
    }

    @Override
    public boolean existAccount(UUID uuid) {
        return this.apiCustomSupplier.getInternal().existAccount(uuid);
    }

    @Override
    public boolean existAccount(String name) {
        return this.apiCustomSupplier.getInternal().existAccount(name);
    }

    @Override
    public String getDefaultCurrencyNamePlural() {
        return this.apiCustomSupplier.getInternal().getDefaultCurrencyNamePlural();
    }

    @Override
    public String getNameCurrencyPlural(String nameCurrency) {
        return this.apiCustomSupplier.getInternal().getNameCurrencyPlural(nameCurrency);
    }

    @Override
    public String getNameCurrencySingular(String nameCurrency) {
        return this.apiCustomSupplier.getInternal().getNameCurrencySingular(nameCurrency);
    }

    @Override
    public String getDefaultCurrencyNameSingular() {
        return this.apiCustomSupplier.getInternal().getDefaultCurrencyNameSingular();
    }

    @Override
    public String format(BigDecimal amount) {
        return this.apiCustomSupplier.getInternal().format(amount);
    }

    @Override
    public String format(BigDecimal amount, String currency) {
        return this.apiCustomSupplier.getInternal().format(amount, currency);
    }

    @Override
    public List<String> getCurrenciesNamesList() {
        return this.apiCustomSupplier.getInternal().getCurrenciesNamesList();
    }

    @Override
    public List<Currency> getCurrencies() {
        return this.apiCustomSupplier.getInternal().getCurrencies();
    }

    @Override
    public EconomyResponse createAccount(UUID accountID, String name) {
        return this.apiCustomSupplier.getInternal().createAccount(accountID, name);
    }

    @Override
    public EconomyResponse deleteAccount(UUID accountID) {
        return this.apiCustomSupplier.getInternal().deleteAccount(accountID);
    }

    @Override
    public EconomyResponse deleteAccount(String name) {
        return this.apiCustomSupplier.getInternal().deleteAccount(name);
    }

    @Override
    public EconomyResponse blockAccountTransactions(UUID accountID) {
        return this.apiCustomSupplier.getInternal().blockAccountTransactions(accountID);
    }

    @Override
    public EconomyResponse unblockAccountTransactions(UUID accountID) {
        return this.apiCustomSupplier.getInternal().unblockAccountTransactions(accountID);
    }

    @Override
    public EconomyResponse createCurrency(String plural, String singular) {
        return this.apiCustomSupplier.getInternal().createCurrency(plural, singular);
    }

    @Override
    public EconomyResponse deleteCurrency(String name) {
        return this.apiCustomSupplier.getInternal().deleteCurrency(name);
    }

    @Override
    public Account getAccount(UUID uuid) {
        return this.apiCustomSupplier.getInternal().getAccount(uuid);
    }

    @Override
    public Account getAccount(String name) {
        return this.apiCustomSupplier.getInternal().getAccount(name);
    }

    @Override
    public List<Account> getTopAccounts(int amount, String currency) {
        return this.apiCustomSupplier.getInternal().getTopAccounts(amount, currency);
    }

    @Override
    public EconomyResponse setCurrencyStartBalance(String name, BigDecimal startBal) {
        return this.apiCustomSupplier.getInternal().setCurrencyStartBalance(name, startBal);
    }

    @Override
    public EconomyResponse setCurrencyColor(String currencyName, String colorString) {
        return this.apiCustomSupplier.getInternal().setCurrencyColor(currencyName, colorString);
    }

    @Override
    public EconomyResponse setCurrencyRate(String currencyName, BigDecimal rate) {
        return this.apiCustomSupplier.getInternal().setCurrencyRate(currencyName, rate);
    }

    @Override
    public EconomyResponse setCurrencyDecimalSupport(String currencyName, boolean supportDecimals) {
        return this.apiCustomSupplier.getInternal().setCurrencyDecimalSupport(currencyName, supportDecimals);
    }

    @Override
    public EconomyResponse setCurrencySymbol(String currencyName, String symbol) {
        return this.apiCustomSupplier.getInternal().setCurrencySymbol(currencyName, symbol);
    }

    @Override
    public EconomyResponse setDefaultCurrency(String currencyName) {
        return this.apiCustomSupplier.getInternal().setDefaultCurrency(currencyName);
    }

    @Override
    public EconomyResponse setSingularName(String currentName, String newName) {
        return this.apiCustomSupplier.getInternal().setSingularName(currentName, newName);
    }

    @Override
    public EconomyResponse setPluralName(String currentName, String newName) {
        return this.apiCustomSupplier.getInternal().setPluralName(currentName, newName);
    }

    @Override
    public EconomyResponse setPayable(String currencyName, boolean isPayable) {
        return this.apiCustomSupplier.getInternal().setPayable(currencyName, isPayable);
    }

    @Override
    public EconomyResponse saveCurrency(Currency currency) {
        return this.apiCustomSupplier.getInternal().saveCurrency(currency);
    }

    @Override
    public Currency getCurrency(String name) {
        return this.apiCustomSupplier.getInternal().getCurrency(name);
    }

    @Override
    public UUID getId() {
        return this.apiCustomSupplier.getId();
    }
}
