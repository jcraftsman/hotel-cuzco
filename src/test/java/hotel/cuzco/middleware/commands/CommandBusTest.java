package hotel.cuzco.middleware.commands;

import hotel.cuzco.middleware.commands.fixtures.OrderFlowersDeliveryCommand;
import hotel.cuzco.middleware.commands.fixtures.OrderFlowersDeliveryCommandHandler;
import hotel.cuzco.middleware.commands.fixtures.ScheduleCheckoutReminderCommand;
import hotel.cuzco.middleware.commands.fixtures.ScheduleCheckoutReminderCommandHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

class CommandBusTest {

    private CommandBus commandBus;

    @BeforeEach
    void setUp() {
        List<CommandHandler> commandHandlers = asList(
                new OrderFlowersDeliveryCommandHandler(),
                new ScheduleCheckoutReminderCommandHandler());
        commandBus = new CommandBus(commandHandlers);
    }

    @Test
    void it_should_dispatch_to_OrderFlowersDeliveryCommandHandler() {
        // Given
        var orderCantutaDeliveryCommand = new OrderFlowersDeliveryCommand(12, "Cantuta");

        // When
        var dispatchedCommandResponse = commandBus.dispatch(orderCantutaDeliveryCommand);

        // Then
        assertThat(dispatchedCommandResponse).isEqualTo("12 Cantuta flowers delivery ordered");
    }

    @Test
    void it_should_should_dispatch_to_ScheduleCheckoutReminderCommandHandler() {
        // Given
        var checkOutDate = LocalDate.parse("2019-02-05");
        var scheduleCheckoutReminderCommand = new ScheduleCheckoutReminderCommand(checkOutDate);

        // When
        var dispatchedCommandResponse = commandBus.dispatch(scheduleCheckoutReminderCommand);

        // Then
        assertThat(dispatchedCommandResponse).isEqualTo("Checkout reminder scheduled for 2019-02-05");
    }
}