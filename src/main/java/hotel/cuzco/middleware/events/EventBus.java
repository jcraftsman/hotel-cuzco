package hotel.cuzco.middleware.events;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EventBus {

    private Map<Class, ? extends List<? extends EventHandler>> eventHandlers;

    public EventBus(List<? extends EventHandler> eventHandlers) {
        this.eventHandlers = eventHandlers.stream()
                .collect(Collectors.groupingBy(EventHandler::listenTo));
    }

    public void publish(Event event) {
        eventHandlers.get(event.getClass()).forEach(handler -> handler.handle(event));
    }
}
