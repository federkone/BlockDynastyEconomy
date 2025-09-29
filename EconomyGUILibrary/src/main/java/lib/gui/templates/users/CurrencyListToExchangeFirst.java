package lib.gui.templates.users;

import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.transaction.ExchangeUseCase;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import lib.gui.abstractions.IGUI;
import lib.gui.abstractions.IEntityGUI;
import lib.gui.abstractions.ITextInput;
import lib.gui.abstractions.Materials;
import lib.gui.templates.abstractions.CurrenciesList;
import lib.util.colors.ChatColor;
import lib.util.colors.Colors;

public class CurrencyListToExchangeFirst extends CurrenciesList {
    private final IEntityGUI player;
    private final SearchCurrencyUseCase searchCurrencyUseCase;
    private final ExchangeUseCase exchangeUseCase;
    private final ITextInput textInput;

    public CurrencyListToExchangeFirst(IEntityGUI player, SearchCurrencyUseCase searchCurrencyUseCase, ExchangeUseCase exchangeUseCase, IGUI parentGUI , ITextInput textInput) {
        super(player, searchCurrencyUseCase, parentGUI,textInput);
        this.player = player;
        this.textInput = textInput;
        this.searchCurrencyUseCase = searchCurrencyUseCase;
        this.exchangeUseCase = exchangeUseCase;
    }

    //en este punto ya selecciono la primer moneda a dar.
    @Override
    protected void functionLeftItemClick(Currency currency) {
        //manejaremos la logica abriendo otro menu de seleccion de moneda, y ahi si, el valor, sobrescribiendo execute() con el caso de uso exchange()
        new CurrencyListToExchangeSecond(player, searchCurrencyUseCase, exchangeUseCase, currency, this,textInput).open();
    }

    @Override
    public void addCustomButtons() {
        setItem(4, createItem(Materials.PAPER, ChatColor.stringValueOf(Colors.GREEN)+"Select Currency you want to give",
                        ChatColor.stringValueOf(Colors.WHITE)+"Click to select the currency you want to give"),
                null);

    }
}