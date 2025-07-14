package me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.GUI.views.admins.adminPanels;

import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.BlockDynastyEconomy;
import me.BlockDynasty.Economy.domain.entities.currency.Currency;
import org.bukkit.entity.Player;

public class CurrencyListEdit extends AbstractCurrenciesList {
    private final BlockDynastyEconomy plugin;

    public CurrencyListEdit(BlockDynastyEconomy plugin, Player player) {
        super(plugin, player);
        this.plugin = plugin;
    }

    @Override
    public void openSubMenu(Currency currency, Player player) {
        EditCurrencyGUI editCurrencyGUI = new EditCurrencyGUI(plugin, player,currency);
        player.openInventory(editCurrencyGUI.getInventory());
        plugin.getGuiManager().registerGUI(player, editCurrencyGUI);
    }

}