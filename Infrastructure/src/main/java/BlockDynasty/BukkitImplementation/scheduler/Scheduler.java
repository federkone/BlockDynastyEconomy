package BlockDynasty.BukkitImplementation.scheduler;

public class Scheduler {

    /**
     * Runs a task on another thread after a delay.
     * @param delay - Delay in ticks.
     * @param contextualTask - Context to perform.
     */
    public static void runLater(long delay, ContextualTask contextualTask) {
        SchedulerFactory.runLater( delay, contextualTask);
    }

    /**
     * Runs a task on another thread immediately.
     * @param contextualTask - Context to perform.
     */
    public static void runAsync(ContextualTask contextualTask) {
        SchedulerFactory.runAsync( contextualTask);
    }

    /**
     * Runs a task on the main thread immediately
     * @param contextualTask - Context to perform
     */
    public static  void run(ContextualTask contextualTask) {
        SchedulerFactory.run(contextualTask);
    }
}
