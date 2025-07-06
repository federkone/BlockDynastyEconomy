
package me.BlockDynasty.Integrations.vault;

import me.BlockDynasty.Economy.BlockDynastyEconomy;
import me.BlockDynasty.Economy.aplication.useCase.UsesCase;
import me.BlockDynasty.Economy.aplication.useCase.account.CreateAccountUseCase;
import me.BlockDynasty.Economy.aplication.useCase.account.GetAccountsUseCase;
import me.BlockDynasty.Economy.aplication.useCase.account.GetBalanceUseCase;
import me.BlockDynasty.Economy.aplication.useCase.currency.GetCurrencyUseCase;
import me.BlockDynasty.Economy.aplication.useCase.transaction.DepositUseCase;
import me.BlockDynasty.Economy.aplication.useCase.transaction.WithdrawUseCase;
import me.BlockDynasty.Economy.utils.UtilServer;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;

public class VaultHandler {
    private VaultHook economy = null;
    private final BlockDynastyEconomy plugin;
    private  final UsesCase usesCase;

    //tendria que tener sus propios casos de uso con inyeccion de logger off/null
    public VaultHandler(BlockDynastyEconomy plugin, UsesCase usesCase) {
        this.plugin = plugin;
        this.usesCase = usesCase;
    }

    public void hook() {
        try {
            if (this.economy == null) {
                this.economy = new VaultHook(usesCase);
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
