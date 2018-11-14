package hotel.cuzco.booking.usecase.query;

import hotel.cuzco.booking.domain.reservation.Room;
import hotel.cuzco.booking.domain.reservation.RoomRepository;

import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class GetAvailableRoomsQueryHandler {
    private final RoomRepository roomRepository;

    public GetAvailableRoomsQueryHandler(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public Iterable<Room> handle(GetAvailableRoomsQuery getAvailableRoomsQuery) {
        return streamAllRooms()
                .filter(isRoomAvailable(getAvailableRoomsQuery))
                .collect(Collectors.toList());
    }

    private Predicate<Room> isRoomAvailable(GetAvailableRoomsQuery getAvailableRoomsQuery) {
        return room -> room.isAvailableFor(
                getAvailableRoomsQuery.getNumberOfGuests(),
                getAvailableRoomsQuery.getCheckIn(),
                getAvailableRoomsQuery.getCheckOut()
        );
    }

    private Stream<Room> streamAllRooms() {
        return StreamSupport.stream(roomRepository.all().spliterator(), false);
    }

}
