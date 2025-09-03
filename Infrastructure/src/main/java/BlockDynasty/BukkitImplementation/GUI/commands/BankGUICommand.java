package BlockDynasty.BukkitImplementation.GUI.commands;


import BlockDynasty.BukkitImplementation.GUI.adapters.MaterialAdapter;
import BlockDynasty.BukkitImplementation.GUI.adapters.PlayerAdapter;
import lib.GUIFactory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BankGUICommand implements CommandExecutor {
    public BankGUICommand() {
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cOnly players can use this command.");
            return true;
        }

        GUIFactory.bankPanel(new PlayerAdapter(player)).open();
        //player.playSound(player.getLocation(), MaterialAdapter.getClickSound(), 0.3f, 1.0f);
        return true;
    }
}