package lib.gui.templates.administrators.subMenus.accounts;

import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.transaction.SetBalanceUseCase;
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

public class CurrencyListToSet extends CurrenciesList {
    private final SetBalanceUseCase setBalanceUseCase;

    private final BlockDynasty.Economy.domain.entities.account.Player targetPlayer;

    public CurrencyListToSet(IPlayer player, BlockDynasty.Economy.domain.entities.account.Player targetPlayer,
                             SearchCurrencyUseCase searchCurrencyUseCase, SetBalanceUseCase setBalanceUseCase, IGUI parentGUI, ITextInput textInput) {
        super( player, searchCurrencyUseCase, parentGUI, textInput);
        this.targetPlayer = targetPlayer;
        this.setBalanceUseCase = setBalanceUseCase;
        //this.messageService = BlockDynastyEconomy.getInstance().getMessageService();
    }

    @Override
    public String execute(IPlayer sender, Currency currency, BigDecimal amount) {
        Result<Void> result = setBalanceUseCase.execute(UUID.fromString(targetPlayer.getUuid()), currency.getSingular(), amount);
        if (result.isSuccess()) {
            Source p = platformAdapter.getPlayer(targetPlayer.getNickname());
            if (p != null) {
               p.sendMessage("&7Your account balance has been set to " + ChatColor.stringValueOf(currency.getColor()) + currency.format(amount) + "&7.");
            }
            sender.sendMessage("Set success");
            this.openParent();
            return null;
        } else {
            return result.getErrorMessage();
        }
    }
}
