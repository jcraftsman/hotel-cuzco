package hotel.cuzco.booking.domain;

import lombok.Data;

@Data
public class ReservationCanceled {
    private final ReservationId reservationId;

    public ReservationCanceled(ReservationId reservationId) {
        this.reservationId = reservationId;
    }
}
