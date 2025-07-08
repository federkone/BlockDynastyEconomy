package me.BlockDynasty.Economy.domain.services;

import me.BlockDynasty.Economy.domain.currency.Currency;

import java.util.List;
import java.util.UUID;

public interface ICurrencyService {
    void add(Currency currency);
    void add(List<Currency> currencyList);
    void remove(Currency currency);
    List<Currency> getCurrencies();
    Currency getCurrency(String name);
    Currency getCurrency(UUID uuid);
    Currency getDefaultCurrency();
    boolean currencyExist(String name);
    void updateDefaultCurrency();
}
