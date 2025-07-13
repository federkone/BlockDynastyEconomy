package me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.GUI.views;

import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.BlockDynastyEconomy;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.GUI.views.TransactionsView.BalanceGUI;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.GUI.views.TransactionsView.PayGUI;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.GUI.components.AbstractGUI;
import me.BlockDynasty.Economy.aplication.useCase.currency.GetCurrencyUseCase;
import me.BlockDynasty.Economy.aplication.useCase.transaction.PayUseCase;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class BankGUI extends AbstractGUI {
    private final BlockDynastyEconomy plugin;
    private final Player player;
    private final PayUseCase payUseCase;
    private final GetCurrencyUseCase getCurrencyUseCase;

    public BankGUI(BlockDynastyEconomy plugin, Player player) {
        super("Banco", 3);
        this.plugin = plugin;
        this.player = player;
        this.payUseCase = plugin.getUsesCase().getPayUseCase();
        this.getCurrencyUseCase = plugin.getUsesCase().getCurrencyUseCase();

        setupGUI();
    }

    private void setupGUI() {
        // Balance option
        setItem(11, createItem(Material.GOLD_INGOT, "§6Ver Balance",
                "§7Click para ver tu balance"), unused -> {
            player.closeInventory();
            openBalanceGUI();
        });

        // Pay option
        setItem(15, createItem(Material.PLAYER_HEAD, "§aPagar a un Jugador",
                "§7Click para pagar a otro jugador"), unused -> {
            player.closeInventory();
            openPayGUI();
        });

        // Exit button
        setItem(22, createItem(Material.BARRIER, "§cSalir",
                "§7Click para salir"), unused -> player.closeInventory());
    }

    private void openBalanceGUI() {
        BalanceGUI balanceGUI = new BalanceGUI(plugin, player);
        player.openInventory(balanceGUI.getInventory());

        // Register the GUI with the GUIService
        plugin.getGuiManager().registerGUI(player, balanceGUI);
    }

    private void openPayGUI() {
        PayGUI payGUI = new PayGUI(plugin, payUseCase, player, getCurrencyUseCase);
        player.openInventory(payGUI.getInventory());

        // Register the GUI with the GUIService
        plugin.getGuiManager().registerGUI(player, payGUI);
    }
}