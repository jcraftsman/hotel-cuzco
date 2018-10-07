package hotel.cuzco.booking.domain;

public interface ReservationRepository {
    Reservation get(ReservationId reservationId);

    void add(Reservation reservation);
}
