package me.BlockDynasty.Economy.aplication.useCase.currency;

import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.Integrations.bungee.Courier;
import me.BlockDynasty.Economy.domain.currency.Currency;
import me.BlockDynasty.Economy.Infrastructure.services.CurrencyService;
import me.BlockDynasty.Economy.domain.currency.Exceptions.CurrencyNotFoundException;
import me.BlockDynasty.Economy.Infrastructure.repository.Exceptions.TransactionException;
import me.BlockDynasty.Economy.domain.persistence.entities.IRepository;
import me.BlockDynasty.Economy.domain.services.ICurrencyService;

public class ToggleFeaturesUseCase {
        private final ICurrencyService currencyService;
        private final IRepository dataStore;
        private final Courier updateForwarder;

     public ToggleFeaturesUseCase(ICurrencyService currencyService, IRepository dataStore, Courier updateForwarder){
            this.currencyService = currencyService;
            this.dataStore = dataStore;
            this.updateForwarder = updateForwarder;
     }

    public void togglePayable(String currencyName){
        Currency currency = currencyService.getCurrency(currencyName);
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
        Currency currency = currencyService.getCurrency(currencyName);
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
