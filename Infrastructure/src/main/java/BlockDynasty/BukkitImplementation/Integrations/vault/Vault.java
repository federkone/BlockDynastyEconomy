package BlockDynasty.BukkitImplementation.Integrations.vault;

import BlockDynasty.BukkitImplementation.BlockDynastyEconomy;
import BlockDynasty.BukkitImplementation.Integrations.vaultUnloked.VaultUnlockedHandler;
import BlockDynasty.BukkitImplementation.utils.JavaUtil;
import BlockDynasty.BukkitImplementation.utils.UtilServer;
import BlockDynasty.Economy.aplication.useCase.UsesCaseFactory;
import org.bukkit.Bukkit;

public class Vault {
    private static final BlockDynastyEconomy plugin = BlockDynastyEconomy.getInstance();
    private static IVaultHandler vaultHandler;

    public static void init(UsesCaseFactory usesCaseFactory) {
        if (!plugin.getConfig().getBoolean("vault")) {
            UtilServer.consoleLog("Vault integration is disabled.");
            return;
        }

        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            UtilServer.consoleLog("Vault plugin not found. Vault integration will not be enabled.");
            return;
        }

        if(JavaUtil.classExists("net.milkbowl.vault2.economy.Economy")){  //if is present vault 2/vault Unlocked plugin
            vaultHandler = new VaultUnlockedHandler(plugin, usesCaseFactory);
        }else {
            vaultHandler = new VaultHandler(plugin,usesCaseFactory);
        }

        vaultHandler.hook();
    }

    public static void unhook(){
        if (vaultHandler != null) vaultHandler.unhook();
    }
}
