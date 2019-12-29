package hotel.cuzco.booking.infrastructure.web.rest.json;

import com.eclipsesource.json.Json;
import hotel.cuzco.booking.domain.reservation.Room;
import lombok.Data;

@Data
public class RoomDto {
    private final String roomNumber;
    private final String description;
    private final int capacity;

    public RoomDto(Room room) {
        this.roomNumber = room.getRoomId().getRoomNumber();
        this.description = room.getDescription();
        this.capacity = room.getCapacity();
    }

    @Override
    public String toString() {
        return Json.object()
                .add("room-number", roomNumber)
                .add("description", description)
                .add("capacity", capacity)
                .toString();
    }
}
