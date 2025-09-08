package BlockDynasty.BukkitImplementation.Integrations.bungee;

import BlockDynasty.BukkitImplementation.BlockDynastyEconomy;
import BlockDynasty.BukkitImplementation.utils.Console;
import BlockDynasty.Economy.aplication.useCase.account.SearchAccountUseCase;
import BlockDynasty.Economy.domain.services.IAccountService;
import api.IApi;

public class Bungee {

    public static void init(BlockDynastyEconomy plugin, IApi api) {
        plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, "BungeeCord");  //outgoing channel
        plugin.getServer().getMessenger().registerIncomingPluginChannel(plugin, "BungeeCord", new BungeeImpl(plugin,api.getAccountService()));  //incoming channel
        Console.log("BungeeCord channel has been initialized. chanel Name: BungeeCord");
    }

    public static void unhook(BlockDynastyEconomy plugin) {
        plugin.getServer().getMessenger().unregisterIncomingPluginChannel(plugin, "BungeeCord");
        plugin.getServer().getMessenger().unregisterOutgoingPluginChannel(plugin, "BungeeCord");
    }
}
