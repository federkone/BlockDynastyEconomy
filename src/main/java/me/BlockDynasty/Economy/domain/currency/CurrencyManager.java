package me.BlockDynasty.Economy.domain.currency;


import me.BlockDynasty.Economy.domain.repository.Criteria.Criteria;
import me.BlockDynasty.Economy.domain.repository.IRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CurrencyManager {

    private List<Currency> currencies ;

    public CurrencyManager(IRepository repository) {
        this.currencies = new ArrayList<>();
        this.currencies = repository.loadCurrencies(Criteria.create());

    }

    public boolean currencyExist(String name) {
        return currencies.stream()
                .anyMatch(currency -> currency.getSingular().equalsIgnoreCase(name) || currency.getPlural().equalsIgnoreCase(name));
    }

    public Currency getCurrency(String name) {
        return currencies.stream()
                .filter(currency -> currency.getSingular().equalsIgnoreCase(name) || currency.getPlural().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public Currency getCurrency(UUID uuid) {
        return currencies.stream()
                .filter(currency -> currency.getUuid().equals(uuid))
                .findFirst()
                .orElse(null);
    }

    public Currency getDefaultCurrency() {
        return currencies.stream()
                .filter(Currency::isDefaultCurrency)
                .findFirst()
                .orElse(null);
    }

    public void remove(Currency currency){
        currencies.remove(currency);
    }

    public void add(Currency currency) {
        if(currencies.contains(currency))return;

        currencies.add(currency);
    }

    public void add(List<Currency> currencyList) {
        currencies.addAll(currencyList);
    }
    public List<Currency> getCurrencies() {
        return currencies;
    }
}
