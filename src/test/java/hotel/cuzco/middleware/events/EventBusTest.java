package hotel.cuzco.middleware.events;

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

    @BeforeEach
    void setUp() {
        flowerOrdersRepository = mock(FlowerOrdersRepository.class);
        statsRepository = mock(StatsRepository.class);
        var flowerOrdersSaver = new FlowerOrdersSaver(flowerOrdersRepository);
        var flowersDeliveryOrdersStatsUpdater = new FlowersDeliveryOrdersStatsUpdater(statsRepository);
        unpublishedEventRepository = mock(AnotherEventRepository.class);
        var handlerOfAnotherEvent = new HandlerOfAnotherEvent(unpublishedEventRepository);
        eventBus = new EventBus(asList(flowerOrdersSaver, flowersDeliveryOrdersStatsUpdater, handlerOfAnotherEvent));
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

    private class HandlerOfAnotherEvent implements EventHandler<UnpublishedEvent> {
        private final AnotherEventRepository eventRepository;

        public HandlerOfAnotherEvent(AnotherEventRepository eventRepository) {
            this.eventRepository = eventRepository;
        }

        @Override
        public void handle(UnpublishedEvent event) {
            eventRepository.save(event);
        }

        @Override
        public Class listenTo() {
            return UnpublishedEvent.class;
        }
    }

    private class AnotherEventRepository {
        public void save(UnpublishedEvent event) {

        }
    }

    private class UnpublishedEvent implements Event {
    }
}