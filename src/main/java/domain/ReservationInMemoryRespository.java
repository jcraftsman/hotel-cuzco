package domain;

import java.util.HashMap;
import java.util.Map;

public class ReservationInMemoryRespository {
    private Map<ReservationId, Reservation> reservations;

    public ReservationInMemoryRespository() {
        reservations = new HashMap<>();
    }

    public Reservation get(ReservationId reservationId) {
        return reservations.get(reservationId);
    }

    public void add(Reservation reservation) {
        reservations.put(reservation.id(),reservation);
    }
}
