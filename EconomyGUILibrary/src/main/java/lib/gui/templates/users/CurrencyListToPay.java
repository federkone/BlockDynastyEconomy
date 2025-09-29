package lib.gui.templates.users;

import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.transaction.PayUseCase;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.result.Result;
import lib.gui.abstractions.IGUI;
import lib.gui.abstractions.IEntityGUI;
import lib.gui.abstractions.ITextInput;
import lib.gui.abstractions.Materials;
import lib.gui.templates.abstractions.CurrenciesList;
import lib.util.colors.ChatColor;
import lib.util.colors.Colors;

import java.math.BigDecimal;
import java.util.UUID;

public class CurrencyListToPay extends CurrenciesList {
    private final PayUseCase payUseCase;
    private final BlockDynasty.Economy.domain.entities.account.Player targetPlayer;

    public CurrencyListToPay(IEntityGUI player, BlockDynasty.Economy.domain.entities.account.Player targetPlayer,
                             SearchCurrencyUseCase searchCurrencyUseCase, PayUseCase payUseCase, IGUI parentGUI, ITextInput textInput) {
        super(player, searchCurrencyUseCase, parentGUI,textInput);
        this.payUseCase = payUseCase;
        this.targetPlayer = targetPlayer;
    }

    @Override
    public String execute(IEntityGUI sender, Currency currency, BigDecimal amount){
        Result<Void> result = payUseCase.execute(sender.getUniqueId(), targetPlayer.getUuid(), currency.getSingular(), amount);
        if (!result.isSuccess()) {
            //messageService.sendErrorMessage(result.getErrorCode(),sender,currency.getSingular());
            return result.getErrorMessage();
        }else{
            return "Transaction Successful!";
        }
    }

    @Override
    public void addCustomButtons() {
        setItem(4, createItem(Materials.PAPER, ChatColor.stringValueOf(Colors.GREEN)+"Select Currency to Pay",
                        ChatColor.stringValueOf(Colors.WHITE)+"Click to select the currency you want to pay", ChatColor.stringValueOf(Colors.WHITE)+"And before that, the amount"),
                null);

    }
}