package hotel.cuzco.middleware;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ScheduleCheckoutReminderCommand implements Command {
    private LocalDate checkOutDate;

    public ScheduleCheckoutReminderCommand(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }
}
