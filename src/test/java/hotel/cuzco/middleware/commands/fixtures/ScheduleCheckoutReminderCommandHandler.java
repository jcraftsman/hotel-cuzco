package hotel.cuzco.middleware.commands.fixtures;

import hotel.cuzco.middleware.commands.CommandHandler;
import hotel.cuzco.middleware.commands.CommandResponse;
import hotel.cuzco.middleware.events.fixtures.CheckoutReminderScheduled;

import java.util.UUID;

public class ScheduleCheckoutReminderCommandHandler
        implements CommandHandler<CommandResponse<String>, ScheduleCheckoutReminderCommand> {

    @Override
    public CommandResponse<String> handle(ScheduleCheckoutReminderCommand command) {
        var reminderScheduleInfo = "Checkout reminder scheduled for " + command.getCheckOutDate().toString();
        return CommandResponse.<String>builder()
                .value(UUID.randomUUID().toString())
                .event(new CheckoutReminderScheduled(reminderScheduleInfo))
                .build();
    }

    @Override
    public Class listenTo() {
        return ScheduleCheckoutReminderCommand.class;
    }
}
