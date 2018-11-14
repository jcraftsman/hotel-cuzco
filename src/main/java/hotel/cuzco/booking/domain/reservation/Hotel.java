package hotel.cuzco.booking.domain.reservation;

import java.util.List;

public class Hotel {
    static final String CUZCO_HOTEL_ID = "CUZCO";

    private Iterable<Room> rooms;
    private String hotelId;


    private Hotel(String hotelId, Iterable<Room> rooms) {
        this.rooms = rooms;
        this.hotelId = hotelId;
    }

    public static Hotel CUZCO() {
        return new Hotel(CUZCO_HOTEL_ID, roomsInCuzcoHotel());
    }

    private static List<Room> roomsInCuzcoHotel() {
        return List.of(
                new Room("101", "1 king size bed - A/C - Wi-Fi - private bathroom - wheelchair accessible", 2),
                new Room("102", "2 queen size beds - A/C - Wi-Fi - private bathroom - wheelchair accessible", 4),
                new Room("103", "3 single beds - A/C - Wi-Fi - private bathroom - wheelchair accessible", 3),
                new Room("201", "1 king size bed - A/C - Wi-Fi - private bathroom", 2),
                new Room("202", "1 queen size bed - Wi-Fi - private bathroom", 2),
                new Room("203", "1 king size bed + 3 single beds - A/C - Wi-Fi - private bathroom", 5),
                new Room("204", "1 single bed - Wi-Fi - shared bathroom", 1),
                new Room("205", "2 single beds - A/C - Wi-Fi - shared bathroom", 2),
                new Room("301", "1 queen size bed - A/C - private bathroom", 2),
                new Room("302", "2 single beds - A/C - private bathroom", 2),
                new Room("303", "3 single beds - A/C - shared bathroom", 3),
                new Room("304", "2 single beds - shared bathroom", 2)
        );
    }

    public Iterable<Room> allRooms() {
        return rooms;
    }

    public String id() {
        return hotelId;
    }
}
