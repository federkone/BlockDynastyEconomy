package BlockDynasty.Economy.domain.events;

public interface CancelableEvent {

     void setCancelled(boolean cancelled);
     boolean isCancelled();
}
