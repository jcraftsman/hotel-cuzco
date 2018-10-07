package hotel.cuzco.booking.command;

import hotel.cuzco.booking.domain.Reservation;
import hotel.cuzco.booking.infrastructure.ReservationInMemoryRespository;
import hotel.cuzco.booking.domain.ReservationMade;

public class MakeReservationCommandHandler {
    private ReservationInMemoryRespository reservationRespository;

    public MakeReservationCommandHandler(ReservationInMemoryRespository reservationRespository) {

        this.reservationRespository = reservationRespository;
    }

    public ReservationMade handle(MakeReservationCommand makeReservationCommand) {
        Reservation reservation = new Reservation();
        this.reservationRespository.add(reservation);
        return new ReservationMade();
    }
}
