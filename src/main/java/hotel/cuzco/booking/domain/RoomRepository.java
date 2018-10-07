package hotel.cuzco.booking.domain;

public interface RoomRepository {
    Room get(RoomId roomId);

    void add(Room room);

    Iterable<Room> all();
}
