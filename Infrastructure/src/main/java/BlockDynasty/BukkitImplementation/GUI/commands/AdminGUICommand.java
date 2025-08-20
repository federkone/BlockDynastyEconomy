package BlockDynasty.BukkitImplementation.GUI.commands;

import BlockDynasty.BukkitImplementation.GUI.GUIFactory;
import BlockDynasty.BukkitImplementation.GUI.MaterialAdapter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AdminGUICommand implements CommandExecutor {

    public AdminGUICommand() {
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cOnly players can use this command.");
            return true;
        }
        GUIFactory.adminPanel(player).open();
        player.playSound(player.getLocation(), MaterialAdapter.getClickSound(), 0.3f, 1.0f);
        return true;
    }
}
