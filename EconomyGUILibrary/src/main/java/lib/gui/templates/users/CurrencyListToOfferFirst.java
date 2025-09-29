package lib.gui.templates.users;

import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.offer.CreateOfferUseCase;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import lib.gui.abstractions.IGUI;
import lib.gui.abstractions.IEntityGUI;
import lib.gui.abstractions.ITextInput;
import lib.gui.abstractions.Materials;
import lib.gui.templates.abstractions.CurrenciesList;
import lib.util.colors.ChatColor;
import lib.util.colors.Colors;

import java.math.BigDecimal;

public class CurrencyListToOfferFirst extends CurrenciesList {
    private final BlockDynasty.Economy.domain.entities.account.Player target;
    private final SearchCurrencyUseCase searchCurrencyUseCase;
    protected final CreateOfferUseCase createOfferUseCase;
    private Currency currency;
    private BigDecimal amount;
    private final ITextInput textInput;

    public CurrencyListToOfferFirst(IEntityGUI player, BlockDynasty.Economy.domain.entities.account.Player target,
                                    SearchCurrencyUseCase searchCurrencyUseCase, CreateOfferUseCase createOfferUseCase, IGUI parentGUI, ITextInput textInput) {
        super(player, searchCurrencyUseCase, parentGUI, textInput);
        this.target = target;
        this.textInput = textInput;
        this.searchCurrencyUseCase = searchCurrencyUseCase;
        this.createOfferUseCase = createOfferUseCase;
    }

    protected Currency getCurrency() {
        return currency;
    }

    protected BigDecimal getAmount() {
        return amount;
    }

    @Override
    protected String execute(IEntityGUI sender, Currency currency, java.math.BigDecimal amount) {
        this.currency = currency;
        this.amount = amount;
        new CurrencyListToOfferSecond(sender, target, searchCurrencyUseCase,createOfferUseCase, this,textInput).open();
        return null;
    }

    @Override
    public void addCustomButtons() {
        setItem(4, createItem(Materials.PAPER, ChatColor.stringValueOf(Colors.GREEN)+"Select Currency to Offer",
                        ChatColor.stringValueOf(Colors.WHITE)+"Click to select the currency you want to offer", ChatColor.stringValueOf(Colors.WHITE)+"And before that, the amount"),
                null);

    }
}