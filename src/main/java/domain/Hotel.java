package domain;

import java.util.List;

public class Hotel {
    private static final Room ROOM_101 = new Room("101", "1 king size bed - A/C - Wi-Fi - private bathroom - wheelchair accessible", 2);
    private static final Room ROOM_102 = new Room("102", "2 queen size beds - A/C - Wi-Fi - private bathroom - wheelchair accessible", 4);
    private static final Room ROOM_103 = new Room("103", "3 single beds - A/C - Wi-Fi - private bathroom - wheelchair accessible", 3);
    private static final Room ROOM_201 = new Room("201", "1 king size bed - A/C - Wi-Fi - private bathroom", 2);
    private static final Room ROOM_202 = new Room("202", "1 queen size bed - Wi-Fi - private bathroom", 2);
    private static final Room ROOM_203 = new Room("203", "1 king size bed + 3 single beds - A/C - Wi-Fi - private bathroom", 5);
    private static final Room ROOM_204 = new Room("204", "1 single bed - Wi-Fi - shared bathroom", 1);
    private static final Room ROOM_205 = new Room("205", "2 single beds - A/C - Wi-Fi - shared bathroom", 2);
    private static final Room ROOM_301 = new Room("301", "1 queen size bed - A/C - private bathroom", 2);
    private static final Room ROOM_302 = new Room("302", "2 single beds - A/C - private bathroom", 2);
    private static final Room ROOM_303 = new Room("303", "3 single beds - A/C - shared bathroom", 3);
    private static final Room ROOM_304 = new Room("304", "2 single beds - shared bathroom", 2);

    private Iterable<Room> rooms;


    private Hotel(Iterable<Room> rooms) {
        this.rooms = rooms;
    }

    public static Hotel CUZCO() {
        return new Hotel(List.of(
                ROOM_101, ROOM_102, ROOM_103,
                ROOM_201, ROOM_202, ROOM_203, ROOM_204, ROOM_205,
                ROOM_301, ROOM_302, ROOM_303, ROOM_304));
    }

    public Iterable<Room> allRooms() {
        return rooms;
    }
}
