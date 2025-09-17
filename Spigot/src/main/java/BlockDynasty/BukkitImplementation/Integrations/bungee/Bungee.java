package BlockDynasty.BukkitImplementation.Integrations.bungee;

import BlockDynasty.BukkitImplementation.BlockDynastyEconomy;
import BlockDynasty.BukkitImplementation.adapters.abstractions.BukkitAdapter;
import BlockDynasty.BukkitImplementation.utils.Console;
import api.IApi;
import lib.abstractions.PlatformAdapter;
import proxy.ProxyData;

public class Bungee {

    public static void init(BlockDynastyEconomy plugin, IApi api) {
        ProxyData data = new BungeeData();
        plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, data.getChannelName());  //outgoing channel
        plugin.getServer().getMessenger().registerIncomingPluginChannel(plugin, data.getChannelName(), new BungeeReceiver());  //incoming channel
        Console.log("BungeeCord channel has been initialized. chanel Name: BungeeCord");
    }

    public static void unhook(BlockDynastyEconomy plugin) {
        plugin.getServer().getMessenger().unregisterIncomingPluginChannel(plugin, "BungeeCord");
        plugin.getServer().getMessenger().unregisterOutgoingPluginChannel(plugin, "BungeeCord");
    }
}
