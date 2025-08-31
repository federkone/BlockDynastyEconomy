package lib.templates.users;

import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.transaction.PayUseCase;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.result.Result;
import lib.components.IGUI;
import lib.components.IPlayer;
import lib.components.ITextInput;
import lib.components.Materials;
import lib.templates.abstractions.CurrenciesList;

import java.math.BigDecimal;
import java.util.UUID;

public class CurrencyListToPay extends CurrenciesList {
    private final PayUseCase payUseCase;
    private final BlockDynasty.Economy.domain.entities.account.Player targetPlayer;

    public CurrencyListToPay(IPlayer player, BlockDynasty.Economy.domain.entities.account.Player targetPlayer,
                             SearchCurrencyUseCase searchCurrencyUseCase, PayUseCase payUseCase, IGUI parentGUI, ITextInput textInput) {
        super(player, searchCurrencyUseCase, parentGUI,textInput);
        this.payUseCase = payUseCase;
        this.targetPlayer = targetPlayer;
    }

    @Override
    public String execute(IPlayer sender, Currency currency, BigDecimal amount){
        Result<Void> result = payUseCase.execute(sender.getUniqueId(), UUID.fromString(targetPlayer.getUuid()), currency.getSingular(), amount);
        if (!result.isSuccess()) {
            //messageService.sendErrorMessage(result.getErrorCode(),sender,currency.getSingular());
            return null;
        }else{
            return "Successful!";
        }
    }

    @Override
    public void addCustomButtons() {
        setItem(4, createItem(Materials.PAPER, "§aSelect Currency to Pay",
                        "§7Click to select the currency you want to pay", "§7And before that, the amount"),
                null);

    }
}