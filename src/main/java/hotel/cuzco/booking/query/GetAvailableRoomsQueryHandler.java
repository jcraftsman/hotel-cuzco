package hotel.cuzco.booking.query;

import hotel.cuzco.booking.domain.Hotel;
import hotel.cuzco.booking.domain.Room;

public class GetAvailableRoomsQueryHandler {
    public Iterable<Room> handle(GetAvailableRoomsQuery getAvailableRoomsQuery) {
       return Hotel.CUZCO().allRooms();
    }
}
