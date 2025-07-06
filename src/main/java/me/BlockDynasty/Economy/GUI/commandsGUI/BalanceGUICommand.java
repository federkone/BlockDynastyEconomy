package me.BlockDynasty.Economy.GUI.commandsGUI;

import me.BlockDynasty.Economy.BlockDynastyEconomy;
import me.BlockDynasty.Economy.GUI.GUIManager;
import me.BlockDynasty.Economy.GUI.account.BalanceGUI;
import me.BlockDynasty.Economy.GUI.IGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BalanceGUICommand implements CommandExecutor {
    private final BlockDynastyEconomy plugin;
    private GUIManager guiManager;

    public BalanceGUICommand(BlockDynastyEconomy plugin) {
        this.plugin = plugin;
        this.guiManager = plugin.getGuiManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can use this command.");
            return true;
        }

        Player player = (Player) sender;
        IGUI gui = new BalanceGUI(plugin, player);
        this.guiManager.registerGUI(player, gui);
        gui.open(player);
        return true;
    }
}