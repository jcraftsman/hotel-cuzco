package hotel.cuzco.booking.command;

import hotel.cuzco.middleware.commands.Command;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class MakeReservationCommand implements Command {

    private final LocalDate checkIn;
    private final LocalDate checkoutOut;
    private final int numberOfGuests;
    private final String roomNumber;
    private String guestEmail;
    private String guestName;

    public MakeReservationCommand(String roomNumber, LocalDate checkIn, LocalDate checkoutOut, int numberOfGuests) {
        this.checkIn = checkIn;
        this.checkoutOut = checkoutOut;
        this.numberOfGuests = numberOfGuests;
        this.roomNumber = roomNumber;
    }

    public MakeReservationCommand(LocalDate checkIn, LocalDate checkoutOut, int numberOfGuests, String roomNumber, String guestEmail, String guestName) {
        this.checkIn = checkIn;
        this.checkoutOut = checkoutOut;
        this.numberOfGuests = numberOfGuests;
        this.roomNumber = roomNumber;
        this.guestEmail = guestEmail;
        this.guestName = guestName;
    }
}
