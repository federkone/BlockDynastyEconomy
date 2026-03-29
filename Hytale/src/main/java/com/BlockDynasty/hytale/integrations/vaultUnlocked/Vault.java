package com.BlockDynasty.hytale.integrations.vaultUnlocked;

import com.BlockDynasty.api.DynastyEconomy;
import com.BlockDynasty.hytale.utils.JavaUtil;
import com.blockdynasty.economy.Economy;
import net.cfh.vault.VaultUnlockedServicesManager;
import services.Console;

import java.util.Optional;

public class Vault {
    public static void register(){
        Optional<DynastyEconomy> api = Economy.getApi();
        if (api.isEmpty()) {
            Console.log("No economy API found. Vault Unlocked integration will not be enabled.");
            return;
        }
        if(JavaUtil.classExists("net.cfh.vault.VaultUnlockedServicesManager")){
            VaultUnlockedServicesManager.get().economy(new vaultHook(api.get()));
            Console.log("Vault Unlocked Hook successful");
        }else {Console.log("Vault Unlocked not detected");}
    }
    public static void unregister(){
        //VaultUnlockedServicesManager.get().economy();
    }
}