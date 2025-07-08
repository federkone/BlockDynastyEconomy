package me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.GUI.commandsGUI;

import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.BlockDynastyEconomy;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.GUI.GUIService;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.GUI.views.TransactionsView.BalanceGUI;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.GUI.components.IGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BalanceGUICommand implements CommandExecutor {
    private final BlockDynastyEconomy plugin;
    private GUIService guiService;

    public BalanceGUICommand(BlockDynastyEconomy plugin) {
        this.plugin = plugin;
        this.guiService = plugin.getGuiManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can use this command.");
            return true;
        }

        Player player = (Player) sender;
        IGUI gui = new BalanceGUI(plugin, player);
        this.guiService.registerGUI(player, gui);
        gui.open(player);
        return true;
    }
}