package BlockDynasty.BukkitImplementation.Integrations.vault;

import BlockDynasty.BukkitImplementation.BlockDynastyEconomy;
import BlockDynasty.BukkitImplementation.Integrations.vaultUnloked.VaultUnlockedHandler;
import BlockDynasty.BukkitImplementation.utils.JavaUtil;
import BlockDynasty.BukkitImplementation.utils.Console;
import api.IApi;
import org.bukkit.Bukkit;

public class Vault {
    private static final BlockDynastyEconomy plugin = BlockDynastyEconomy.getInstance();
    private static IVaultHandler vaultHandler;

    public static void init(IApi api) {
        if (!BlockDynastyEconomy.getConfiguration().getBoolean("vault")) {
            Console.log("Vault integration is disabled.");
            return;
        }

        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            Console.log("Vault plugin not found. Vault integration will not be enabled.");
            return;
        }

        if(JavaUtil.classExists("net.milkbowl.vault2.economy.Economy")){  //if is present vault 2/vault Unlocked plugin
            vaultHandler = new VaultUnlockedHandler(plugin,api);
        }else {
            vaultHandler = new VaultHandler(plugin,api);
        }

        vaultHandler.hook();
    }

    public static void unhook(){
        if (vaultHandler != null) vaultHandler.unhook();
    }
}
