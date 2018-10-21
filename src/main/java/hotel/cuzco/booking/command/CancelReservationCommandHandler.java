package hotel.cuzco.booking.command;

import hotel.cuzco.booking.domain.Reservation;
import hotel.cuzco.booking.domain.ReservationCanceled;
import hotel.cuzco.booking.domain.ReservationId;
import hotel.cuzco.booking.domain.ReservationRepository;

public class CancelReservationCommandHandler {
    private final ReservationRepository reservationRepository;

    public CancelReservationCommandHandler(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public ReservationCanceled handle(CancelReservationCommand cancelReservationCommand) {
        ReservationId reservationId = cancelReservationCommand.getReservationId();
        Reservation reservation = reservationRepository.get(reservationId).orElseThrow(InvalidCommandException::new);
        reservation.room().cancelReservation(reservationId);
        return new ReservationCanceled(reservationId);
    }
}
