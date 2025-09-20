package BlockDynasty.FoliaImplementation.scheduler;

import BlockDynasty.BukkitImplementation.BlockDynastyEconomy;
import lib.scheduler.IScheduler;
import lib.scheduler.ContextualTask;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

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
            Entity entity = (Entity) contextualTask.getEntityContext().getRoot();
            entity.getScheduler().execute(plugin, contextualTask.getRunnable(), null, 1);
            //Bukkit.getRegionScheduler().execute(plugin, contextualTask.getEntityContext().getLocation(), contextualTask.getRunnable());
            return;
        }

        if(contextualTask.hasLocationContext()) {
            Location location = (Location) contextualTask.getLocationContext().getRoot();
            Bukkit.getRegionScheduler().execute(plugin, location, contextualTask.getRunnable());
            return;
        }

        Bukkit.getGlobalRegionScheduler().execute(plugin, contextualTask.getRunnable());
    }

    public static SchedulerFolia init() {
        return new SchedulerFolia();
    }
}
