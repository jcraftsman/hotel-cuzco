package domain;

public class GetAvailableRoomsQueryHandler {
    public Iterable<Room> handle(GetAvailableRoomsQuery getAvailableRoomsQuery) {
       return Hotel.CUZCO().allRooms();
    }
}
