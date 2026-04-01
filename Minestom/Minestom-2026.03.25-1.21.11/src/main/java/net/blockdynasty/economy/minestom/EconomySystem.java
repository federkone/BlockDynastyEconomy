package net.blockdynasty.economy.minestom;

import net.blockdynasty.economy.engine.platform.IPlatform;
import net.blockdynasty.economy.minestom.commons.EconomyHandler;
import net.blockdynasty.economy.minestom.commons.adapters.IMaterialAdapter;
import net.blockdynasty.economy.minestom.commons.adapters.PlatformAdapter;
import net.blockdynasty.economy.minestom.commons.services.PermissionsService;

public class EconomySystem {
    private static EconomyHandler economySystem;

    public static void start(boolean onlineMode,PermissionsService permissionsService){
        IMaterialAdapter materialAdapter = new net.blockdynasty.economy.minestom.adapters.MaterialAdapter();
        IPlatform platform = new PlatformAdapter(onlineMode,materialAdapter);
        economySystem = new EconomyHandler(platform);
        economySystem.start(onlineMode, permissionsService);
    }

    public static void start(boolean onlineMode){
        IMaterialAdapter materialAdapter = new net.blockdynasty.economy.minestom.adapters.MaterialAdapter();
        IPlatform platform = new PlatformAdapter(onlineMode,materialAdapter);
        economySystem = new EconomyHandler(platform);
        economySystem.start(onlineMode);
    }

    public static void stop(){
        if(economySystem != null){
            economySystem.stop();
        }
    }

    public static boolean isStarted(){
        if(economySystem != null){
           return economySystem.isStarted();
        }
        return false;
    }
}
