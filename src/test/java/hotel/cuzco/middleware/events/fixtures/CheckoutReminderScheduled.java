package hotel.cuzco.middleware.events.fixtures;

import hotel.cuzco.middleware.events.Event;
import lombok.Data;

@Data
public class CheckoutReminderScheduled implements Event {

    private final String reminderInformation;

    public CheckoutReminderScheduled(String reminderInformation) {
        this.reminderInformation = reminderInformation;
    }
}
