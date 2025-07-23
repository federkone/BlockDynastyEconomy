package BlockDynasty.FoliaImplementation.scheduler;

import BlockDynasty.BukkitImplementation.scheduler.IScheduler;

//FOLIA IMPLEMENTATION, STOP SUPPORT ASYNCHRONOUS TASKS
//esta clase ya está preparada para funcionar en Folia
public class Scheduler  implements IScheduler {

    public void runLater(long delay, Runnable runnable)
    {
        runnable.run();
    }

    public void runAsync(Runnable runnable) {
        runnable.run();
    }

    public void run(Runnable runnable){
        runnable.run();
    }

    public static Scheduler init() {
        return new Scheduler();
    }
}
