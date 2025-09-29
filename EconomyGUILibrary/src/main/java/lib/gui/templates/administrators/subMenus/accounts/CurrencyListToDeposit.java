package lib.gui.templates.administrators.subMenus.accounts;

import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.transaction.DepositUseCase;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.events.Context;
import BlockDynasty.Economy.domain.result.Result;
import lib.gui.abstractions.IGUI;
import lib.gui.abstractions.IEntityGUI;
import lib.gui.abstractions.ITextInput;
import lib.gui.templates.abstractions.CurrenciesList;
import lib.util.colors.ChatColor;

import java.math.BigDecimal;

public class CurrencyListToDeposit extends CurrenciesList {
    private final DepositUseCase depositUseCase;
    private final BlockDynasty.Economy.domain.entities.account.Player targetPlayer;


    public CurrencyListToDeposit(IEntityGUI player, BlockDynasty.Economy.domain.entities.account.Player targetPlayer,
                                 SearchCurrencyUseCase searchCurrencyUseCase, DepositUseCase depositUseCase, IGUI parentGUI, ITextInput textInput) {
        super(player, searchCurrencyUseCase, parentGUI,textInput);
        this.targetPlayer = targetPlayer;
        this.depositUseCase = depositUseCase;
        //this.messageService = BlockDynastyEconomy.getInstance().getMessageService();
    }

    @Override
    public String execute(IEntityGUI sender, Currency currency, BigDecimal amount){
        Result<Void> result = depositUseCase.execute(targetPlayer.getUuid(),currency.getSingular(), amount, Context.COMMAND);
        if (result.isSuccess()) {
            sender.sendMessage("Deposited "+ ChatColor.stringValueOf(currency.getColor()) + currency.format(amount) + " to " + targetPlayer.getNickname() + "'s account.");
            this.openParent();
            return null;
        } else {
            return result.getErrorMessage();
        }
    }
}