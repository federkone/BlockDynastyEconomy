package BlockDynasty.Economy.domain.services;

import BlockDynasty.Economy.domain.entities.currency.Currency;

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
    boolean existsDefaultCurrency();
}
