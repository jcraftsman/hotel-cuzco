package hotel.cuzco.booking.domain.command;

import common.ddd.patterns.Command;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class MakeReservationCommand implements Command {

    private final String roomNumber;
    private final LocalDate checkIn;
    private final LocalDate checkoutOut;
    private final int numberOfGuests;
    private String guestName;
    private String guestEmail;

    public MakeReservationCommand(String roomNumber, LocalDate checkIn, LocalDate checkoutOut, int numberOfGuests) {
        this.checkIn = checkIn;
        this.checkoutOut = checkoutOut;
        this.numberOfGuests = numberOfGuests;
        this.roomNumber = roomNumber;
    }

    public MakeReservationCommand(String roomNumber, LocalDate checkIn, LocalDate checkoutOut, int numberOfGuests, String guestName, String guestEmail) {
        this.checkIn = checkIn;
        this.checkoutOut = checkoutOut;
        this.numberOfGuests = numberOfGuests;
        this.roomNumber = roomNumber;
        this.guestEmail = guestEmail;
        this.guestName = guestName;
    }
}
