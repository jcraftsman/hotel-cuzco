package hotel.cuzco.booking.domain;

import hotel.cuzco.middleware.events.Event;
import lombok.Data;

@Data
public class ReservationCanceled implements Event {
    private final ReservationId reservationId;

    public ReservationCanceled(ReservationId reservationId) {
        this.reservationId = reservationId;
    }
}
