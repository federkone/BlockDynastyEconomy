package BlockDynasty.BukkitImplementation.scheduler;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ContextualTask {
    private final Runnable runnable;
    private final Player playerContext;
    private final Location locationContext;

    public ContextualTask(Runnable runnable) {
        this(runnable, null, null);
    }

    public ContextualTask(Runnable runnable, Player playerContext) {
        this(runnable, playerContext, null);
    }

    public ContextualTask(Runnable runnable, Location locationContext) {
        this(runnable, null, locationContext);
    }

    private ContextualTask(Runnable runnable, Player playerContext, Location locationContext) {
        this.runnable = runnable;
        this.playerContext = playerContext;
        this.locationContext = locationContext;
    }

    public Runnable getRunnable() {
        return runnable;
    }

    public Player getPlayerContext() {
        return playerContext;
    }

    public Location getLocationContext() {
        return locationContext;
    }

    public boolean hasPlayerContext() {
        return playerContext != null;
    }

    public boolean hasLocationContext() {
        return locationContext != null;
    }
}
