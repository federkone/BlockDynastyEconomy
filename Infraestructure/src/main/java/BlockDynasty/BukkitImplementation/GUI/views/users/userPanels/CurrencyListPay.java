package BlockDynasty.BukkitImplementation.GUI.views.users.userPanels;

import BlockDynasty.BukkitImplementation.GUI.components.AbstractGUI;
import BlockDynasty.BukkitImplementation.GUI.services.GUIService;
import BlockDynasty.BukkitImplementation.GUI.views.admins.adminPanels.AbstractCurrenciesList;
import BlockDynasty.BukkitImplementation.config.file.MessageService;
import BlockDynasty.Economy.aplication.useCase.currency.GetCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.transaction.PayUseCase;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.result.Result;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

public class CurrencyListPay  extends AbstractCurrenciesList {
    private final PayUseCase payUseCase;
    private final Player targetPlayer;
    private final JavaPlugin plugin;
    private final MessageService messageService;

    public CurrencyListPay(JavaPlugin plugin, GUIService guiService, Player player, Player targetPlayer, GetCurrencyUseCase getCurrencyUseCase, PayUseCase payUseCase, MessageService messageService,AbstractGUI parentGUI) {
        super(guiService, player, getCurrencyUseCase, parentGUI);
        this.payUseCase = payUseCase;
        this.targetPlayer = targetPlayer;
        this.messageService = messageService;
        this.plugin = plugin;
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
                        Result<Void> result = payUseCase.execute(sender.getUniqueId(), targetPlayer.getUniqueId(), currency.getSingular(), amount);

                        if (result.isSuccess()) {
                            sender.sendMessage(messageService.getSuccessMessage(sender.getName(), targetPlayer.getName(), currency.getSingular(), amount));
                            targetPlayer.sendMessage(messageService.getReceivedMessage(sender.getName(), currency.getSingular(), amount));

                            return List.of(AnvilGUI.ResponseAction.close());
                        } else {
                            return List.of(AnvilGUI.ResponseAction.replaceInputText(
                                    "§c" + result.getErrorMessage()));
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
