package me.BlockDynasty.Economy.aplication.useCase.currency;


import me.BlockDynasty.Economy.aplication.bungee.UpdateForwarder;
import me.BlockDynasty.Economy.domain.currency.Currency;
import me.BlockDynasty.Economy.domain.currency.CurrencyManager;
import me.BlockDynasty.Economy.domain.currency.Exceptions.CurrencyAlreadyExist;
import me.BlockDynasty.Economy.domain.currency.Exceptions.CurrencyColorUnformat;
import me.BlockDynasty.Economy.domain.currency.Exceptions.CurrencyNotFoundException;
import me.BlockDynasty.Economy.domain.currency.Exceptions.DecimalNotSupportedException;
import me.BlockDynasty.Economy.domain.repository.Exceptions.TransactionException;
import me.BlockDynasty.Economy.domain.repository.IRepository;
import org.bukkit.ChatColor;

import java.util.List;
import java.util.UUID;

//TODO :CREATE CURRENCY, NOTA IMPORTANTE, VALIDAR QUE SI YA EXISTE UNA CURRENCY POR DEFAULT NO SE PUEDA CREAR, Y EN EL CASO DE QUERER SETEAR UNA POR DEFECTO DEBEMOS DESASER LA ANTERIOR DEFAULT
public class CreateCurrencyUseCase {
    private final CurrencyManager currencyManager;
    private final IRepository dataStore;
    private final UpdateForwarder updateForwarder;

    public CreateCurrencyUseCase(CurrencyManager currencyManager, UpdateForwarder updateForwarder,IRepository dataStore) {
        this.currencyManager = currencyManager;
        this.dataStore = dataStore;
        this.updateForwarder = updateForwarder;
    }

    public void createCurrency(String singular,String plural){
        if (currencyManager.currencyExist(singular) || currencyManager.currencyExist(plural)){
            throw new CurrencyAlreadyExist("Currency already exist");
        }

        Currency currency = new Currency(UUID.randomUUID(), singular, plural);
        currency.setExchangeRate(1.0);
        if(currencyManager.getCurrencies().isEmpty()) {  //setear por defecto si es la unica moneda en el sistema
            currency.setDefaultCurrency(true);
        }

        try {
            dataStore.saveCurrency(currency);
            currencyManager.add(currency);//cache
            updateForwarder.sendUpdateMessage("currency", currency.getUuid().toString());
        }catch (TransactionException e){
            throw new TransactionException("Error creating currency");
        }

    }

    public List<Currency> getCurrencies(){
        return currencyManager.getCurrencies();
    }

    public Currency getCurrency(String name){
        return currencyManager.getCurrency(name);
    }

    public void editStartBal(String name, double startBal){
        Currency currency = currencyManager.getCurrency(name);
        if (currency == null){
            throw new CurrencyNotFoundException("Currency not found");
        }

        if (!currency.isDecimalSupported() && startBal % 1 != 0) {
            throw new DecimalNotSupportedException("Currency does not support decimals");
        }

        currency.setStartBalance(startBal);
        try {
            dataStore.saveCurrency(currency);
            //actualizar cache no hace falta por que ya traje la referencia de la moneda de currencymanager
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

    public void togglePayable(String currencyName){
        Currency currency = currencyManager.getCurrency(currencyName);
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
        Currency currency = currencyManager.getCurrency(currencyName);
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

    //todo abstraer a otro caso de uso?
    public void deleteCurrency(String currencyName){
        Currency currency = currencyManager.getCurrency(currencyName);

        if (currency == null){
            throw new CurrencyNotFoundException("Currency not found");
        }
        if (currency.isDefaultCurrency()){
            throw new CurrencyNotFoundException("Currency is default");
        }
        try {
            dataStore.deleteCurrency(currency);
            //plugin.getAccountManager().getAccounts().stream().filter(account -> account.getBalances().containsKey(currency)).forEach(account -> account.getBalances().remove(currency)); //TODO: ELIMINAR LAS CURRENCY Y BALANCE DE TODOS LOS USUARIOS?
            currencyManager.remove(currency);  //TODO:agregar a currency manager ub metodo para remover
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
}
