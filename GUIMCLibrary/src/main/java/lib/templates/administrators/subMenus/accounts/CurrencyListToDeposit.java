package lib.templates.administrators.subMenus.accounts;

import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.transaction.DepositUseCase;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.result.Result;
import lib.components.IGUI;
import lib.components.IPlayer;
import lib.components.ITextInput;
import lib.templates.abstractions.CurrenciesList;

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
            //Player p = Bukkit.getPlayer(targetPlayer.getNickname());
            //if (p != null) {
             //   p.sendMessage(messageService.getReceivedMessage(sender.getName(), currency.getSingular(), amount));
            //}
            //sender.sendMessage(messageService.getDepositMessage(sender.getName(), currency.getSingular(), amount));
            this.openParent();
            return null;
        } else {
            return result.getErrorMessage();
        }
    }
}