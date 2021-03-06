package hotel.cuzco.booking.domain.exceptions;

public class InvalidReservationPeriodException extends RuntimeException {
    public InvalidReservationPeriodException() {
        super("check-in date should be before the check-out date.");
    }

    InvalidReservationPeriodException(String message) {
        super(message);
    }
}
