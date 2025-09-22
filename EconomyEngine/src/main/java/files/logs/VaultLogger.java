package files.logs;

import BlockDynasty.Economy.domain.services.log.Log;
import Main.Console;
import files.Configuration;
import lib.scheduler.IScheduler;

public class VaultLogger extends  AbstractLogger{
    private boolean enable = false;

    public VaultLogger(Configuration configuration, IScheduler scheduler) {
        super(configuration, scheduler);
        this.enable = configuration.getBoolean("transaction_log_vault");
    }


    @Override
    public void log(String message) {
        Console.debug(getName() + message);
        if (!enable) {
            return;
        }
        super.log(message);
    }

    @Override
    public String getName() {
        return "[VAULT-LOG] ";
    }

    public static Log build(Configuration configuration, IScheduler scheduler) {return new VaultLogger(configuration,scheduler);}
}
