package me.BlockDynasty.Economy.aplication.services;

import me.BlockDynasty.Economy.Infrastructure.repositoryOld.Criteria.Criteria;
import me.BlockDynasty.Economy.domain.entities.currency.Currency;
import me.BlockDynasty.Economy.domain.persistence.entities.IRepository;
import me.BlockDynasty.Economy.domain.services.ICurrencyService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CurrencyService implements ICurrencyService {
    private List<Currency> currencies ;
    public Currency defaultCurrency;

    public CurrencyService(IRepository repository) {
        this.currencies = new ArrayList<>();
        this.currencies = repository.loadCurrencies(Criteria.create());

        if(currencies.isEmpty()){
            Currency defaultCurrency = new Currency(UUID.randomUUID(), "Default", "Default");
            defaultCurrency.setDefaultCurrency(true);
            repository.saveCurrency(defaultCurrency);
            currencies.add(defaultCurrency);
        }

        updateDefaultCurrency();

    }

    public void add(Currency currency) {
        if(currencies.contains(currency))return;

        currencies.add(currency);
    }

    public void add(List<Currency> currencyList) {
        currencies.addAll(currencyList);
    }

    public void remove(Currency currency){
        currencies.remove(currency);
    }

    public List<Currency> getCurrencies() {
        return currencies;
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
        return this.defaultCurrency;
    }

    public boolean currencyExist(String name) {
        return currencies.stream()
                .anyMatch(currency -> currency.getSingular().equalsIgnoreCase(name) || currency.getPlural().equalsIgnoreCase(name));
    }

    public void updateDefaultCurrency() {
        defaultCurrency = currencies.stream()
                .filter(Currency::isDefaultCurrency)
                .findFirst()
                .orElse(defaultCurrency);
    }

    @Override
    public boolean existsDefaultCurrency() {
        return this.currencies.stream().anyMatch(Currency::isDefaultCurrency);
    }

}
