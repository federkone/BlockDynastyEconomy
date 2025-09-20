package BlockDynasty.BukkitImplementation.adapters.proxy;

import BlockDynasty.BukkitImplementation.BlockDynastyEconomy;
import BlockDynasty.BukkitImplementation.utils.Console;
import api.IApi;
import proxy.ProxyData;

public class ChannelRegister {

    public static void init(BlockDynastyEconomy plugin, IApi api) {
        plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, ProxyData.getChannelName());  //outgoing channel
        plugin.getServer().getMessenger().registerIncomingPluginChannel(plugin,ProxyData.getChannelName(), new ProxyReceiverImp());  //incoming channel
        Console.log("Proxy channel has been initialized. chanel Name: BungeeCord");
    }

    public static void unhook(BlockDynastyEconomy plugin) {
        plugin.getServer().getMessenger().unregisterIncomingPluginChannel(plugin, ProxyData.getChannelName());
        plugin.getServer().getMessenger().unregisterOutgoingPluginChannel(plugin, ProxyData.getChannelName());
    }
}
