package BlockDynasty.BukkitImplementation.GUI.views.users.userPanels;

import BlockDynasty.Economy.domain.entities.balance.Money;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.BukkitImplementation.GUI.components.AbstractGUI;
import BlockDynasty.Economy.aplication.useCase.balance.GetBalanceUseCase;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import org.bukkit.plugin.java.JavaPlugin;

import java.math.BigDecimal;
import java.util.Collections;

import java.util.List;
import java.util.function.Function;

public class BalanceGUI extends AbstractGUI {
    private final GetBalanceUseCase getBalanceUseCase;
    private final Player player;
    private final JavaPlugin plugin;

    //CONSULTA SALDO
    public BalanceGUI(JavaPlugin plugin, Player player,GetBalanceUseCase getBalanceUseCase) {
        super("Balance de cuenta", 3);
        this.getBalanceUseCase = getBalanceUseCase;
        this.player = player;
        this.plugin = plugin;

        setupGUI();
    }

    private void setupGUI() {
        Result<List<Money>> result = getBalanceUseCase.getBalances(player.getUniqueId());

        if (result.isSuccess() && result.getValue() != null) {
            List<Money> monies = result.getValue();

            // Add title item
            setItem(4, createItem(Material.BOOK, "§6Balance de tu cuenta",
                    "§7Tus saldos disponibles"), null);

            // Display each balance in separate slots
            int slot = 10;
            for (Money money : monies) {
                String currencyName = money.getCurrency().getSingular();
                BigDecimal amount = money.getAmount();
                Currency currency = money.getCurrency();

                setItem(slot, createItem(Material.GOLD_INGOT,
                        "§6" + currencyName,
                        "§eBalance: §f" + ChatColor.valueOf(currency.getColor())+  currency.format(amount)), null);

                // Adjust slot position
                slot++;
                if (slot % 9 == 8) slot += 2;
            }

            // Add a close button
            setItem(22, createItem(Material.BARRIER, "§cCerrar",
                    "§7Click para cerrar"), unused -> player.closeInventory());
        } else {
            // Show error message if balances couldn't be retrieved
            setItem(13, createItem(Material.BARRIER, "§cError",
                    "§7No se pudieron obtener los balances"), null);
        }
    }


    private void setupGUItest() {

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