package adapters;

import Main.Economy;
import api.IApi;
import adapters.commands.Commands;
import adapters.events.ClickInventoryEvent;
import adapters.events.playerExitEvent;
import adapters.events.playerJoinEvent;

//build economy engine with dependency injection
public class EconomySystem {
    private static Economy economy;

    public static void start(boolean onlineMode){
        EconomySystem.economy= Economy.init(new PlatformAdapter(onlineMode));
        playerJoinEvent.register(economy.getPlayerJoinListener());
        playerExitEvent.register(economy.getPlayerJoinListener());
        Commands.register();
        ClickInventoryEvent.register();
    }

    public static void stop(){
        Economy.shutdown();
    }

    public static boolean isStarted(){
        return EconomySystem.economy!=null;
    }

    public static IApi getApi(){
        return EconomySystem.economy.getApi();
    }
}
