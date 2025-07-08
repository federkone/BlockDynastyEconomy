package me.BlockDynasty.Economy.aplication.useCase.currency;


import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.Integrations.bungee.Courier;
import me.BlockDynasty.Economy.Infrastructure.services.CurrencyService;
import me.BlockDynasty.Economy.aplication.useCase.account.GetAccountsUseCase;
import me.BlockDynasty.Economy.domain.currency.Currency;
import me.BlockDynasty.Economy.domain.currency.Exceptions.CurrencyAlreadyExist;
import me.BlockDynasty.Economy.Infrastructure.repository.Exceptions.TransactionException;
import me.BlockDynasty.Economy.domain.persistence.entities.IRepository;
import me.BlockDynasty.Economy.domain.services.ICurrencyService;

import java.util.UUID;

//TODO :CREATE CURRENCY, NOTA IMPORTANTE, VALIDAR QUE SI YA EXISTE UNA CURRENCY POR DEFAULT NO SE PUEDA CREAR, Y EN EL CASO DE QUERER SETEAR UNA POR DEFECTO DEBEMOS DESASER LA ANTERIOR DEFAULT
public class CreateCurrencyUseCase {
    private final ICurrencyService currencyService;
    private final IRepository dataStore;
    private final Courier updateForwarder;
    private final GetAccountsUseCase getAccountsUseCase;

    public CreateCurrencyUseCase(ICurrencyService currencyService, GetAccountsUseCase getAccountsUseCase, Courier updateForwarder, IRepository dataStore) {
        this.currencyService = currencyService;
        this.getAccountsUseCase = getAccountsUseCase;
        this.dataStore = dataStore;
        this.updateForwarder = updateForwarder;
    }

    public void createCurrency(String singular,String plural){
        if (currencyService.currencyExist(singular) || currencyService.currencyExist(plural)){
            throw new CurrencyAlreadyExist("Currency already exist");
        }
        Currency currency = new Currency(UUID.randomUUID(), singular, plural);
        currency.setExchangeRate(1.0);
        if(currencyService.getCurrencies().isEmpty()) {  //setear por defecto si es la unica moneda en el sistema
            currency.setDefaultCurrency(true);
        }
        try {
            dataStore.saveCurrency(currency);
            currencyService.add(currency);//cache
            getAccountsUseCase.updateAccountsCache();
            if (updateForwarder != null){
                updateForwarder.sendUpdateMessage("currency", currency.getUuid().toString());
            }
        }catch (TransactionException e){
            throw new TransactionException("Error creating currency");
        }
    }
}
