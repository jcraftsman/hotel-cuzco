package hotel.cuzco.booking.domain;

import lombok.Getter;

@Getter
public class Room {
    private RoomId roomId;
    private String description;
    private int capacity;

    public Room(String number, String description, int capacity) {
        this.roomId = new RoomId(number);
        this.description = description;
        this.capacity = capacity;
    }

    public RoomId id() {
        return this.roomId;
    }

    public boolean isAvailableFor(int numberOfGuests) {
        return getCapacity() >= numberOfGuests;
    }
}
