package files.logs;

import BlockDynasty.Economy.domain.services.log.Log;
import Main.Console;
import files.Configuration;
import lib.scheduler.IScheduler;

public class EconomyLogger extends AbstractLogger{
    private boolean enable = true;

    public EconomyLogger(Configuration configuration, IScheduler scheduler) {
        super(configuration, scheduler);
        this.enable = configuration.getBoolean("transaction_log");
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
    public String getName() {return "[ECONOMY-LOG]";}

    public static Log build(Configuration configuration, IScheduler scheduler) {return new EconomyLogger(configuration,scheduler);}
}
