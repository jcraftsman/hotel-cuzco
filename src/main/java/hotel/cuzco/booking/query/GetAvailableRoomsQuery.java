package hotel.cuzco.booking.query;

import lombok.Data;

import java.time.LocalDate;

@Data
public class GetAvailableRoomsQuery {
    private final LocalDate checkIn;
    private final LocalDate checkOut;
    private final int numberOfGuests;

    public GetAvailableRoomsQuery(LocalDate checkIn, LocalDate checkOut, int numberOfGuests) {
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.numberOfGuests = numberOfGuests;
    }
}
