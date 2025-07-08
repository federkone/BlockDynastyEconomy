package me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.GUI.views.TransactionsView;

import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.BlockDynastyEconomy;
import me.BlockDynasty.Economy.domain.result.Result;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.GUI.components.AbstractGUI;
import me.BlockDynasty.Economy.aplication.useCase.transaction.GetBalanceUseCase;
import me.BlockDynasty.Economy.domain.balance.Balance;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class BalanceGUI extends AbstractGUI {
    private final GetBalanceUseCase getBalanceUseCase;
    private final Player player;
    private final BlockDynastyEconomy plugin;

    public BalanceGUI(BlockDynastyEconomy plugin, Player player) {
        super("Balance de cuenta", 3);
        this.getBalanceUseCase = plugin.getUsesCase().getGetBalanceUseCase();
        this.player = player;
        this.plugin = plugin;

        setupGUI();
    }

    private void setupGUI() {
        Result<Balance> result = getBalanceUseCase.getBalance(player.getUniqueId());
        double balance = result.getValue().getBalance().doubleValue();

        setItem(13, createItem(Material.GOLD_INGOT, "§6Tu balance",
                "§eBalance: §f" + balance), null);

       /* setItem(11, createItem(Material.EMERALD, "§aDepositar", "§7Click para depositar"), unused -> {
            player.closeInventory();
            openAmountInputGUI(
                    player,
                    "Depositar monto",
                    amount -> usesCase.getDepositUseCase().execute(player.getUniqueId(), amount),
                    "§aHas depositado: §f%amount%"
            );
        });

        setItem(15, createItem(Material.REDSTONE, "§cExtraer", "§7Click para extraer"), unused -> {
            player.closeInventory();
            openAmountInputGUI(
                    player,
                    "Extraer monto",
                    amount -> usesCase.getWithdrawUseCase().execute(player.getUniqueId(), amount),
                    "§cHas extraído: §f%amount%"
            );
        });*/
    }

    // Método para abrir el AnvilGUI y manejar la entrada de montos
    private void openAmountInputGUI(Player player, String title, Function<BigDecimal, Result<Void>> operation, String successMessage) {
        new AnvilGUI.Builder()
                .onClick((slot, stateSnapshot) -> {
                    if (slot != AnvilGUI.Slot.OUTPUT) {
                        return Collections.emptyList();
                    }
                    try {
                        BigDecimal amount = new BigDecimal(stateSnapshot.getText());
                        Result<Void> result = operation.apply(amount);

                        if (result.isSuccess()) {
                            player.sendMessage(successMessage.replace("%amount%", amount.toString()));
                            return List.of(AnvilGUI.ResponseAction.close());
                        } else {
                            return List.of( AnvilGUI.ResponseAction.replaceInputText("§c" + result.getErrorMessage()) );
                        }
                    } catch (NumberFormatException e) {
                        return List.of( AnvilGUI.ResponseAction.replaceInputText("§cFormato inválido"));
                    }
                })
                .text("0")
                .title(title)
                .plugin(plugin)
                .open(player);
    }
}