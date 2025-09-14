package BlockDynasty.BukkitImplementation.Integrations.velocity;

import BlockDynasty.BukkitImplementation.BlockDynastyEconomy;
import BlockDynasty.BukkitImplementation.utils.Console;
import api.IApi;

public class Velocity {

    public static void init(BlockDynastyEconomy plugin, IApi api) {
        plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, "velocity:economy");  //outgoing channel
        plugin.getServer().getMessenger().registerIncomingPluginChannel(plugin, "velocity:economy", new VelocityReceiver(plugin,api.getAccountService()));  //incoming channel
        Console.log("BungeeCord channel has been initialized. chanel Name: BungeeCord");
    }
}
