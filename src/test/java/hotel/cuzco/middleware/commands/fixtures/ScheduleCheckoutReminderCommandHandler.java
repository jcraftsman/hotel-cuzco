package hotel.cuzco.middleware.commands.fixtures;

import hotel.cuzco.middleware.commands.CommandHandler;

public class ScheduleCheckoutReminderCommandHandler implements CommandHandler<String,ScheduleCheckoutReminderCommand> {

    @Override
    public String handle(ScheduleCheckoutReminderCommand command) {
        return "Checkout reminder scheduled for " + command.getCheckOutDate().toString();
    }

    @Override
    public Class listenTo() {
        return ScheduleCheckoutReminderCommand.class;
    }
}
