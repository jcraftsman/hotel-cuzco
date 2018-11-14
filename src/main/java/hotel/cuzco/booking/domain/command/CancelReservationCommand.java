package hotel.cuzco.booking.domain.command;

import common.ddd.patterns.Command;
import hotel.cuzco.booking.domain.reservation.ReservationId;
import lombok.Data;

@Data
public class CancelReservationCommand implements Command {
    private final ReservationId reservationId;

    public CancelReservationCommand(ReservationId reservationId) {
        this.reservationId = reservationId;
    }

    public static CancelReservationCommand of(String reservationId) {
        return new CancelReservationCommand(new ReservationId(reservationId));
    }
}
