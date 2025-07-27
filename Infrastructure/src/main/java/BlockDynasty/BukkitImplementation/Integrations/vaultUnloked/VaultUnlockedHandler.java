package BlockDynasty.BukkitImplementation.Integrations.vaultUnloked;

import BlockDynasty.BukkitImplementation.BlockDynastyEconomy;
import BlockDynasty.BukkitImplementation.Integrations.vault.IVaultHandler;
import BlockDynasty.BukkitImplementation.utils.UtilServer;
import BlockDynasty.Economy.aplication.useCase.AccountsUseCase;
import BlockDynasty.Economy.aplication.useCase.CurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.TransactionsUseCase;
import net.milkbowl.vault2.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;

public class VaultUnlockedHandler implements IVaultHandler {
    private VaultUnlockedHook vaultUnlockedHook;
    private final BlockDynastyEconomy plugin;
    private final AccountsUseCase accountsUseCase;
    private final CurrencyUseCase currencyUseCase;
    private final TransactionsUseCase transactionsUseCase;

    public VaultUnlockedHandler(BlockDynastyEconomy plugin, AccountsUseCase accountsUseCase, CurrencyUseCase currencyUseCase, TransactionsUseCase transactionsUseCase) {
        this.plugin = plugin;
        this.accountsUseCase = accountsUseCase;
        this.currencyUseCase = currencyUseCase;
        this.transactionsUseCase = transactionsUseCase;
    }

    public void hook() {
        try {
            if (this.vaultUnlockedHook == null) {
                this.vaultUnlockedHook = new VaultUnlockedHook(accountsUseCase,currencyUseCase,transactionsUseCase);
            }

            ServicesManager sm = Bukkit.getServicesManager();
            sm.register(Economy.class, this.vaultUnlockedHook, plugin, ServicePriority.Highest);

            UtilServer.consoleLog("Vault Unlocked detect, link enabled.");
        } catch (Exception e) {
            UtilServer.consoleLogError(e.getMessage());
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
