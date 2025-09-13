package lib.gui.templates.users;

import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.transaction.ExchangeUseCase;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.result.Result;
import lib.gui.abstractions.IEntityGUI;
import lib.gui.abstractions.ITextInput;
import lib.gui.abstractions.Materials;
import lib.gui.templates.abstractions.CurrenciesList;

import java.math.BigDecimal;

public class CurrencyListToExchangeSecond extends CurrenciesList {
    private final Currency currencyFrom;
    private final ExchangeUseCase exchangeUseCase;

    public CurrencyListToExchangeSecond(IEntityGUI player, SearchCurrencyUseCase searchCurrencyUseCase, ExchangeUseCase exchangeUseCase,
                                        Currency currencyFrom, CurrencyListToExchangeFirst parentGUI, ITextInput textInput) {
        super(player, searchCurrencyUseCase, parentGUI,currencyFrom, textInput);
        this.currencyFrom = currencyFrom;
        this.exchangeUseCase = exchangeUseCase;
    }

    @Override
    protected String execute(IEntityGUI sender, Currency currencyTo, BigDecimal amountTo){
        Result<BigDecimal> result= exchangeUseCase.execute(sender.getUniqueId(),currencyFrom.getSingular(),currencyTo.getSingular(),null, amountTo);
        if(result.isSuccess()){
            return "success";
        }else {
            return result.getErrorMessage();
        }
    }

    @Override
    public void addCustomButtons() {
        setItem(4, createItem(Materials.PAPER, "§aSelect Currency you want to receive",
                        "§7Click to select the currency you want to receive", "§7And before that, the amount"),
                null);

    }
}