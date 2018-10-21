package hotel.cuzco.middleware.events;

import hotel.cuzco.middleware.events.fixtures.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static java.util.Arrays.asList;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

class EventBusTest {

    private EventBus eventBus;
    private FlowerOrdersRepository flowerOrdersRepository;
    private StatsRepository statsRepository;
    private AnotherEventRepository unpublishedEventRepository;
    private EventsStore eventsStore;

    @BeforeEach
    void setUp() {
        flowerOrdersRepository = mock(FlowerOrdersRepository.class);
        statsRepository = mock(StatsRepository.class);
        unpublishedEventRepository = mock(AnotherEventRepository.class);
        eventsStore = mock(EventsStore.class);
        var flowerOrdersSaver = new FlowerOrdersSaver(flowerOrdersRepository);
        var flowersDeliveryOrdersStatsUpdater = new FlowersDeliveryOrdersStatsUpdater(statsRepository);
        var handlerOfAnotherEvent = new HandlerOfAnotherEvent(unpublishedEventRepository);
        var eventsCommonBehaviourHandler = new EventsCommonBehaviourHandler(eventsStore);
        eventBus = new EventBus(asList(eventsCommonBehaviourHandler, flowerOrdersSaver, flowersDeliveryOrdersStatsUpdater, handlerOfAnotherEvent));
    }

    @Test
    void it_should_handle_the_published_event_by_all_handlers_listening_to_published_event() {
        // Given
        String flowersDeliveryOrdered = "12 Cantuta flowers delivery ordered";
        FlowersDeliveryOrdered flowersDeliveryOrderedEvent = new FlowersDeliveryOrdered(flowersDeliveryOrdered);

        // When
        eventBus.publish(flowersDeliveryOrderedEvent);

        // Then
        then(flowerOrdersRepository).should().save(flowersDeliveryOrderedEvent);
        then(statsRepository).should().save(flowersDeliveryOrderedEvent);
        then(unpublishedEventRepository).shouldHaveZeroInteractions();
    }

    @Test
    void it_should_handle_the_published_event_with_the_common_handler_only_when_no_specific_handler() {
        // Given
        var eventWithoutSpecificHandler = new EventWithoutSpecificHandler();

        // When
        eventBus.publish(eventWithoutSpecificHandler);

        // Then
        then(eventsStore).should().save(eventWithoutSpecificHandler);
        then(flowerOrdersRepository).shouldHaveZeroInteractions();
        then(statsRepository).shouldHaveZeroInteractions();
        then(unpublishedEventRepository).shouldHaveZeroInteractions();
    }

    private class UnpublishedEvent implements Event {
    }

    private class EventWithoutSpecificHandler implements Event {
    }

    private class HandlerOfAnotherEvent implements EventHandler<UnpublishedEvent> {

        private final AnotherEventRepository anotherEventRepository;

        HandlerOfAnotherEvent(AnotherEventRepository anotherEventRepository) {
            this.anotherEventRepository = anotherEventRepository;
        }

        @Override
        public void handle(UnpublishedEvent event) {
            anotherEventRepository.save(event);
        }

        @Override
        public Class listenTo() {
            return UnpublishedEvent.class;
        }
    }

    private class AnotherEventRepository {
        void save(UnpublishedEvent event) {

        }
    }
}