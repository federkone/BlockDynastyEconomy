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

    public static void run(Runnable runnable){
        getScheduler().run( runnable);
    }

    public static void runAsync(Runnable runnable) {
        getScheduler().runAsync(runnable);
    }
    public static void runLater(long delay, Runnable runnable) {
        getScheduler().runLater(delay, runnable);
    }
}