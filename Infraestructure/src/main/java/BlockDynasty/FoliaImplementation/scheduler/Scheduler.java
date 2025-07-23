package BlockDynasty.FoliaImplementation.scheduler;

import BlockDynasty.BukkitImplementation.BlockDynastyEconomy;
import BlockDynasty.BukkitImplementation.scheduler.ContextualTask;
import BlockDynasty.BukkitImplementation.scheduler.IScheduler;
import org.bukkit.Bukkit;


//FOLIA IMPLEMENTATION, STOP SUPPORT ASYNCHRONOUS TASKS
//esta clase ya está preparada para funcionar en Folia
public class Scheduler  implements IScheduler {
    public static BlockDynastyEconomy plugin = BlockDynastyEconomy.getInstance();

    public void runLater(long delay, ContextualTask contextualTask) {
        Bukkit.getGlobalRegionScheduler().runDelayed(plugin, task -> contextualTask.getRunnable().run(), delay);
    }
    public void runAsync(ContextualTask contextualTask) {
        Bukkit.getAsyncScheduler().runNow(plugin, scheduledTask -> contextualTask.getRunnable().run());
    }

    public void run(ContextualTask task){
        if (task.hasPlayerContext()) {
            Bukkit.getRegionScheduler().execute(plugin, task.getPlayerContext().getLocation(), task.getRunnable());
        } else if (task.hasLocationContext()) {
            Bukkit.getRegionScheduler().execute(plugin, task.getLocationContext(), task.getRunnable());
        } else {
            Bukkit.getGlobalRegionScheduler().execute(plugin, task.getRunnable());
        }
    }

    public static Scheduler init() {
        return new Scheduler();
    }
}
