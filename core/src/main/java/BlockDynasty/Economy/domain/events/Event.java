package BlockDynasty.Economy.domain.events;

public abstract class Event implements CancelableEvent {
    private boolean cancelled = false;

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public boolean isCancelled() {
        return cancelled;
    }
}