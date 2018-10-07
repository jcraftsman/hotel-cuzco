package hotel.cuzco.booking.command;

import hotel.cuzco.booking.domain.Reservation;
import hotel.cuzco.booking.domain.ReservationMade;
import hotel.cuzco.booking.domain.ReservationRepository;

public class MakeReservationCommandHandler {
    private ReservationRepository reservationRepository;

    public MakeReservationCommandHandler(ReservationRepository reservationRepository) {

        this.reservationRepository = reservationRepository;
    }

    public ReservationMade handle(MakeReservationCommand makeReservationCommand) {
        Reservation reservation = new Reservation();
        this.reservationRepository.add(reservation);
        return new ReservationMade();
    }
}
