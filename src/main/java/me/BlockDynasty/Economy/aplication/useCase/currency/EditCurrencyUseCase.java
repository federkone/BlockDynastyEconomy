package me.BlockDynasty.Economy.aplication.useCase.currency;

import me.BlockDynasty.Economy.aplication.bungee.UpdateForwarder;
import me.BlockDynasty.Economy.domain.currency.Currency;
import me.BlockDynasty.Economy.domain.currency.CurrencyManager;
import me.BlockDynasty.Economy.domain.currency.Exceptions.CurrencyColorUnformat;
import me.BlockDynasty.Economy.domain.currency.Exceptions.CurrencyNotFoundException;
import me.BlockDynasty.Economy.domain.currency.Exceptions.DecimalNotSupportedException;
import me.BlockDynasty.Economy.domain.repository.Exceptions.TransactionException;
import me.BlockDynasty.Economy.domain.repository.IRepository;
import org.bukkit.ChatColor;

import java.math.BigDecimal;

public class EditCurrencyUseCase {
    private final CurrencyManager currencyManager;
    private final IRepository dataStore;
    private final UpdateForwarder updateForwarder;

    public EditCurrencyUseCase(CurrencyManager currencyManager, UpdateForwarder updateForwarder, IRepository dataStore) {
        this.currencyManager = currencyManager;
        this.dataStore = dataStore;
        this.updateForwarder = updateForwarder;
    }


    public void editStartBal(String name, double startBal){
        Currency currency = currencyManager.getCurrency(name);
        if (currency == null){
            throw new CurrencyNotFoundException("Currency not found");
        }

        if (!currency.isDecimalSupported() && startBal % 1 != 0) {
            throw new DecimalNotSupportedException("Currency does not support decimals");
        }

        currency.setStartBalance(BigDecimal.valueOf(startBal));
        try {
            dataStore.saveCurrency(currency);
            //actualizar cache no hace falta por que ya traje la referencia de la moneda de currencymanager
            updateForwarder.sendUpdateMessage("currency", currency.getUuid().toString());
        }catch (TransactionException e){
            throw new TransactionException("Error creating currency");
        }
    }

        public void setCurrencyRate(String currencyName, double rate){
        Currency currency = currencyManager.getCurrency(currencyName);
        if (currency == null){
            throw new CurrencyNotFoundException("Currency not found");
        }
        currency.setExchangeRate(rate);
        try {
            dataStore.saveCurrency(currency);
            updateForwarder.sendUpdateMessage("currency", currency.getUuid().toString());
        }catch (TransactionException e){
            throw new TransactionException("Error creating currency");
        }
    }


       public void editColor(String nameCurrency, String colorString){
        Currency currency = currencyManager.getCurrency(nameCurrency);

        if (currency == null){
            throw new CurrencyNotFoundException("Currency not found");
        }

        ChatColor color = ChatColor.valueOf(colorString);
        if (color.isFormat()) {
            throw new CurrencyColorUnformat("currency color is not a format");
        }

        currency.setColor(color);
        try {
            dataStore.saveCurrency(currency);
            //actualizar cache no hace falta por que ya traje la referencia de la moneda de currencymanager
            updateForwarder.sendUpdateMessage("currency", currency.getUuid().toString());
        }catch (TransactionException e){
            throw new TransactionException("Error creating currency");
        }

    }

    public void editSymbol(String nameCurrency,String symbol){
            Currency currency = currencyManager.getCurrency(nameCurrency);

        if (currency == null){
            throw new CurrencyNotFoundException("Currency not found");
        }

        currency.setSymbol(symbol);
        try {
            dataStore.saveCurrency(currency);
            //actualizar cache no hace falta por que ya traje la referencia de la moneda de currencymanager
            updateForwarder.sendUpdateMessage("currency", currency.getUuid().toString());
        }catch (TransactionException e){
            throw new TransactionException("Error creating currency");
        }
    }

        public void setDefaultCurrency(String currencyName){
        Currency currency = currencyManager.getCurrency(currencyName);
        if (currency == null){
            throw new CurrencyNotFoundException("Currency not found");
        }

        if (currency.isDefaultCurrency()){
            return;
        }

        currencyManager.getCurrencies().forEach(c -> {
            if (c.isDefaultCurrency()){
                c.setDefaultCurrency(false);
                try {
                    dataStore.saveCurrency(c);
                    updateForwarder.sendUpdateMessage("currency", c.getUuid().toString());
                }catch (TransactionException e){
                    throw new TransactionException("Error creating currency");
                }
            }
        });

        currency.setDefaultCurrency(true);
        try {
            dataStore.saveCurrency(currency);
            updateForwarder.sendUpdateMessage("currency", currency.getUuid().toString());
        }catch (TransactionException e){
            throw new TransactionException("Error creating currency");
        }
    }

}
