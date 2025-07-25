package BlockDynasty.BukkitImplementation.GUI.commands;

import BlockDynasty.BukkitImplementation.BlockDynastyEconomy;
import BlockDynasty.BukkitImplementation.GUI.services.GUIService;
import BlockDynasty.BukkitImplementation.GUI.views.users.userPanels.BalanceGUI;
import BlockDynasty.BukkitImplementation.GUI.components.IGUI;
import BlockDynasty.Economy.aplication.useCase.balance.GetBalanceUseCase;
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