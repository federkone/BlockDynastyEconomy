package BlockDynasty.BukkitImplementation.logs;

import BlockDynasty.BukkitImplementation.BlockDynastyEconomy;
import BlockDynasty.Economy.domain.services.log.Log;

public class VaultLogger extends AbstractLogger {
    private boolean enable = false;

    public VaultLogger(BlockDynastyEconomy plugin) {
        super(plugin);
        this.enable = plugin.getConfig().getBoolean("transaction_log_vault");
    }

    @Override
    public void log(String message) {
        if (!enable) {
            return;
        }
        super.log(message);
    }

    @Override
    public String getName() {
        return "[VAULT-LOG] ";
    }

    public static Log build(BlockDynastyEconomy plugin) {return new VaultLogger(plugin);}
}
