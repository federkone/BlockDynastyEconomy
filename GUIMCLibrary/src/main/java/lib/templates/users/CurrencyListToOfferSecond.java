package lib.templates.users;

import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.offer.CreateOfferUseCase;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.result.Result;
import lib.components.IPlayer;
import lib.components.ITextInput;
import lib.components.Materials;
import lib.templates.abstractions.CurrenciesList;
import net.kyori.adventure.text.Component;

import java.util.UUID;

public class CurrencyListToOfferSecond extends CurrenciesList {
    private final BlockDynasty.Economy.domain.entities.account.Player target;
    private final BlockDynasty.Economy.aplication.useCase.offer.CreateOfferUseCase createOfferUseCase;
    private final CurrencyListToOfferFirst parentGUI;

    public CurrencyListToOfferSecond(IPlayer player, BlockDynasty.Economy.domain.entities.account.Player target, SearchCurrencyUseCase searchCurrencyUseCase,
                                     CreateOfferUseCase createOfferUseCase, CurrencyListToOfferFirst parentGUI, ITextInput textInput) {
        super(player, searchCurrencyUseCase, parentGUI,parentGUI.getCurrency(), textInput);
        this.createOfferUseCase = createOfferUseCase;
        this.target = target;
        this.parentGUI = parentGUI;
    }

    @Override
    protected String  execute(IPlayer sender, Currency currency, java.math.BigDecimal amount){
        Result<Void> result= createOfferUseCase.execute(
                sender.getUniqueId(),
                UUID.fromString(target.getUuid()),
                parentGUI.getCurrency().getPlural(),
                parentGUI.getAmount(),
                currency.getSingular(),
                amount
        );

        if (!result.isSuccess()){
            sender.sendMessage("§cError: " + result.getErrorMessage()+ "."+result.getErrorCode());
        }else{
            //GUIFactory.seeMyOffersPanel(sender).open();
        }

        return null;
    }

    @Override
    public void addCustomButtons() {
        setItem(4, createItem(Materials.PAPER, "§aSelect Currency to Receive",
                        "§7Click to select the currency you want to receive", "And before that, the amount"
                ),
                null);

    }
}
