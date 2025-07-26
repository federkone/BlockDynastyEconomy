package BlockDynasty.BukkitImplementation.Integrations.bungee;

import BlockDynasty.BukkitImplementation.BlockDynastyEconomy;

public class Bungee {
    private final static BlockDynastyEconomy plugin = BlockDynastyEconomy.getInstance();

    public static void init(){
        plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, "BungeeCord");  //outgoing channel
        plugin.getServer().getMessenger().registerIncomingPluginChannel(plugin, "BungeeCord", new BungeeImpl(plugin));  //incoming channel
    }
}
