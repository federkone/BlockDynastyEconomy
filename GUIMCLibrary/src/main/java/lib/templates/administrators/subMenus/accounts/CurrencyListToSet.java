package lib.templates.administrators.subMenus.accounts;

import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.transaction.SetBalanceUseCase;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.result.Result;
import lib.components.IGUI;
import lib.components.IPlayer;
import lib.components.ITextInput;
import lib.templates.abstractions.CurrenciesList;

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
           // Player p =  Bukkit.getPlayer(targetPlayer.getNickname());
           // if (p != null) {
           //     p.sendMessage(messageService.getSetSuccess(currency.getSingular(), amount));
            //}
            sender.sendMessage("success");
            this.openParent();
            return null;
        } else {
            return result.getErrorMessage();
        }
    }
}
