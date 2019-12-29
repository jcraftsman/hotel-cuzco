package hotel.cuzco.booking.domain.exceptions;

public class OneNightReservationIsNotAllowedException extends InvalidReservationPeriodException {
    public OneNightReservationIsNotAllowedException() {
        super("Cannot checkout the same day. the reservation period should be at least for one night.");
    }
}
