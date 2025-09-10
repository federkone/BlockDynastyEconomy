package lib.gui.templates.administrators.subMenus.accounts;

import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.transaction.WithdrawUseCase;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.result.Result;
import lib.commands.abstractions.Source;
import lib.gui.abstractions.IGUI;
import lib.gui.abstractions.IPlayer;
import lib.gui.abstractions.ITextInput;
import lib.gui.templates.abstractions.ChatColor;
import lib.gui.templates.abstractions.CurrenciesList;

import java.math.BigDecimal;
import java.util.UUID;

public class CurrencyListToWithdraw extends CurrenciesList {
    private final WithdrawUseCase withdrawUseCase;
    private final BlockDynasty.Economy.domain.entities.account.Player targetPlayer;

    public CurrencyListToWithdraw(IPlayer player, BlockDynasty.Economy.domain.entities.account.Player targetPlayer,
                                  SearchCurrencyUseCase searchCurrencyUseCase, WithdrawUseCase withdrawUseCase,
                                  IGUI parentGUI, ITextInput textInput) {
        super( player, searchCurrencyUseCase, parentGUI,textInput);
        this.targetPlayer = targetPlayer;
        this.withdrawUseCase = withdrawUseCase;
        //this.messageService = BlockDynastyEconomy.getInstance().getMessageService();
    }

    @Override
    public String execute(IPlayer sender, Currency currency, BigDecimal amount) {
        Result<Void> result = withdrawUseCase.execute(UUID.fromString(targetPlayer.getUuid()), currency.getSingular(), amount);
        if (result.isSuccess()) {
            Source p = platformAdapter.getPlayer(this.targetPlayer.getNickname());
            if (p != null) {
                p.sendMessage("&7You have withdrawn " + ChatColor.stringValueOf(currency.getColor()) + currency.format(amount) + "&7 from your account.");
            }
            sender.sendMessage("&7Success withdraw " +ChatColor.stringValueOf(currency.getColor()) + currency.format(amount) + "&7 from " + targetPlayer.getNickname() + "'s account.");
            this.openParent();
            return null;
        } else {
            return result.getErrorMessage();
        }
    }
}