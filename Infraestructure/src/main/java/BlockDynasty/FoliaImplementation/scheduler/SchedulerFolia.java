package BlockDynasty.FoliaImplementation.scheduler;

import BlockDynasty.BukkitImplementation.BlockDynastyEconomy;
import BlockDynasty.BukkitImplementation.scheduler.ContextualTask;
import BlockDynasty.BukkitImplementation.scheduler.IScheduler;
import org.bukkit.Bukkit;

public class SchedulerFolia implements IScheduler {
    public static BlockDynastyEconomy plugin = BlockDynastyEconomy.getInstance();

    public void runLater(long delay, ContextualTask contextualTask) {
        Bukkit.getGlobalRegionScheduler().runDelayed(plugin, task -> contextualTask.getRunnable().run(), delay);
    }

    public void runAsync(ContextualTask contextualTask) {
        Bukkit.getAsyncScheduler().runNow(plugin, scheduledTask -> contextualTask.getRunnable().run());
    }

    public void run(ContextualTask contextualTask){
        if (contextualTask.hasEntityContext()) {
            contextualTask.getEntityContext().getScheduler().execute(plugin, contextualTask.getRunnable(),null,1);
            //Bukkit.getRegionScheduler().execute(plugin, contextualTask.getEntityContext().getLocation(), contextualTask.getRunnable());
        } else if (contextualTask.hasLocationContext()) {
            Bukkit.getRegionScheduler().execute(plugin, contextualTask.getLocationContext(), contextualTask.getRunnable());
        } else {
            Bukkit.getGlobalRegionScheduler().execute(plugin, contextualTask.getRunnable());
        }
    }

    public static SchedulerFolia init() {
        return new SchedulerFolia();
    }
}
