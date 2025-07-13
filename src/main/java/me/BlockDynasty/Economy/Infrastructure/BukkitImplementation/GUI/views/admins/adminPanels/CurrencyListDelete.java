package me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.GUI.views.admins.adminPanels;

import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.BlockDynastyEconomy;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.GUI.components.AbstractGUI;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.GUI.views.admins.CurrencyPanelGUI;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.config.file.F;
import me.BlockDynasty.Economy.Infrastructure.repository.Exceptions.TransactionException;
import me.BlockDynasty.Economy.aplication.useCase.currency.DeleteCurrencyUseCase;
import me.BlockDynasty.Economy.aplication.useCase.currency.GetCurrencyUseCase;
import me.BlockDynasty.Economy.domain.entities.currency.Currency;
import me.BlockDynasty.Economy.domain.entities.currency.Exceptions.CurrencyNotFoundException;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

public class CurrencyListDelete extends AbstractGUI {
    private final BlockDynastyEconomy plugin;
    private final Player player;
    private final GetCurrencyUseCase getCurrencyUseCase;
    private final DeleteCurrencyUseCase deleteCurrencyUseCase;
    private int currentPage = 0;
    private final int CURRENCIES_PER_PAGE = 21;

    public CurrencyListDelete(BlockDynastyEconomy plugin, Player player) {
        super("Lista de Monedas", 5);
        this.plugin = plugin;
        this.player = player;
        this.getCurrencyUseCase = plugin.getUsesCase().getCurrencyUseCase();
        this.deleteCurrencyUseCase = plugin.getUsesCase().deleteCurrencyUseCase();

        showCurrenciesPage();
    }

    private void showCurrenciesPage() {
        // Get all currencies
        List<Currency> currencies = getCurrencyUseCase.getCurrencies();

        // Calculate pagination
        int startIndex = currentPage * CURRENCIES_PER_PAGE;
        int endIndex = Math.min(startIndex + CURRENCIES_PER_PAGE, currencies.size());

        // Clear GUI
        for (int i = 0; i < getInventory().getSize(); i++) {
            setItem(i, null, null);
        }

        if (currencies.isEmpty()) {
            setItem(22, createItem(Material.BARRIER, "§cNo hay monedas",
                    "§7No hay monedas registradas en el sistema"), null);

            // Back button
            setItem(40, createItem(Material.ARROW, "§aVolver",
                    "§7Click para volver"), unused -> {
                player.closeInventory();
                openCurrencyEditorGUI();
            });

            return;
        }

        // Add currencies to GUI
        int slot = 10;
        for (int i = startIndex; i < endIndex; i++) {
            Currency currency = currencies.get(i);
            ChatColor color = ChatColor.valueOf(currency.getColor());

            setItem(slot, createItem(Material.GOLD_INGOT,
                            color + currency.getSingular(),
                            "§7Singular: " + color + currency.getSingular(),
                            "§7Plural: " + color + currency.getPlural()),
                    unused -> {
                        player.closeInventory();
                        try {
                            deleteCurrencyUseCase.deleteCurrency(currency.getSingular());
                            player.sendMessage(F.getPrefix() + "§7Deleted currency: §a" + currency.getSingular());
                        } catch (CurrencyNotFoundException e) {
                            player.sendMessage(F.getPrefix()+"§7"+ e.getMessage()+" asegurate de tener otra moneda por defecto antes de eliminarla");
                        } catch (TransactionException e) {
                            player.sendMessage(F.getPrefix() + "§cError while deleting currency: §4" + e.getMessage());
                        }
                    });

            // Adjust slot position
            slot++;
            if (slot % 9 == 8) slot += 2;
        }

        // Navigation buttons
        if (currentPage > 0) {
            setItem(38, createItem(Material.ARROW, "§aPágina Anterior",
                    "§7Click para ver monedas anteriores"), unused -> {
                currentPage--;
                showCurrenciesPage();
            });
        }

        if (endIndex < currencies.size()) {
            setItem(42, createItem(Material.ARROW, "§aPágina Siguiente",
                    "§7Click para ver más monedas"), unused -> {
                currentPage++;
                showCurrenciesPage();
            });
        }

        // Back button
        setItem(40, createItem(Material.BARRIER, "§cVolver",
                "§7Click para volver"), unused -> {
            player.closeInventory();
            openCurrencyEditorGUI();
        });
    }
    private void openCurrencyEditorGUI() {
        // Create and open the CurrencyEditorGUI
        CurrencyPanelGUI editorGUI =
                new CurrencyPanelGUI(plugin, player);
        player.openInventory(editorGUI.getInventory());

        // Register GUI with manager
        plugin.getGuiManager().registerGUI(player, editorGUI);
    }

}