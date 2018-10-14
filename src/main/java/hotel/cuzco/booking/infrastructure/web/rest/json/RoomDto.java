package hotel.cuzco.booking.infrastructure.web.rest.json;

import hotel.cuzco.booking.domain.Room;
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
}
