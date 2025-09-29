package BlockDynasty.BukkitImplementation.scheduler;

import BlockDynasty.BukkitImplementation.utils.JavaUtil;
import BlockDynasty.BukkitImplementation.utils.Console;
import BlockDynasty.FoliaImplementation.scheduler.SchedulerFolia;
import lib.scheduler.ContextualTask;
import lib.scheduler.IScheduler;

public class SchedulerFactory {
    public static final boolean isFolia = JavaUtil.classExists("io.papermc.paper.threadedregions.RegionizedServer");
    public static final boolean isCanvas = JavaUtil.classExists("io.canvasmc.canvas.server.ThreadedServer"); //tested and is redundant with Folia, probably not needed
    private static IScheduler schedulerInstance;

    public static IScheduler getScheduler() {
        if (schedulerInstance != null) {
            return schedulerInstance;
        }

        if(isFolia || isCanvas){
            schedulerInstance = SchedulerFolia.init();
            Console.log("Folia detected, applying Folia scheduler implementation.");
        }else{
            schedulerInstance = SchedulerBukkit.init();
            Console.log("Bukkit detected, applying Bukkit scheduler implementation.");
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