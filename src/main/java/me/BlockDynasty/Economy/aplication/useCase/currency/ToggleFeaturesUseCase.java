package me.BlockDynasty.Economy.aplication.useCase.currency;

import me.BlockDynasty.Integrations.bungee.UpdateForwarder;
import me.BlockDynasty.Economy.domain.currency.Currency;
import me.BlockDynasty.Economy.domain.currency.CurrencyCache;
import me.BlockDynasty.Economy.domain.currency.Exceptions.CurrencyNotFoundException;
import me.BlockDynasty.Economy.domain.repository.Exceptions.TransactionException;
import me.BlockDynasty.Economy.domain.repository.IRepository;

public class ToggleFeaturesUseCase {
        private final CurrencyCache currencyCache;
        private final IRepository dataStore;
        private final UpdateForwarder updateForwarder;

     public ToggleFeaturesUseCase(CurrencyCache currencyCache, IRepository dataStore, UpdateForwarder updateForwarder){
            this.currencyCache = currencyCache;
            this.dataStore = dataStore;
            this.updateForwarder = updateForwarder;
     }

    public void togglePayable(String currencyName){
        Currency currency = currencyCache.getCurrency(currencyName);
        if (currency == null){
            throw new CurrencyNotFoundException("Currency not found");
        }
        currency.setPayable(!currency.isPayable());
        try {
            dataStore.saveCurrency(currency);
            updateForwarder.sendUpdateMessage("currency", currency.getUuid().toString());
        }catch (TransactionException e){
            throw new TransactionException("Error creating currency");
        }
    }

    public void toggleDecimals(String currencyName){
        Currency currency = currencyCache.getCurrency(currencyName);
        if (currency == null){
            throw new CurrencyNotFoundException("Currency not found");
        }
        currency.setDecimalSupported(!currency.isDecimalSupported());
        try {
            dataStore.saveCurrency(currency);
            updateForwarder.sendUpdateMessage("currency", currency.getUuid().toString());
        }catch (TransactionException e){
            throw new TransactionException("Error creating currency");
        }
    }

}
