
package BlockDynasty.BukkitImplementation.Integrations.vault;

import BlockDynasty.BukkitImplementation.BlockDynastyEconomy;
import BlockDynasty.Economy.aplication.useCase.UsesCaseFactory;
import BlockDynasty.BukkitImplementation.utils.UtilServer;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;

public class VaultHandler {
    private VaultHook economy = null;
    private final BlockDynastyEconomy plugin;
    private  final UsesCaseFactory usesCaseFactory;

    //tendria que tener sus propios casos de uso con inyeccion de logger off/null
    public VaultHandler(BlockDynastyEconomy plugin, UsesCaseFactory usesCaseFactory) {
        this.plugin = plugin;
        this.usesCaseFactory = usesCaseFactory;
    }

    public void hook() {
        try {
            if (this.economy == null) {
                this.economy = new VaultHook(usesCaseFactory);
            }

            if(plugin.getCurrencyManager().getDefaultCurrency() == null){
                UtilServer.consoleLog("No Default currency found. Vault linking disabled!");
                return;
            }

            ServicesManager sm = Bukkit.getServicesManager();
            sm.register(Economy.class, this.economy, plugin, ServicePriority.Highest);

            UtilServer.consoleLog("Vault link enabled.");
        } catch (Exception e) {
            e.printStackTrace();
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
