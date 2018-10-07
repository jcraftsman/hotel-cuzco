package hotel.cuzco.booking.query;

import hotel.cuzco.booking.domain.Room;
import hotel.cuzco.booking.infrastructure.RoomInMemoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

class GetAvailableRoomsQueryTest {

    private static final LocalDate JAN_1ST_19 = LocalDate.parse("2019-01-01");
    private static final LocalDate JAN_2ND_19 = LocalDate.parse("2019-01-02");
    private static final Room SINGLE_ROOM = new Room("1", "Single room", 1);
    private static final List<Room> ALL_ROOMS = asList(SINGLE_ROOM);

    private GetAvailableRoomsQueryHandler getAvailableRoomsQueryHandler;

    @BeforeEach
    void setUp() {
        RoomInMemoryRepository roomInMemoryRepository = new RoomInMemoryRepository();
        roomInMemoryRepository.addAll(ALL_ROOMS);

        getAvailableRoomsQueryHandler = new GetAvailableRoomsQueryHandler(roomInMemoryRepository);
    }

    @Test
    void it_returns_all_rooms_available_for_single_guest_when_there_is_no_reservation() {
        // Given
        int numberOfGuests = 1;
        var getAvailableRoomsQuery = new GetAvailableRoomsQuery(JAN_1ST_19, JAN_2ND_19, numberOfGuests);

        // When
        Iterable<Room> availableRooms = getAvailableRoomsQueryHandler.handle(getAvailableRoomsQuery);

        // Then
        assertThat(availableRooms).containsExactlyInAnyOrderElementsOf(ALL_ROOMS);
    }
}