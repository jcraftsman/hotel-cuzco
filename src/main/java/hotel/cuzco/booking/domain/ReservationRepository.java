package hotel.cuzco.booking.domain;

import java.util.Optional;

public interface ReservationRepository {
    Optional<Reservation> get(ReservationId reservationId);

    void add(Reservation reservation);
}
