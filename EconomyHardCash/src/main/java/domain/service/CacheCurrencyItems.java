package domain.service;

import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.currency.ICurrency;
import aplication.useCase.items.ItemBase64Creator;
import domain.entity.currency.ItemStackCurrency;
import domain.entity.platform.HardCashCreator;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CacheCurrencyItems {
    private SearchCurrencyUseCase searchCurrencyUseCase;
    private Map<UUID, Currencywrapper> items;
    private ItemCreator itemCreator;

    public CacheCurrencyItems(SearchCurrencyUseCase searchCurrencyUseCase, HardCashCreator hardCashCreator) {
        this.items = new HashMap<>();
        this.searchCurrencyUseCase = searchCurrencyUseCase;
        this.itemCreator = new ItemBase64Creator(hardCashCreator);
        this.updateCurrencies();
    }

    public Currencywrapper getSimilarItem(ItemStackCurrency item) {
        return this.items.values().stream()
                .filter(cachedItem -> cachedItem.isSimilar(item))
                .findFirst()
                .orElse(null);
    }

    public void updateCurrencies() {
        this.searchCurrencyUseCase.getCurrencies()
                //.filter(currency -> currency.getBase64Item() != null && !currency.getBase64Item().isEmpty())
                .forEach(currency -> {
                    ItemStackCurrency item =itemCreator.create(currency, BigDecimal.ONE);
                    if (item != null && !item.isNull()){
                        this.items.put(currency.getUuid(), new Currencywrapper(item, currency));
                    }else{
                        this.items.remove(currency.getUuid());
                    }
                });
    }

    public static class Currencywrapper {
        private ItemStackCurrency item;
        private ICurrency currency;

        public Currencywrapper(ItemStackCurrency item, ICurrency currency) {
            this.currency = currency;
            this.item = item;
        }

        public ItemStackCurrency getItem() {
            return item;
        }

        public boolean isSimilar(ItemStackCurrency other) {
            return item.isSimilar(other);
        }

        public ICurrency getCurrency() {
            return currency;
        }
    }
}