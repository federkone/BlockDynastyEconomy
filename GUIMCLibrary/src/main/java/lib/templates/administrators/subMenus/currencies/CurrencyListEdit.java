package lib.templates.administrators.subMenus.currencies;

import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import lib.GUIFactory;
import lib.components.IGUI;
import lib.components.IPlayer;
import lib.components.ITextInput;
import lib.templates.abstractions.CurrenciesList;

public class CurrencyListEdit extends CurrenciesList {
    private final IPlayer player;
    public CurrencyListEdit(IPlayer player, SearchCurrencyUseCase searchCurrencyUseCase, IGUI abstractGUI, ITextInput textInput) {
        super( player, searchCurrencyUseCase,abstractGUI,textInput);
        this.player = player;
    }

    @Override
    public void functionLeftItemClick(Currency currency) {
        GUIFactory.editCurrencyPanel(player, currency, this).open();
    }

}
