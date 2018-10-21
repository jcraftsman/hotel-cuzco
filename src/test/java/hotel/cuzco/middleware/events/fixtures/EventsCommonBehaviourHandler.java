package hotel.cuzco.middleware.events.fixtures;

import hotel.cuzco.middleware.events.Event;
import hotel.cuzco.middleware.events.EventHandler;

public class EventsCommonBehaviourHandler implements EventHandler<Event> {
    private final EventsStore eventsStore;

    public EventsCommonBehaviourHandler(EventsStore eventsStore) {
        this.eventsStore = eventsStore;
    }

    @Override
    public void handle(Event event) {
        eventsStore.save(event);
    }

    @Override
    public Class listenTo() {
        return Event.class;
    }
}
