package BlockDynasty.Economy.aplication.useCase.currency;

import BlockDynasty.Economy.domain.result.ErrorCode;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;
import BlockDynasty.Economy.domain.services.ICurrencyService;

import java.util.List;

public class SearchCurrencyUseCase {
    private final ICurrencyService currencyService;
    private final IRepository datastore;

    public SearchCurrencyUseCase(ICurrencyService currencyService, IRepository datastore) {
        this.currencyService = currencyService;
        this.datastore = datastore;
    }

    public Result<Currency> getCurrency(String name) {
        Currency currency = currencyService.getCurrency(name);
        if (currency == null) {
            Result<Currency> result = datastore.loadCurrencyByName(name);
            if(result.isSuccess()){
                currency = result.getValue();
            }else {
                return Result.failure("Currency not found", ErrorCode.CURRENCY_NOT_FOUND);
            }
        }
        return Result.success(currency);
    }

    public Result<Currency>  getDefaultCurrency() {
        Currency defaultCurrency = currencyService.getDefaultCurrency();
        if(defaultCurrency == null){
            Result<Currency> result = datastore.loadDefaultCurrency();
            if(result.isSuccess()){
                defaultCurrency = result.getValue();
            }else {
                return Result.failure("Currency not found", ErrorCode.CURRENCY_NOT_FOUND);
            }
        }
        return Result.success(defaultCurrency);

    }

    public List<Currency> getCurrencies(){
        return currencyService.getCurrencies();
    }
}
