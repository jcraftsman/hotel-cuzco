package hotel.cuzco.booking.infrastructure;

import hotel.cuzco.booking.domain.Room;
import hotel.cuzco.booking.domain.RoomId;
import hotel.cuzco.booking.domain.RoomRepository;

import java.util.HashMap;
import java.util.Map;

public class RoomInMemoryRepository implements RoomRepository {

    Map<RoomId, Room> rooms;

    public RoomInMemoryRepository() {
        this.rooms = new HashMap<>();
    }

    @Override
    public Room get(RoomId roomId) {
        return rooms.get(roomId);
    }

    @Override
    public void add(Room room) {
        rooms.put(room.id(), room);
    }

    @Override
    public Iterable<Room> all() {
        return rooms.values();
    }

    public void addAll(Iterable<Room> allRooms) {
        allRooms.forEach(this::add);
    }
}
