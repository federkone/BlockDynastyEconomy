package me.BlockDynasty.Economy.aplication.useCase.currency;


import me.BlockDynasty.Integrations.bungee.UpdateForwarder;
import me.BlockDynasty.Economy.aplication.useCase.account.GetAccountsUseCase;
import me.BlockDynasty.Economy.domain.currency.Currency;
import me.BlockDynasty.Economy.domain.currency.CurrencyCache;
import me.BlockDynasty.Economy.domain.currency.Exceptions.CurrencyNotFoundException;
import me.BlockDynasty.Economy.domain.repository.Exceptions.TransactionException;
import me.BlockDynasty.Economy.domain.repository.IRepository;

//todo: borrar currency y registro de esta moneda de todos los usuarios que la tengan. tanto en la cache como en la base de datos?
public class DeleteCurrencyUseCase {
    private final CurrencyCache currencyCache;
    private final GetAccountsUseCase getAccountsUseCase;
    private final IRepository dataStore;
    private final UpdateForwarder updateForwarder;

    public DeleteCurrencyUseCase(CurrencyCache currencyCache, GetAccountsUseCase getAccountsUseCase, IRepository dataStore, UpdateForwarder updateForwarder){
        this.currencyCache = currencyCache;
        this.getAccountsUseCase = getAccountsUseCase;
        this.dataStore = dataStore;
        this.updateForwarder = updateForwarder;
    }
    public void deleteCurrency(String currencyName){
        Currency currency = currencyCache.getCurrency(currencyName);
        if (currency == null){
            throw new CurrencyNotFoundException("Currency not found");
        }
        if (currency.isDefaultCurrency()){
            throw new CurrencyNotFoundException("Currency is default");
        }
        try {
            dataStore.deleteCurrency(currency);
            currencyCache.remove(currency);
            getAccountsUseCase.updateAccountsCache();
            if (updateForwarder != null){
                updateForwarder.sendUpdateMessage("currency", currency.getUuid().toString());
            }
        }catch (TransactionException e){
            throw new TransactionException("Error deleting currency");
        }
    }
}
