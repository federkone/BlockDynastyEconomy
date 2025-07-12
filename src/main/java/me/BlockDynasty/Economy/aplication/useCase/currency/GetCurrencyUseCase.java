package me.BlockDynasty.Economy.aplication.useCase.currency;

import me.BlockDynasty.Economy.domain.result.ErrorCode;
import me.BlockDynasty.Economy.domain.result.Result;
import me.BlockDynasty.Economy.domain.entities.currency.Currency;
import me.BlockDynasty.Economy.Infrastructure.repository.Criteria.Criteria;
import me.BlockDynasty.Economy.domain.persistence.entities.IRepository;
import me.BlockDynasty.Economy.domain.services.ICurrencyService;

import java.util.List;

public class GetCurrencyUseCase {
    private final ICurrencyService currencyService;
    private final IRepository datastore;

    public GetCurrencyUseCase(ICurrencyService currencyService, IRepository datastore) {
        this.currencyService = currencyService;
        this.datastore = datastore;
    }

    public Result<Currency> getCurrency(String name) {
        Currency currency = currencyService.getCurrency(name);
        if (currency == null) {
            List<Currency> currencies = datastore.loadCurrencies(Criteria.create().filter("singular", name).filter("plural",name).limit(1));
            if(!currencies.isEmpty()){
                currency = currencies.get(0);
            }else {
                return Result.failure("Currency not found", ErrorCode.CURRENCY_NOT_FOUND);
            }
        }
        return Result.success(currency);
    }

    public Result<Currency>  getDefaultCurrency() {
        Currency defaultCurrency = currencyService.getDefaultCurrency();
        if(defaultCurrency == null){
            System.out.println("la moneda por defecto no esta en cache");
            List<Currency> currencies = datastore.loadCurrencies(Criteria.create().filter("defaultCurrency", true).limit(1));
            if(!currencies.isEmpty()){
                defaultCurrency = currencies.get(0);
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
