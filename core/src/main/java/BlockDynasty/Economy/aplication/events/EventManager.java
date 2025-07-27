package BlockDynasty.Economy.aplication.events;

import BlockDynasty.Economy.domain.events.Event;
import BlockDynasty.Economy.domain.events.EventHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventManager {
    private final Map<Class<? extends Event>, List<EventHandler<?>>> handlers = new HashMap<>();

    public <T extends Event> void subscribe(Class<T> eventType, EventHandler<T> handler) {
        handlers.computeIfAbsent(eventType, k -> new ArrayList<>()).add(handler);
    }

    public <T extends Event> void unsubscribe(Class<T> eventType, EventHandler<T> handler) {
        List<EventHandler<?>> eventHandlers = handlers.get(eventType);
        if (eventHandlers != null) {
            eventHandlers.remove(handler);
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends Event> void emit(T event) {
        List<EventHandler<?>> eventHandlers = handlers.get(event.getClass());
        if (eventHandlers != null) {
            for (EventHandler<?> handler : new ArrayList<>(eventHandlers)) {
                ((EventHandler<T>) handler).handle(event);
                if (event.isCancelled()) break; // Opcional: cortar propagación
            }
        }
    }
}