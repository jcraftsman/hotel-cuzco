package hotel.cuzco.booking.infrastructure.database.inmemory;

import hotel.cuzco.booking.domain.reservation.Reservation;
import hotel.cuzco.booking.domain.reservation.ReservationId;
import hotel.cuzco.booking.domain.reservation.ReservationRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ReservationInMemoryRepository implements ReservationRepository {
    private Map<ReservationId, Reservation> reservations;

    public ReservationInMemoryRepository() {
        reservations = new HashMap<>();
    }

    @Override
    public Optional<Reservation> get(ReservationId reservationId) {
        return Optional.ofNullable(reservations.get(reservationId));
    }

    @Override
    public void add(Reservation reservation) {
        reservations.put(reservation.id(), reservation);
    }
}
