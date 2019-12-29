package hotel.cuzco.middleware.commands;

import common.ddd.patterns.CommandHandler;
import hotel.cuzco.middleware.commands.fixtures.OrderFlowersDeliveryCommand;
import hotel.cuzco.middleware.commands.fixtures.OrderFlowersDeliveryCommandHandler;
import hotel.cuzco.middleware.commands.fixtures.ScheduleCheckoutReminderCommand;
import hotel.cuzco.middleware.commands.fixtures.ScheduleCheckoutReminderCommandHandler;
import hotel.cuzco.middleware.events.EventBus;
import hotel.cuzco.middleware.events.fixtures.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

class EventDispatcherBusMiddlewareTest {

    private EventDispatcherBusMiddleware eventDispatcherBusMiddleware;

    private FlowerOrdersRepository flowerOrdersRepository;
    private StatsRepository statsRepository;

    @BeforeEach
    void setUp() {
        List<CommandHandler<?, ?>> commandHandlers = asList(
                new OrderFlowersDeliveryCommandHandler(),
                new ScheduleCheckoutReminderCommandHandler());
        var commandDispatcher = new CommandDispatcher(commandHandlers);
        flowerOrdersRepository = mock(FlowerOrdersRepository.class);
        statsRepository = mock(StatsRepository.class);
        var flowerOrdersSaver = new FlowerOrdersSaver(flowerOrdersRepository);
        var flowersDeliveryOrdersStatsUpdater = new FlowersDeliveryOrdersStatsUpdater(statsRepository);
        var eventBus = new EventBus(asList(flowerOrdersSaver, flowersDeliveryOrdersStatsUpdater));
        eventDispatcherBusMiddleware = new EventDispatcherBusMiddleware(commandDispatcher, eventBus);
    }

    @Test
    void it_should_handle_each_event_of_the_handled_command_response() {
        // Given
        var orderCameliaDeliveryCommand = new OrderFlowersDeliveryCommand(4, "Camelia");

        // When
        var dispatchedCommandResponse = eventDispatcherBusMiddleware.dispatch(orderCameliaDeliveryCommand);

        // Then
        var flowersDeliveryOrderedEvent = new FlowersDeliveryOrdered("4 Camelia flowers delivery ordered");
        assertThat(dispatchedCommandResponse.getEvents()).containsExactly(flowersDeliveryOrderedEvent);
        then(flowerOrdersRepository).should().save(flowersDeliveryOrderedEvent);
        then(statsRepository).should().save(flowersDeliveryOrderedEvent);
    }

    @Test
    void it_should_return_the_handled_command_response() {
        // Given
        var checkOutDate = LocalDate.parse("2020-02-01");
        var scheduleCheckoutReminderCommand = new ScheduleCheckoutReminderCommand(checkOutDate);

        // When
        var dispatchedCommandResponse = eventDispatcherBusMiddleware.dispatch(scheduleCheckoutReminderCommand);

        // Then
        var event = new CheckoutReminderScheduled("Checkout reminder scheduled for 2020-02-01");
        assertThat(dispatchedCommandResponse.getEvents()).containsExactly(event);
    }
}