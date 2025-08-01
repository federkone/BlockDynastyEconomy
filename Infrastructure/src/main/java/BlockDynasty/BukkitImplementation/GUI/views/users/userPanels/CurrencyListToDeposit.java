package BlockDynasty.BukkitImplementation.GUI.views.users.userPanels;

import BlockDynasty.BukkitImplementation.GUI.components.AbstractGUI;
import BlockDynasty.BukkitImplementation.GUI.components.CurrenciesList;
import BlockDynasty.BukkitImplementation.GUI.services.GUIService;
import BlockDynasty.BukkitImplementation.services.MessageService;
import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.transaction.DepositUseCase;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.result.Result;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.UUID;

public class CurrencyListToDeposit extends CurrenciesList {
    private final DepositUseCase depositUseCase;
    private final BlockDynasty.Economy.domain.entities.account.Player targetPlayer;
    //private final MessageService messageService;

    public CurrencyListToDeposit(GUIService guiService, Player player,  BlockDynasty.Economy.domain.entities.account.Player targetPlayer,
                                 SearchCurrencyUseCase searchCurrencyUseCase,DepositUseCase depositUseCase,AbstractGUI parentGUI) {
        super(guiService, player, searchCurrencyUseCase, parentGUI);
        this.targetPlayer = targetPlayer;
        this.depositUseCase = depositUseCase;
        //this.messageService = messageService;
    }

    @Override
    public String execute(Player sender,Currency currency, BigDecimal amount){
        Result<Void> result = depositUseCase.execute(UUID.fromString(targetPlayer.getUuid()),currency.getSingular(), amount);
        if (result.isSuccess()) {
            return "deposit successful";
        } else {
            return result.getErrorMessage();
        }
    }
}
