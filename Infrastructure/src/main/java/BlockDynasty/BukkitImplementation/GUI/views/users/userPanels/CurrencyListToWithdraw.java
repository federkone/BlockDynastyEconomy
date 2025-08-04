package BlockDynasty.BukkitImplementation.GUI.views.users.userPanels;

import BlockDynasty.BukkitImplementation.BlockDynastyEconomy;
import BlockDynasty.BukkitImplementation.GUI.components.AbstractGUI;
import BlockDynasty.BukkitImplementation.GUI.components.CurrenciesList;
import BlockDynasty.BukkitImplementation.GUI.components.IGUI;
import BlockDynasty.BukkitImplementation.services.MessageService;
import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.transaction.WithdrawUseCase;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.result.Result;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.math.BigDecimal;

import java.util.UUID;

public class CurrencyListToWithdraw extends CurrenciesList {
    private final WithdrawUseCase withdrawUseCase;
    private final BlockDynasty.Economy.domain.entities.account.Player targetPlayer;
    private final MessageService messageService;

    public CurrencyListToWithdraw( Player player, BlockDynasty.Economy.domain.entities.account.Player targetPlayer,
                                   SearchCurrencyUseCase searchCurrencyUseCase, WithdrawUseCase withdrawUseCase,
                                   IGUI parentGUI) {
        super( player, searchCurrencyUseCase, parentGUI);
        this.targetPlayer = targetPlayer;
        this.withdrawUseCase = withdrawUseCase;
        this.messageService = BlockDynastyEconomy.getInstance().getMessageService();
    }

    @Override
    public String execute(Player sender, Currency currency, BigDecimal amount) {
        Result<Void> result = withdrawUseCase.execute(UUID.fromString(targetPlayer.getUuid()), currency.getSingular(), amount);
        if (result.isSuccess()) {
            Player p = Bukkit.getPlayer(this.targetPlayer.getNickname());
            if (p != null) {
                p.sendMessage(messageService.getWithdrawSuccess(currency.getSingular(), amount));
            }
            sender.sendMessage(messageService.getWithdrawMessage(sender.getName(), currency.getSingular(), amount));
            this.openParent();
            return null;
        } else {
            return result.getErrorMessage();
        }
    }
}