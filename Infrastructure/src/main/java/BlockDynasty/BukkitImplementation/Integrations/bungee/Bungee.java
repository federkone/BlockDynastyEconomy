package BlockDynasty.BukkitImplementation.Integrations.bungee;

import BlockDynasty.BukkitImplementation.BlockDynastyEconomy;

public class Bungee {

    public static void init(BlockDynastyEconomy plugin) {
        plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, "BungeeCord");  //outgoing channel
        plugin.getServer().getMessenger().registerIncomingPluginChannel(plugin, "BungeeCord", new BungeeImpl(plugin));  //incoming channel
    }

    public static void unhook(BlockDynastyEconomy plugin) {
        plugin.getServer().getMessenger().unregisterIncomingPluginChannel(plugin, "BungeeCord");
        plugin.getServer().getMessenger().unregisterOutgoingPluginChannel(plugin, "BungeeCord");
    }
}
