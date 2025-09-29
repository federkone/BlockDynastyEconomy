
package BlockDynasty.BukkitImplementation.Integrations.vault;

import BlockDynasty.BukkitImplementation.BlockDynastyEconomy;
import BlockDynasty.Economy.aplication.useCase.AccountsUseCase;
import BlockDynasty.Economy.aplication.useCase.CurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.TransactionsUseCase;
import BlockDynasty.BukkitImplementation.utils.Console;
import api.IApi;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;

public class VaultHandler implements  IVaultHandler {
    private VaultHook economy = null;
    private final BlockDynastyEconomy plugin;
    private final IApi api;

    public VaultHandler(BlockDynastyEconomy plugin, IApi api) {
        this.plugin = plugin;
        this.api = api;
    }

    public void hook() {
        try {
            if (this.economy == null) {
                this.economy = new VaultHook(api);
            }

            //if(plugin.getCurrencyManager().getDefaultCurrency() == null){
            //    UtilServer.consoleLog("No Default currency found. Vault linking disabled!, ensure you have at least one currency created and setup to Default.");
            //    return;
            //}

            ServicesManager sm = Bukkit.getServicesManager();
            sm.register(Economy.class, this.economy, plugin, ServicePriority.Highest);

            Console.log("Vault link enabled.");
        } catch (Exception e) {
            Console.logError(e.getMessage());
        }
    }

    public void unhook() {
        ServicesManager sm = Bukkit.getServicesManager();
        if(this.economy != null){
            sm.unregister(Economy.class, this.economy);
            this.economy = null;
        }
    }

}
