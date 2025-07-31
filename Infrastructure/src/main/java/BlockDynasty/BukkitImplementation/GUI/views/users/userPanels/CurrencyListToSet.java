package BlockDynasty.BukkitImplementation.GUI.views.users.userPanels;

import BlockDynasty.BukkitImplementation.GUI.components.AbstractGUI;
import BlockDynasty.BukkitImplementation.GUI.components.CurrenciesList;
import BlockDynasty.BukkitImplementation.GUI.services.GUIService;
import BlockDynasty.BukkitImplementation.services.MessageService;
import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.transaction.DepositUseCase;
import BlockDynasty.Economy.aplication.useCase.transaction.SetBalanceUseCase;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.result.Result;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class CurrencyListToSet extends CurrenciesList {
    SetBalanceUseCase setBalanceUseCase;
    private final BlockDynasty.Economy.domain.entities.account.Player targetPlayer;
    private final JavaPlugin plugin;

    public CurrencyListToSet(JavaPlugin plugin, GUIService guiService, Player player, BlockDynasty.Economy.domain.entities.account.Player targetPlayer,
                             SearchCurrencyUseCase searchCurrencyUseCase, SetBalanceUseCase setBalanceUseCase, AbstractGUI parentGUI) {
        super(guiService, player, searchCurrencyUseCase, parentGUI);
        this.targetPlayer = targetPlayer;
        this.plugin = plugin;
        this.setBalanceUseCase = setBalanceUseCase;

    }

    @Override
    public void openSubMenu(Currency currency, Player sender) {
        new AnvilGUI.Builder()
                .onClick((slot, stateSnapshot) -> {
                    if (slot != AnvilGUI.Slot.OUTPUT) {
                        return Collections.emptyList();
                    }
                    try {
                        BigDecimal amount = new BigDecimal(stateSnapshot.getText());
                        Result<Void> result = setBalanceUseCase.execute(UUID.fromString(targetPlayer.getUuid()),currency.getSingular(), amount);

                        if (result.isSuccess()) {
                            sender.sendMessage("§aSaldo actualizado exitosamente");
                            return List.of(AnvilGUI.ResponseAction.close());
                        } else {
                            return List.of(AnvilGUI.ResponseAction.replaceInputText("§c" + result.getErrorMessage()));
                        }
                    } catch (NumberFormatException e) {
                        return List.of(AnvilGUI.ResponseAction.replaceInputText("§cFormato inválido"));
                    }
                })
                .text("0")
                .title("Ingresar Monto")
                .plugin(plugin)
                .open(sender);

    }
}
