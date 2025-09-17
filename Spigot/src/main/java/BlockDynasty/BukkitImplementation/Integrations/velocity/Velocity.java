package BlockDynasty.BukkitImplementation.Integrations.velocity;

import BlockDynasty.BukkitImplementation.BlockDynastyEconomy;
import BlockDynasty.BukkitImplementation.adapters.abstractions.BukkitAdapter;
import BlockDynasty.BukkitImplementation.utils.Console;
import api.IApi;
import lib.abstractions.PlatformAdapter;

public class Velocity {

    public static void init(BlockDynastyEconomy plugin, IApi api) {
        plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, "velocity:economy");  //outgoing channel
        plugin.getServer().getMessenger().registerIncomingPluginChannel(plugin, "velocity:economy", new VelocityReceiver());  //incoming channel
        Console.log("Velocity channel has been initialized.");
    }
}
