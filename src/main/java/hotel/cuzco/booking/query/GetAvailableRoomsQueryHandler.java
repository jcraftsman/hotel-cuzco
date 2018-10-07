package hotel.cuzco.booking.query;

import hotel.cuzco.booking.domain.Room;
import hotel.cuzco.booking.domain.RoomRepository;

public class GetAvailableRoomsQueryHandler {
    private final RoomRepository roomRepository;

    public GetAvailableRoomsQueryHandler(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public Iterable<Room> handle(GetAvailableRoomsQuery getAvailableRoomsQuery) {
        return roomRepository.all();
    }
}
