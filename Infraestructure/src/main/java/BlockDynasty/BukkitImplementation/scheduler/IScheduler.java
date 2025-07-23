package BlockDynasty.BukkitImplementation.scheduler;

public interface IScheduler {
    void runLater(long delay, Runnable runnable);
    void runAsync(Runnable runnable);
    void run(Runnable runnable);
}
