package hotel.cuzco.booking.command;

import hotel.cuzco.booking.domain.ReservationId;
import lombok.Data;

@Data
public class CancelReservationCommand {
    private final ReservationId reservationId;

    public CancelReservationCommand(ReservationId reservationId) {
        this.reservationId = reservationId;
    }

    public static CancelReservationCommand of(String reservationId) {
        return new CancelReservationCommand(new ReservationId(reservationId));
    }
}