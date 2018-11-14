package hotel.cuzco.middleware.events;

import common.ddd.patterns.Event;
import common.ddd.patterns.EventHandler;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EventBus {

    private final Map<Class, ? extends List<? extends EventHandler>> eventHandlers;

    public EventBus(List<? extends EventHandler> eventHandlers) {
        this.eventHandlers = eventHandlers.stream()
                .collect(Collectors.groupingBy(EventHandler::listenTo));
    }

    public void publish(Event event) {
        List<? extends EventHandler> eventHandlers = getEventHandlers(event);
        eventHandlers.forEach(handler -> handler.handle(event));
    }

    private List<? extends EventHandler> getEventHandlers(Event event) {
        return this.eventHandlers.entrySet().stream()
                .filter(entry -> entry.getKey().isInstance(event))
                .flatMap(classEntry -> classEntry.getValue().stream())
                .collect(Collectors.toList());
    }
}
