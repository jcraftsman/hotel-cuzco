package hotel.cuzco.booking.domain;

public class UnavailableForReservationException extends RuntimeException {
    public UnavailableForReservationException() {
        super("Requested room is unavailable during the reservation period.");
    }
}
