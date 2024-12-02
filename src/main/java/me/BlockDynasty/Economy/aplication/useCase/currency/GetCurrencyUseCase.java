package me.BlockDynasty.Economy.aplication.useCase.currency;

import me.BlockDynasty.Economy.domain.currency.Currency;
import me.BlockDynasty.Economy.domain.currency.CurrencyManager;
import me.BlockDynasty.Economy.domain.currency.Exceptions.CurrencyNotFoundException;

public class GetCurrencyUseCase {
    private final CurrencyManager currencyManager;

    public GetCurrencyUseCase(CurrencyManager currencyManager) {
        this.currencyManager = currencyManager;
    }

    public Currency getCurrency(String name) {
        Currency currency = currencyManager.getCurrency(name);

        if (currency == null) {
            throw new CurrencyNotFoundException("Currency not found");
        }

        return currency;
    }

    public Currency getDefaultCurrency() {
        return currencyManager.getDefaultCurrency();
    }

}
