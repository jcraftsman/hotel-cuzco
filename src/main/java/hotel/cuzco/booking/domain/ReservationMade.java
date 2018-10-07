package hotel.cuzco.booking.domain;

public class ReservationMade {
    private final ReservationId reservationId;

    public ReservationMade(Reservation reservation) {
        this.reservationId = reservation.id();
    }

    public ReservationId id() {
        return reservationId;
    }
}
