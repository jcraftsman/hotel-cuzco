package hotel.cuzco.booking.domain;

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
}
