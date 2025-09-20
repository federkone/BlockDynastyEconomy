package lib.scheduler;

public class ContextualTask {
    private final Runnable runnable;
    private final IEntity entity;
    private final ILocation locationContext;

    //future use cases could include for folia total compatibility
    //private final World worldContext;  // Contextual world, if needed

    private ContextualTask(Runnable runnable, IEntity entity, ILocation locationContext) {
        this.runnable = runnable;
        this.entity = entity;
        this.locationContext = locationContext;
    }

    // Static factory methods
    public static ContextualTask build(Runnable runnable) {
        return new ContextualTask(runnable, null, null );
    }

    public static ContextualTask build(Runnable runnable, IEntity entity) {
        return new ContextualTask(runnable, entity, null);
    }

    public static ContextualTask build(Runnable runnable, ILocation locationContext) {
        return new ContextualTask(runnable, null, locationContext);
    }

    public static ContextualTask build(Runnable runnable, IEntity entity, ILocation locationContext) {
        return new ContextualTask(runnable, entity, locationContext);
    }

    public Runnable getRunnable() {
        return runnable;
    }

    public IEntity getEntityContext() {
        return entity;
    }

    public ILocation getLocationContext() {
        return locationContext;
    }

    public boolean hasEntityContext() {
        return entity != null;
    }

    public boolean hasLocationContext() {
        return locationContext != null;
    }
}