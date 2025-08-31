package lib.templates.administrators.subMenus.accounts;

import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.transaction.WithdrawUseCase;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.result.Result;
import lib.components.IGUI;
import lib.components.IPlayer;
import lib.components.ITextInput;
import lib.templates.abstractions.CurrenciesList;

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
            //Player p = Bukkit.getPlayer(this.targetPlayer.getNickname());
            //if (p != null) {
             //   p.sendMessage(messageService.getWithdrawSuccess(currency.getSingular(), amount));
            //}
            //sender.sendMessage(messageService.getWithdrawMessage(sender.getName(), currency.getSingular(), amount));
            this.openParent();
            return null;
        } else {
            return result.getErrorMessage();
        }
    }
}