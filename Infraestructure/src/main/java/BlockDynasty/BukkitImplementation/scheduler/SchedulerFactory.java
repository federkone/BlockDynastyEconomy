package BlockDynasty.BukkitImplementation.scheduler;

import BlockDynasty.BukkitImplementation.utils.UtilServer;

public class SchedulerFactory {
    // Static instance to cache the scheduler implementation
    private static IScheduler schedulerInstance;

    public static IScheduler getScheduler() {
        // Return cached instance if already determined
        if (schedulerInstance != null) {
            return schedulerInstance;
        }

        try {
            // Check if Folia API exists
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            // Folia detected
            schedulerInstance = BlockDynasty.FoliaImplementation.scheduler.Scheduler.init();
            UtilServer.consoleLog("Folia detected, applying Folia scheduler implementation.");
            System.out.println("Folia detected, applying Folia scheduler implementation.");
        } catch (ClassNotFoundException e) {
            // Regular Bukkit/Paper
            schedulerInstance = BlockDynasty.BukkitImplementation.scheduler.Scheduler.init();
        }

        return schedulerInstance;
    }

    public static void run(ContextualTask contextualTask){
        getScheduler().run(contextualTask);
    }

    public static void runAsync(ContextualTask contextualTask) {
        getScheduler().runAsync(contextualTask);
    }
    public static void runLater(long delay, ContextualTask contextualTask) {
        getScheduler().runLater(delay, contextualTask);
    }
}