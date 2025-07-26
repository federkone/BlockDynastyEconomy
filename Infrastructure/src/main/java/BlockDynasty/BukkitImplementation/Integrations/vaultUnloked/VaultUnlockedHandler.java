package BlockDynasty.BukkitImplementation.Integrations.vaultUnloked;

import BlockDynasty.BukkitImplementation.BlockDynastyEconomy;
import BlockDynasty.BukkitImplementation.Integrations.vault.IVaultHandler;
import BlockDynasty.BukkitImplementation.utils.UtilServer;
import BlockDynasty.Economy.aplication.useCase.UsesCaseFactory;
import net.milkbowl.vault2.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;

public class VaultUnlockedHandler implements IVaultHandler {
    private VaultUnlockedHook vaultUnlockedHook;
    private final BlockDynastyEconomy plugin;
    private final UsesCaseFactory usesCaseFactory;

    public VaultUnlockedHandler(BlockDynastyEconomy plugin, UsesCaseFactory usesCaseFactory) {
        this.plugin = plugin;
        this.usesCaseFactory = usesCaseFactory;
    }

    public void hook() {
        try {
            if (this.vaultUnlockedHook == null) {
                this.vaultUnlockedHook = new VaultUnlockedHook(usesCaseFactory);
            }

            ServicesManager sm = Bukkit.getServicesManager();
            sm.register(Economy.class, this.vaultUnlockedHook, plugin, ServicePriority.Highest);

            UtilServer.consoleLog("Vault Unlocked detect, link enabled.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void unhook() {
        ServicesManager sm = Bukkit.getServicesManager();
        if (this.vaultUnlockedHook != null) {
            sm.unregister(Economy.class, this.vaultUnlockedHook);
            this.vaultUnlockedHook = null;
        }
    }
}
