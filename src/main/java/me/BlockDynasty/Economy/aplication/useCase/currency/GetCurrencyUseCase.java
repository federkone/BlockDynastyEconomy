package me.BlockDynasty.Economy.aplication.useCase.currency;

import me.BlockDynasty.Economy.aplication.result.ErrorCode;
import me.BlockDynasty.Economy.aplication.result.Result;
import me.BlockDynasty.Economy.domain.currency.Currency;
import me.BlockDynasty.Economy.domain.currency.CurrencyCache;
import me.BlockDynasty.Economy.domain.repository.Criteria.Criteria;
import me.BlockDynasty.Economy.domain.repository.IRepository;

import java.util.List;

public class GetCurrencyUseCase {
    private final CurrencyCache currencyCache;
    private final IRepository datastore;

    public GetCurrencyUseCase(CurrencyCache currencyCache, IRepository datastore) {
        this.currencyCache = currencyCache;
        this.datastore = datastore;
    }

    public Result<Currency> getCurrency(String name) {
        Currency currency = currencyCache.getCurrency(name);
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
        Currency defaultCurrency = currencyCache.getDefaultCurrency();
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
        return currencyCache.getCurrencies();
    }


}
