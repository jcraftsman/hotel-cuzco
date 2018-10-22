package hotel.cuzco.booking.domain;

import java.util.Optional;

public interface RoomRepository {
    Room get(RoomId roomId);

    void add(Room room);

    Iterable<Room> all();

    Optional<Room> getByReservation(ReservationId reservationId);
}
