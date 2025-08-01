package BlockDynasty.BukkitImplementation.GUI.views.users.userPanels;

import BlockDynasty.BukkitImplementation.GUI.components.AbstractGUI;
import BlockDynasty.BukkitImplementation.GUI.components.CurrenciesList;
import BlockDynasty.BukkitImplementation.GUI.services.GUIService;
import BlockDynasty.BukkitImplementation.services.MessageService;
import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.transaction.SetBalanceUseCase;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.result.Result;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.UUID;

public class CurrencyListToSet extends CurrenciesList {
    SetBalanceUseCase setBalanceUseCase;
    private final BlockDynasty.Economy.domain.entities.account.Player targetPlayer;

    public CurrencyListToSet(GUIService guiService, Player player, BlockDynasty.Economy.domain.entities.account.Player targetPlayer,
                             SearchCurrencyUseCase searchCurrencyUseCase, SetBalanceUseCase setBalanceUseCase, AbstractGUI parentGUI) {
        super(guiService, player, searchCurrencyUseCase, parentGUI);
        this.targetPlayer = targetPlayer;
        this.setBalanceUseCase = setBalanceUseCase;

    }

    @Override
    public String execute(Player sender, Currency currency, BigDecimal amount) {
        Result<Void> result = setBalanceUseCase.execute(UUID.fromString(targetPlayer.getUuid()), currency.getSingular(), amount);
        if (result.isSuccess()) {
            return "Balance set successfully";
        } else {
           return result.getErrorMessage();
        }
    }
}
