package me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.GUI.views.TransactionsView;

import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.BlockDynastyEconomy;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.GUI.components.AbstractGUI;
import me.BlockDynasty.Economy.aplication.useCase.transaction.PayUseCase;
import me.BlockDynasty.Economy.domain.entities.currency.Currency;
import me.BlockDynasty.Economy.domain.result.Result;
import me.BlockDynasty.Economy.domain.services.ICurrencyService;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


//la ui para pagar, va a mostrar primero: las personas Online indexadas, luego elegir la moneda "monedas disponibles del sistema" indexada, luego abrir AnvilGUI para escribir monto.Fin
public class PayGUI extends AbstractGUI {
    private final BlockDynastyEconomy plugin;
    private final Player sender;
    private final PayUseCase payUseCase;
    private final ICurrencyService currencyService;
    private int currentPage = 0;
    private final int PLAYERS_PER_PAGE = 21;

    public PayGUI(BlockDynastyEconomy plugin, PayUseCase payUseCase,Player sender, ICurrencyService currencyService) {
        super("Seleccionar Jugador", 5);
        this.plugin = plugin;
        this.sender = sender;
        this.payUseCase = payUseCase;
        this.currencyService = currencyService;

        showPlayersPage();
    }

    private void showPlayersPage() {
        // Get online players
        List<Player> onlinePlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
        onlinePlayers.remove(sender); // Remove sender from list

        // Calculate pagination
        int startIndex = currentPage * PLAYERS_PER_PAGE;
        int endIndex = Math.min(startIndex + PLAYERS_PER_PAGE, onlinePlayers.size());

        // Clear GUI
        for (int i = 0; i < getInventory().getSize(); i++) {
            setItem(i, null, null);
        }

        // Add players to GUI
        int slot = 10;
        for (int i = startIndex; i < endIndex; i++) {
            Player target = onlinePlayers.get(i);
            ItemStack playerHead = createPlayerHead(target);

            setItem(slot, playerHead, unused -> {
                sender.closeInventory();
                openCurrencySelectionGUI(target);
            });

            // Adjust slot position
            slot++;
            if (slot % 9 == 8) slot += 2;
        }

        // Navigation buttons
        if (currentPage > 0) {
            setItem(38, createItem(Material.ARROW, "§aPágina Anterior",
                    "§7Click para ver jugadores anteriores"), unused -> {
                currentPage--;
                showPlayersPage();
            });
        }

        if (endIndex < onlinePlayers.size()) {
            setItem(42, createItem(Material.ARROW, "§aPágina Siguiente",
                    "§7Click para ver más jugadores"), unused -> {
                currentPage++;
                showPlayersPage();
            });
        }

        // Cancel button
        setItem(40, createItem(Material.BARRIER, "§cCancelar",
                "§7Click para cancelar"), unused -> sender.closeInventory());
    }

    private ItemStack createPlayerHead(Player player) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.setOwningPlayer(player);
        meta.setDisplayName("§e" + player.getName());
        meta.setLore(Collections.singletonList("§7Click para pagar a este jugador"));
        head.setItemMeta(meta);
        return head;
    }

    private void openCurrencySelectionGUI(Player targetPlayer) {
        CurrencySelectionGUI gui = new CurrencySelectionGUI(plugin, sender, targetPlayer);
        sender.openInventory(gui.getInventory());

        // Register the GUI with the GUIService
        plugin.getGuiManager().registerGUI(sender, gui);
    }

    private void openAmountInputGUI(Player targetPlayer, Currency currency) {
        new AnvilGUI.Builder()
                .onClick((slot, stateSnapshot) -> {
                    if (slot != AnvilGUI.Slot.OUTPUT) {
                        return Collections.emptyList();
                    }
                    try {
                        BigDecimal amount = new BigDecimal(stateSnapshot.getText());
                        Result<Void> result = payUseCase.execute(sender.getUniqueId(), targetPlayer.getUniqueId(), currency.getSingular(), amount);

                        if (result.isSuccess()) {
                            sender.sendMessage("§aHas pagado §f" + amount + " " +
                                    currency.getSingular() + " §aa §f" + targetPlayer.getName());
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

    // Inner class for currency selection
    private class CurrencySelectionGUI extends AbstractGUI {
        private final Player targetPlayer;

        public CurrencySelectionGUI(BlockDynastyEconomy plugin, Player sender, Player targetPlayer) {
            super("Seleccionar Moneda", 3);
            this.targetPlayer = targetPlayer;

            setupCurrencyGUI();
        }

        private void setupCurrencyGUI() {
            List<Currency> currencies = currencyService.getCurrencies();

            int slot = 10;
            for (Currency currency : currencies) {
                setItem(slot, createItem(Material.GOLD_INGOT, "§6" + currency.getSingular(),
                        "§7Click para seleccionar esta moneda"), unused -> {
                    sender.closeInventory();
                    openAmountInputGUI(targetPlayer, currency);
                });

                slot++;
                if (slot % 9 == 8) slot += 2;
            }

            // Back button
            setItem(22, createItem(Material.ARROW, "§aVolver",
                    "§7Click para volver a selección de jugadores"), unused -> {
                sender.closeInventory();
                // Create a new PayGUI instance and open it
                PayGUI payGUI = new PayGUI(plugin, payUseCase, sender, currencyService);
                sender.openInventory(payGUI.getInventory());

                // Register the new GUI with the GUIService
                plugin.getGuiManager().registerGUI(sender, payGUI);
            });
        }
    }
}