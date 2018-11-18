package hotel.cuzco.booking.domain.reservation;

import lombok.Data;

import java.util.UUID;

@Data
public class ReservationId {
    private final String id;

    public ReservationId() {
        this.id = UUID.randomUUID().toString();
    }

    public ReservationId(String id) {
        this.id = id;
    }

    public static ReservationId random() {
        return new ReservationId();
    }

    @Override
    public String toString() {
        return id;
    }
}
