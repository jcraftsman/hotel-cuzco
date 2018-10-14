package hotel.cuzco.booking.infrastructure.database.inmemory;

import hotel.cuzco.booking.domain.Reservation;
import hotel.cuzco.booking.domain.ReservationId;
import hotel.cuzco.booking.domain.ReservationRepository;

import java.util.HashMap;
import java.util.Map;

public class ReservationInMemoryRepository implements ReservationRepository {
    private Map<ReservationId, Reservation> reservations;

    public ReservationInMemoryRepository() {
        reservations = new HashMap<>();
    }

    @Override
    public Reservation get(ReservationId reservationId) {
        return reservations.get(reservationId);
    }

    @Override
    public void add(Reservation reservation) {
        reservations.put(reservation.id(), reservation);
    }
}
