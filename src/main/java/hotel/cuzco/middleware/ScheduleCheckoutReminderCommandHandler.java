package hotel.cuzco.middleware;

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
