package BlockDynasty.BukkitImplementation.logs;

import BlockDynasty.BukkitImplementation.BlockDynastyEconomy;
import BlockDynasty.Economy.domain.services.log.Log;

public class EconomyLogger extends AbstractLogger {
    private boolean enable = true;

    public EconomyLogger(BlockDynastyEconomy plugin) {
        super(plugin);
        this.enable = plugin.getConfig().getBoolean("transaction_log");
    }

    @Override
    public void log(String message) {
        if (!enable) {
            return;
        }
        super.log(message);
    }

    @Override
    public String getName() {return "[ECONOMY-LOG]";}

    public static Log build(BlockDynastyEconomy plugin) {return new EconomyLogger(plugin);}
}
