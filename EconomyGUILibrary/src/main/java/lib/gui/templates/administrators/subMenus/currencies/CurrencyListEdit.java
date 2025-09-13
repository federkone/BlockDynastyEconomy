package lib.gui.templates.administrators.subMenus.currencies;

import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import lib.gui.GUIFactory;
import lib.gui.abstractions.IGUI;
import lib.gui.abstractions.IEntityGUI;
import lib.gui.abstractions.ITextInput;
import lib.gui.templates.abstractions.CurrenciesList;

public class CurrencyListEdit extends CurrenciesList {
    private final IEntityGUI player;
    public CurrencyListEdit(IEntityGUI player, SearchCurrencyUseCase searchCurrencyUseCase, IGUI abstractGUI, ITextInput textInput) {
        super( player, searchCurrencyUseCase,abstractGUI,textInput);
        this.player = player;
    }

    @Override
    public void functionLeftItemClick(Currency currency) {
        GUIFactory.editCurrencyPanel(player, currency, this).open();
    }

}
