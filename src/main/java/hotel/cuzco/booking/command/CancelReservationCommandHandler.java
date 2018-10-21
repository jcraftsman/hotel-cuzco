package hotel.cuzco.booking.command;

import hotel.cuzco.booking.domain.*;

public class CancelReservationCommandHandler {
    private final ReservationRepository reservationRepository;

    public CancelReservationCommandHandler(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public ReservationCanceled handle(CancelReservationCommand cancelReservationCommand) {
        ReservationId reservationId = cancelReservationCommand.getReservationId();
        Reservation reservation = reservationRepository.get(reservationId);
        reservation.room().cancelReservation(reservationId);
        return new ReservationCanceled(reservationId);
    }
}
