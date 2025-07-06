package me.BlockDynasty.Economy.aplication.useCase.currency;


import me.BlockDynasty.Integrations.bungee.UpdateForwarder;
import me.BlockDynasty.Economy.aplication.useCase.account.GetAccountsUseCase;
import me.BlockDynasty.Economy.domain.currency.Currency;
import me.BlockDynasty.Economy.domain.currency.CurrencyCache;
import me.BlockDynasty.Economy.domain.currency.Exceptions.CurrencyAlreadyExist;
import me.BlockDynasty.Economy.domain.repository.Exceptions.TransactionException;
import me.BlockDynasty.Economy.domain.repository.IRepository;

import java.util.UUID;

//TODO :CREATE CURRENCY, NOTA IMPORTANTE, VALIDAR QUE SI YA EXISTE UNA CURRENCY POR DEFAULT NO SE PUEDA CREAR, Y EN EL CASO DE QUERER SETEAR UNA POR DEFECTO DEBEMOS DESASER LA ANTERIOR DEFAULT
public class CreateCurrencyUseCase {
    private final CurrencyCache currencyCache;
    private final IRepository dataStore;
    private final UpdateForwarder updateForwarder;
    private final GetAccountsUseCase getAccountsUseCase;

    public CreateCurrencyUseCase(CurrencyCache currencyCache, GetAccountsUseCase getAccountsUseCase,UpdateForwarder updateForwarder, IRepository dataStore) {
        this.currencyCache = currencyCache;
        this.getAccountsUseCase = getAccountsUseCase;
        this.dataStore = dataStore;
        this.updateForwarder = updateForwarder;
    }

    public void createCurrency(String singular,String plural){
        if (currencyCache.currencyExist(singular) || currencyCache.currencyExist(plural)){
            throw new CurrencyAlreadyExist("Currency already exist");
        }
        Currency currency = new Currency(UUID.randomUUID(), singular, plural);
        currency.setExchangeRate(1.0);
        if(currencyCache.getCurrencies().isEmpty()) {  //setear por defecto si es la unica moneda en el sistema
            currency.setDefaultCurrency(true);
        }
        try {
            dataStore.saveCurrency(currency);
            currencyCache.add(currency);//cache
            getAccountsUseCase.updateAccountsCache();
            if (updateForwarder != null){
                updateForwarder.sendUpdateMessage("currency", currency.getUuid().toString());
            }
        }catch (TransactionException e){
            throw new TransactionException("Error creating currency");
        }
    }
}
