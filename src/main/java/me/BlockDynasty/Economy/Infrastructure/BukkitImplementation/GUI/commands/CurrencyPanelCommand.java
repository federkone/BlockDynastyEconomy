package me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.GUI.commands;

import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.BlockDynastyEconomy;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.GUI.services.GUIService;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.GUI.components.IGUI;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.GUI.views.admins.CurrencyPanelGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CurrencyPanelCommand implements CommandExecutor {
    private final BlockDynastyEconomy plugin;
    private GUIService guiService;

    public CurrencyPanelCommand(BlockDynastyEconomy plugin) {
        this.plugin = plugin;
        this.guiService = plugin.getGuiManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("blockdynasty.economy.currencypanel")) {
            sender.sendMessage("§cYou do not have permission to use this command.");
            return true;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can use this command.");
            return true;
        }

        Player player = (Player) sender;
        IGUI gui = new CurrencyPanelGUI(plugin, player);
        this.guiService.registerGUI(player, gui);
        gui.open(player);
        return true;
    }
}