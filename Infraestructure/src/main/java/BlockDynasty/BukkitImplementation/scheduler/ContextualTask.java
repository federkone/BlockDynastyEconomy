package BlockDynasty.BukkitImplementation.scheduler;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

public class ContextualTask {
    private final Runnable runnable;
    private final Entity entity;
    private final Location locationContext;

    //future use cases could include for folia total compatibility
    //private final World worldContext;  // Contextual world, if needed

    private ContextualTask(Runnable runnable, Entity entity, Location locationContext) {
        this.runnable = runnable;
        this.entity = entity;
        this.locationContext = locationContext;
    }

    // Static factory methods
    public static ContextualTask build(Runnable runnable) {
        return new ContextualTask(runnable, null, null );
    }

    public static ContextualTask build(Runnable runnable, Entity entity) {
        return new ContextualTask(runnable, entity, null);
    }

    public static ContextualTask build(Runnable runnable, Location locationContext) {
        return new ContextualTask(runnable, null, locationContext);
    }

    public static ContextualTask build(Runnable runnable, Entity entity, Location locationContext) {
        return new ContextualTask(runnable, entity, locationContext);
    }

    public Runnable getRunnable() {
        return runnable;
    }

    public Entity getEntityContext() {
        return entity;
    }

    public Location getLocationContext() {
        return locationContext;
    }

    public boolean hasEntityContext() {
        return entity != null;
    }

    public boolean hasLocationContext() {
        return locationContext != null;
    }
}
