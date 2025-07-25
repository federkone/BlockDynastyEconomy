package BlockDynasty.BukkitImplementation.scheduler;

public interface IScheduler {
    void runLater(long delay, ContextualTask contextualTask);
    void runAsync(ContextualTask contextualTask);
    void run(ContextualTask contextualTask);
}
