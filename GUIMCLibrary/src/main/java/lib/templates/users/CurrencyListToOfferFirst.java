package lib.templates.users;

import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.offer.CreateOfferUseCase;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import lib.components.IGUI;
import lib.components.IPlayer;
import lib.components.ITextInput;
import lib.components.Materials;
import lib.templates.abstractions.CurrenciesList;

import java.math.BigDecimal;

public class CurrencyListToOfferFirst extends CurrenciesList {
    private final BlockDynasty.Economy.domain.entities.account.Player target;
    private final SearchCurrencyUseCase searchCurrencyUseCase;
    protected final CreateOfferUseCase createOfferUseCase;
    private Currency currency;
    private BigDecimal amount;
    private final ITextInput textInput;

    public CurrencyListToOfferFirst(IPlayer player, BlockDynasty.Economy.domain.entities.account.Player target,
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
    protected String execute(IPlayer sender, Currency currency, java.math.BigDecimal amount) {
        this.currency = currency;
        this.amount = amount;
        new CurrencyListToOfferSecond(sender, target, searchCurrencyUseCase,createOfferUseCase, this,textInput).open();
        return null;
    }

    @Override
    public void addCustomButtons() {
        setItem(4, createItem(Materials.PAPER, "§aSelect Currency to Offer",
                        "§7Click to select the currency you want to offer", "§7And before that, the amount"),
                null);

    }
}