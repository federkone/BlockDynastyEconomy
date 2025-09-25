package BlockDynasty.Economy.domain.events;

import java.time.Instant;

public abstract class Event implements CancelableEvent {
    private boolean cancelled = false;
    private final String date;

    public Event(){
        this.date = Instant.now().toString();
    }

    public String getDate() {
        return date;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public boolean isCancelled() {
        return cancelled;
    }
}