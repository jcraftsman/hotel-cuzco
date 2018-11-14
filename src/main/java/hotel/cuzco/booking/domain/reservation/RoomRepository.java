package hotel.cuzco.booking.domain.reservation;

import java.util.Optional;

public interface RoomRepository {
    Room get(RoomId roomId);

    void save(Room room);

    Iterable<Room> all();

    Optional<Room> getByReservation(ReservationId reservationId);
}
