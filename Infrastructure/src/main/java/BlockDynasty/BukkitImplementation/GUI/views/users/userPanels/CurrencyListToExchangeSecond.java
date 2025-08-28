package BlockDynasty.BukkitImplementation.GUI.views.users.userPanels;

import BlockDynasty.BukkitImplementation.GUI.components.CurrenciesList;
import BlockDynasty.BukkitImplementation.GUI.components.IGUI;
import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.transaction.ExchangeUseCase;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.result.Result;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.Arrays;

public class CurrencyListToExchangeSecond extends CurrenciesList {
    private final Currency currencyFrom;
    private final ExchangeUseCase exchangeUseCase;

    public CurrencyListToExchangeSecond(Player player, SearchCurrencyUseCase searchCurrencyUseCase, ExchangeUseCase exchangeUseCase,
                                        Currency currencyFrom, CurrencyListToExchangeFirst parentGUI) {
        super(player, searchCurrencyUseCase, parentGUI,currencyFrom);
        this.currencyFrom = currencyFrom;
        this.exchangeUseCase = exchangeUseCase;
    }

    @Override
    protected String execute(Player sender,Currency currencyTo, BigDecimal amountTo){
       Result<BigDecimal> result= exchangeUseCase.execute(sender.getUniqueId(),currencyFrom.getSingular(),currencyTo.getSingular(),null, amountTo);
       if(result.isSuccess()){
           return "success";
       }else {
           return result.getErrorMessage();
       }
    }

    @Override
    public void addCustomButtons() {
        setItem(4, createItem(Material.BOOK, "§aSelect Currency you want to receive",
                        Arrays.asList("§7Click to select the currency you want to receive", "§7And before that, the amount")),
                null);

    }
}
