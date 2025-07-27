package BlockDynasty.Economy.domain.events;

@FunctionalInterface
public interface EventHandler<T extends Event> {
    void handle(T event);
}