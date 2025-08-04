package BlockDynasty.BukkitImplementation.GUI.views.users.userPanels;

import BlockDynasty.BukkitImplementation.BlockDynastyEconomy;
import BlockDynasty.BukkitImplementation.GUI.components.AbstractGUI;
import BlockDynasty.BukkitImplementation.GUI.components.CurrenciesList;
import BlockDynasty.BukkitImplementation.GUI.components.IGUI;
import BlockDynasty.BukkitImplementation.GUI.services.GUIService;
import BlockDynasty.BukkitImplementation.services.MessageService;
import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.transaction.SetBalanceUseCase;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.result.Result;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public class CurrencyListToSet extends CurrenciesList {
    private final SetBalanceUseCase setBalanceUseCase;
    private final MessageService messageService;
    private final BlockDynasty.Economy.domain.entities.account.Player targetPlayer;

    public CurrencyListToSet( Player player, BlockDynasty.Economy.domain.entities.account.Player targetPlayer,
                             SearchCurrencyUseCase searchCurrencyUseCase, SetBalanceUseCase setBalanceUseCase, IGUI parentGUI) {
        super( player, searchCurrencyUseCase, parentGUI);
        this.targetPlayer = targetPlayer;
        this.setBalanceUseCase = setBalanceUseCase;
        this.messageService = BlockDynastyEconomy.getInstance().getMessageService();
    }

    @Override
    public String execute(Player sender, Currency currency, BigDecimal amount) {
        Result<Void> result = setBalanceUseCase.execute(UUID.fromString(targetPlayer.getUuid()), currency.getSingular(), amount);
        if (result.isSuccess()) {
            Player p =  Bukkit.getPlayer(targetPlayer.getNickname());
            if (p != null) {
                p.sendMessage(messageService.getSetSuccess(currency.getSingular(), amount));
            }
            sender.sendMessage("success");
            this.openParent();
            return null;
        } else {
           return result.getErrorMessage();
        }
    }
}
