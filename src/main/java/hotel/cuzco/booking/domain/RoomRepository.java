package hotel.cuzco.booking.domain;

import java.util.HashMap;
import java.util.Map;

public class RoomRepository {

    Map<RoomId, Room> rooms;

    public RoomRepository() {
        this.rooms = new HashMap<>();
    }

    public Room get(RoomId roomId) {
        return rooms.get(roomId);
    }

    public void add(Room room) {
        rooms.put(room.id(),room);
    }
}
