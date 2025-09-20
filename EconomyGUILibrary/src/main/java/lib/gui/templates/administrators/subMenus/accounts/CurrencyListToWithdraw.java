package lib.gui.templates.administrators.subMenus.accounts;

import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.transaction.WithdrawUseCase;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.events.Context;
import BlockDynasty.Economy.domain.result.Result;
import lib.gui.abstractions.IGUI;
import lib.gui.abstractions.IEntityGUI;
import lib.gui.abstractions.ITextInput;
import lib.gui.templates.abstractions.ChatColor;
import lib.gui.templates.abstractions.CurrenciesList;

import java.math.BigDecimal;
import java.util.UUID;

public class CurrencyListToWithdraw extends CurrenciesList {
    private final WithdrawUseCase withdrawUseCase;
    private final BlockDynasty.Economy.domain.entities.account.Player targetPlayer;

    public CurrencyListToWithdraw(IEntityGUI player, BlockDynasty.Economy.domain.entities.account.Player targetPlayer,
                                  SearchCurrencyUseCase searchCurrencyUseCase, WithdrawUseCase withdrawUseCase,
                                  IGUI parentGUI, ITextInput textInput) {
        super( player, searchCurrencyUseCase, parentGUI,textInput);
        this.targetPlayer = targetPlayer;
        this.withdrawUseCase = withdrawUseCase;
        //this.messageService = BlockDynastyEconomy.getInstance().getMessageService();
    }

    @Override
    public String execute(IEntityGUI sender, Currency currency, BigDecimal amount) {
        Result<Void> result = withdrawUseCase.execute(targetPlayer.getUuid(), currency.getSingular(), amount, Context.COMMAND);
        if (result.isSuccess()) {
            sender.sendMessage("&7Success withdraw " +ChatColor.stringValueOf(currency.getColor()) + currency.format(amount) + "&7 from " + targetPlayer.getNickname() + "'s account.");
            this.openParent();
            return null;
        } else {
            return result.getErrorMessage();
        }
    }
}