package hotel.cuzco.middleware.commands.fixtures;

import common.ddd.patterns.Command;
import common.ddd.patterns.CommandHandler;
import common.ddd.patterns.CommandResponse;
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
    public Class<? extends Command> listenTo() {
        return ScheduleCheckoutReminderCommand.class;
    }
}
