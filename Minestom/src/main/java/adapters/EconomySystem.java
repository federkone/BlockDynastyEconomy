package adapters;

import Main.Economy;
import api.IApi;
import minestom.commands.Commands;
import minestom.events.ClickInventoryEvent;
import minestom.events.playerExitEvent;
import minestom.events.playerJoinEvent;

//build economy engine with dependency injection
public class EconomySystem {
    private static Economy economy;

    public static void start(){
        economy= Economy.init(new PlatformAdapter());
        playerJoinEvent.register(economy.getPlayerJoinListener());
        playerExitEvent.register(economy.getPlayerJoinListener());
        Commands.register();
        ClickInventoryEvent.register();
    }

    public static void stop(){
        Economy.shutdown();
    }

    public static IApi getApi(){
        return economy.getApi();
    }
}
