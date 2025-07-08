package me.BlockDynasty.Economy.aplication.useCase.currency;


import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.Integrations.bungee.Courier;
import me.BlockDynasty.Economy.aplication.useCase.account.GetAccountsUseCase;
import me.BlockDynasty.Economy.domain.currency.Currency;
import me.BlockDynasty.Economy.Infrastructure.services.CurrencyService;
import me.BlockDynasty.Economy.domain.currency.Exceptions.CurrencyNotFoundException;
import me.BlockDynasty.Economy.Infrastructure.repository.Exceptions.TransactionException;
import me.BlockDynasty.Economy.domain.persistence.entities.IRepository;
import me.BlockDynasty.Economy.domain.services.ICurrencyService;

//todo: borrar currency y registro de esta moneda de todos los usuarios que la tengan. tanto en la cache como en la base de datos?
public class DeleteCurrencyUseCase {
    private final ICurrencyService currencyService;
    private final GetAccountsUseCase getAccountsUseCase;
    private final IRepository dataStore;
    private final Courier updateForwarder;

    public DeleteCurrencyUseCase(ICurrencyService currencyService, GetAccountsUseCase getAccountsUseCase, IRepository dataStore, Courier updateForwarder){
        this.currencyService = currencyService;
        this.getAccountsUseCase = getAccountsUseCase;
        this.dataStore = dataStore;
        this.updateForwarder = updateForwarder;
    }
    public void deleteCurrency(String currencyName){
        Currency currency = currencyService.getCurrency(currencyName);
        if (currency == null){
            throw new CurrencyNotFoundException("Currency not found");
        }
        if (currency.isDefaultCurrency()){
            throw new CurrencyNotFoundException("Currency is default");
        }
        try {
            dataStore.deleteCurrency(currency);
            currencyService.remove(currency);
            getAccountsUseCase.updateAccountsCache();
            if (updateForwarder != null){
                updateForwarder.sendUpdateMessage("currency", currency.getUuid().toString());
            }
        }catch (TransactionException e){
            throw new TransactionException("Error deleting currency");
        }
    }
}
