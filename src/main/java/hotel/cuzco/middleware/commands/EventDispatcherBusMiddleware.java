package hotel.cuzco.middleware.commands;

import common.ddd.patterns.*;
import hotel.cuzco.middleware.events.EventBus;

import java.util.List;

public class EventDispatcherBusMiddleware implements CommandBusMiddleware {
    private final CommandBus commandBus;
    private final EventBus eventBus;

    public EventDispatcherBusMiddleware(CommandBus commandBus, EventBus eventBus) {
        this.commandBus = commandBus;
        this.eventBus = eventBus;
    }

    public <R extends CommandResponse, C extends Command> R dispatch(C command) {
        R commandResponse = commandBus.dispatch(command);
        List<Event> events = commandResponse.getEvents();
        events.forEach(eventBus::publish);
        return commandResponse;
    }
}
