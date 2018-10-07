package hotel.cuzco.booking.query;

import hotel.cuzco.booking.domain.Room;
import hotel.cuzco.booking.domain.RoomRepository;

import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class GetAvailableRoomsQueryHandler {
    private final RoomRepository roomRepository;

    public GetAvailableRoomsQueryHandler(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public Iterable<Room> handle(GetAvailableRoomsQuery getAvailableRoomsQuery) {
        int numberOfGuests = getAvailableRoomsQuery.getNumberOfGuests();
        return streamAllRooms()
                .filter(room -> room.isAvailableFor(numberOfGuests))
                .collect(Collectors.toList());
    }

    private Stream<Room> streamAllRooms() {
        return StreamSupport.stream(roomRepository.all().spliterator(), false);
    }

}
