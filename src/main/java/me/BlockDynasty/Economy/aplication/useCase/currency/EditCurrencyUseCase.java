package me.BlockDynasty.Economy.aplication.useCase.currency;

import me.BlockDynasty.Economy.aplication.bungee.UpdateForwarder;
import me.BlockDynasty.Economy.domain.currency.Currency;
import me.BlockDynasty.Economy.domain.currency.CurrencyCache;
import me.BlockDynasty.Economy.domain.currency.Exceptions.CurrencyColorUnformat;
import me.BlockDynasty.Economy.domain.currency.Exceptions.CurrencyNotFoundException;
import me.BlockDynasty.Economy.domain.currency.Exceptions.DecimalNotSupportedException;
import me.BlockDynasty.Economy.domain.repository.Exceptions.TransactionException;
import me.BlockDynasty.Economy.domain.repository.IRepository;
import org.bukkit.ChatColor;

import java.math.BigDecimal;

public class EditCurrencyUseCase {
    private final CurrencyCache currencyCache;
    private final IRepository dataStore;
    private final UpdateForwarder updateForwarder;

    public EditCurrencyUseCase(CurrencyCache currencyCache, UpdateForwarder updateForwarder, IRepository dataStore) {
        this.currencyCache = currencyCache;
        this.dataStore = dataStore;
        this.updateForwarder = updateForwarder;
    }


    public void editStartBal(String name, double startBal){
        Currency currency = currencyCache.getCurrency(name);
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
        Currency currency = currencyCache.getCurrency(currencyName);
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
        Currency currency = currencyCache.getCurrency(nameCurrency);

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
            Currency currency = currencyCache.getCurrency(nameCurrency);

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
        Currency currency = currencyCache.getCurrency(currencyName);

        if (currency.isDefaultCurrency()){
            return;
        }

        currencyCache.getCurrencies().forEach(c -> {
            if (c.isDefaultCurrency()){
                c.setDefaultCurrency(false);
                try {
                    dataStore.saveCurrency(c);
                    updateForwarder.sendUpdateMessage("currency", c.getUuid().toString());
                }catch (TransactionException e){
                    throw new TransactionException("Error save in setDefaultCurrency");
                }
            }
        });

        currency.setDefaultCurrency(true);
        currencyCache.updateDefaultCurrency();

        try {
            dataStore.saveCurrency(currency);
            updateForwarder.sendUpdateMessage("currency", currency.getUuid().toString());
        }catch (TransactionException e){
            throw new TransactionException("Error saving currency");
        }

    }

    public void setSingularName(String actualName, String newName){
        //todo: cambiar nombre de la moneda, verificar si existe el actualname tanto plural como singualr, y actualizar el mismo campo plural o singular
        Currency currency = currencyCache.getCurrency(actualName);

        if (currency == null){
            throw new CurrencyNotFoundException("Currency not found");
        }

        currency.setSingular(newName);
        try {
            dataStore.saveCurrency(currency);
            updateForwarder.sendUpdateMessage("currency", currency.getUuid().toString());
        }catch (TransactionException e){
            throw new TransactionException("Error saving currency");
        }
    }

    public void setPluralName(String actualName, String newName){
        Currency currency = currencyCache.getCurrency(actualName);

        if (currency == null){
            throw new CurrencyNotFoundException("Currency not found");
        }

        currency.setPlural(newName);
        try {
            dataStore.saveCurrency(currency);
            updateForwarder.sendUpdateMessage("currency", currency.getUuid().toString());
        }catch (TransactionException e){
            throw new TransactionException("Error saving currency");
        }

    }

}
