package BlockDynasty.BukkitImplementation.Integrations.bungee;

import BlockDynasty.BukkitImplementation.BlockDynastyEconomy;
import BlockDynasty.BukkitImplementation.utils.UtilServer;

public class Bungee {

    public static void init(BlockDynastyEconomy plugin) {
        plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, "BungeeCord");  //outgoing channel
        plugin.getServer().getMessenger().registerIncomingPluginChannel(plugin, "BungeeCord", new BungeeImpl(plugin));  //incoming channel
        UtilServer.consoleLog("BungeeCord channel has been initialized. chanel Name: BungeeCord");
    }

    public static void unhook(BlockDynastyEconomy plugin) {
        plugin.getServer().getMessenger().unregisterIncomingPluginChannel(plugin, "BungeeCord");
        plugin.getServer().getMessenger().unregisterOutgoingPluginChannel(plugin, "BungeeCord");
    }
}
