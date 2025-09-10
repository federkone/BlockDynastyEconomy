package lib.gui.templates.administrators.subMenus.accounts;

import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.transaction.DepositUseCase;
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

public class CurrencyListToDeposit extends CurrenciesList {
    private final DepositUseCase depositUseCase;
    private final BlockDynasty.Economy.domain.entities.account.Player targetPlayer;


    public CurrencyListToDeposit(IPlayer player, BlockDynasty.Economy.domain.entities.account.Player targetPlayer,
                                 SearchCurrencyUseCase searchCurrencyUseCase, DepositUseCase depositUseCase, IGUI parentGUI, ITextInput textInput) {
        super(player, searchCurrencyUseCase, parentGUI,textInput);
        this.targetPlayer = targetPlayer;
        this.depositUseCase = depositUseCase;
        //this.messageService = BlockDynastyEconomy.getInstance().getMessageService();
    }

    @Override
    public String execute(IPlayer sender, Currency currency, BigDecimal amount){
        Result<Void> result = depositUseCase.execute(UUID.fromString(targetPlayer.getUuid()),currency.getSingular(), amount);
        if (result.isSuccess()) {
            Source player = platformAdapter.getPlayer(targetPlayer.getNickname());
            if (player != null) {
                player.sendMessage("&7You have received a deposit of " + ChatColor.stringValueOf(currency.getColor()) + currency.format(amount) + "&7.");
            }
            sender.sendMessage("&7Deposited "+ ChatColor.stringValueOf(currency.getSingular()) + currency.format(amount) + "&7 to " + targetPlayer.getNickname() + "'s account.");
            this.openParent();
            return null;
        } else {
            return result.getErrorMessage();
        }
    }
}