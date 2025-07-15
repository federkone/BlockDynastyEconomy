package me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.GUI.views.admins;

import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.BlockDynastyEconomy;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.GUI.components.AbstractGUI;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.GUI.views.admins.adminPanels.CreateCurrencyGUI;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.GUI.views.admins.adminPanels.CurrencyListDelete;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.GUI.views.admins.adminPanels.CurrencyListEdit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class CurrencyPanelGUI extends AbstractGUI {
    private final BlockDynastyEconomy plugin;
    private final Player player;

    public CurrencyPanelGUI(BlockDynastyEconomy plugin, Player player) {
        super("Administrador de Monedas", 3);
        this.plugin = plugin;
        this.player = player;

        setupGUI();
    }

    private void setupGUI() {
        // Create Currency button
        setItem(10, createItem(Material.EMERALD, "§aCrear Moneda",
                "§7Click para crear una nueva moneda"), unused -> {
            player.closeInventory();
            new CreateCurrencyGUI(plugin, player);
            //player.sendMessage("§6[Sistema] §eCrear moneda: Función en desarrollo");
        });

        // Delete Currency button
        setItem(12, createItem(Material.REDSTONE, "§cEliminar Moneda",
                "§7Click para eliminar una moneda existente"), unused -> {
            player.closeInventory();
            currencyListDelete();
        });

        // Edit Currency button
        setItem(14, createItem(Material.WRITABLE_BOOK, "§eEditar Moneda",
                "§7Click para editar una moneda existente"), unused -> {
            player.closeInventory();
            openCurrencyListGUI();
            //player.sendMessage("§6[Sistema] §eEditar moneda: Función en desarrollo");
        });

        // Toggle Features button
        setItem(16, createItem(Material.COMPARATOR, "§bConfigurar Características",
                "§7Click para activar/desactivar características"), unused -> {
            player.closeInventory();
            // TODO: Implement Toggle Features GUI
            player.sendMessage("§6[Banco] §eFunción en desarrollo");
        });

        // Exit button
        setItem(22, createItem(Material.BARRIER, "§cSalir",
                "§7Click para salir"), unused -> player.closeInventory());
    }

    private void openCurrencyListGUI() {
        CurrencyListEdit currencyListEdit = new CurrencyListEdit(plugin, player);
        player.openInventory(currencyListEdit.getInventory());

        // Register the GUI with the GUIService
        plugin.getGuiManager().registerGUI(player, currencyListEdit);
    }

    private void currencyListDelete() {
        CurrencyListDelete currencyListDelete = new CurrencyListDelete(plugin, player);
        player.openInventory(currencyListDelete.getInventory());

        // Register the GUI with the GUIService
        plugin.getGuiManager().registerGUI(player, currencyListDelete);
    }
}