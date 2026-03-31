package net.blockdynasty.economy.spigot.BukkitImplementation.adapters.commands;

import net.blockdynasty.economy.gui.commands.abstractions.AbstractCommand;
import net.blockdynasty.economy.gui.commands.abstractions.IEntityCommands;
import net.blockdynasty.economy.libs.util.colors.ChatColor;
import net.blockdynasty.economy.libs.util.colors.Colors;
import net.blockdynasty.economy.spigot.BukkitImplementation.BlockDynastyEconomy;
import net.blockdynasty.economy.spigot.BukkitImplementation.adapters.platformAdapter.EntityConsoleAdapter;


public class ReloadCommand extends AbstractCommand {
    private BlockDynastyEconomy plugin ;

    public ReloadCommand(BlockDynastyEconomy plugin) {
        super("reload", "BlockDynastyEconomy.economy.superUser");
        this.plugin = plugin;
    }

    @Override
    public boolean execute(IEntityCommands sender, String[] args) {
        if(!super.execute(sender, args)){
            return false;
        }
        if(sender instanceof EntityConsoleAdapter){
            plugin.reload();
        }else{
            sender.sendMessage(ChatColor.stringValueOf(Colors.RED)+"This command can only be executed by the console.");
        }
        return true;
    }
}
