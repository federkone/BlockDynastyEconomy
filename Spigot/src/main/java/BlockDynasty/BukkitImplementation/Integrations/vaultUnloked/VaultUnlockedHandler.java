package BlockDynasty.BukkitImplementation.Integrations.vaultUnloked;

import BlockDynasty.BukkitImplementation.BlockDynastyEconomy;
import BlockDynasty.BukkitImplementation.Integrations.vault.IVaultHandler;
import BlockDynasty.BukkitImplementation.utils.Console;
import BlockDynasty.Economy.aplication.useCase.AccountsUseCase;
import BlockDynasty.Economy.aplication.useCase.CurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.TransactionsUseCase;
import api.IApi;
import net.milkbowl.vault2.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;

public class VaultUnlockedHandler implements IVaultHandler {
    private VaultUnlockedHook vaultUnlockedHook;
    private final BlockDynastyEconomy plugin;
    private IApi api;

    public VaultUnlockedHandler(BlockDynastyEconomy plugin, IApi api) {
        this.plugin = plugin;
        this.api = api;
    }

    public void hook() {
        try {
            if (this.vaultUnlockedHook == null) {
                this.vaultUnlockedHook = new VaultUnlockedHook(api);
            }

            ServicesManager sm = Bukkit.getServicesManager();
            sm.register(Economy.class, this.vaultUnlockedHook, plugin, ServicePriority.Highest);

            Console.log("Vault Unlocked detect, link enabled.");
        } catch (Exception e) {
            Console.logError(e.getMessage());
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
