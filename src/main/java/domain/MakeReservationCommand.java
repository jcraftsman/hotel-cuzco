package domain;

import lombok.Data;

import java.time.LocalDate;

@Data
public class MakeReservationCommand {

    private final LocalDate checkIn;
    private final LocalDate checkoutOut;
    private final int numberOfGuests;
    private final String roomNumber;

    public MakeReservationCommand(String roomNumber, LocalDate checkIn, LocalDate checkoutOut, int numberOfGuests) {
        this.checkIn = checkIn;
        this.checkoutOut = checkoutOut;
        this.numberOfGuests = numberOfGuests;
        this.roomNumber = roomNumber;
    }
}
