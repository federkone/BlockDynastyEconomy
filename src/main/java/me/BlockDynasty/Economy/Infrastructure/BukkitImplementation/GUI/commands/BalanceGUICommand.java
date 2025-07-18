package me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.GUI.commands;

import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.BlockDynastyEconomy;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.GUI.services.GUIService;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.GUI.views.users.userPanels.BalanceGUI;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.GUI.components.IGUI;
import me.BlockDynasty.Economy.aplication.useCase.balance.GetBalanceUseCase;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class BalanceGUICommand implements CommandExecutor {
    private final JavaPlugin plugin;
    private GUIService guiService;
    private GetBalanceUseCase getBalanceUseCase;

    public BalanceGUICommand(JavaPlugin plugin, GUIService guiService, GetBalanceUseCase getBalanceUseCase) {
        this.plugin = plugin;
        this.guiService = guiService;
        this.getBalanceUseCase = getBalanceUseCase;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can use this command.");
            return true;
        }

        Player player = (Player) sender;
        IGUI gui = new BalanceGUI(plugin, player,getBalanceUseCase);
        this.guiService.registerGUI(player, gui);
        gui.open(player);
        return true;
    }
}