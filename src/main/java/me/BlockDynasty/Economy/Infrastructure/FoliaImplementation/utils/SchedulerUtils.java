package me.BlockDynasty.Economy.Infrastructure.FoliaImplementation.utils;

import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.BlockDynastyEconomy;

//FOLIA IMPLEMENTATION, STOP SUPPORT ASYNCHRONOUS TASKS
//esta clase ya está preparada para funcionar en Folia
public class SchedulerUtils {
    public static BlockDynastyEconomy plugin = BlockDynastyEconomy.getInstance();

    public static void runLater(long delay, Runnable runnable)
    {
        runnable.run();
    }

    public static void runAsync(Runnable runnable) {
        runnable.run();
    }

    public static void run(Runnable runnable){
        runnable.run();
    }
}
