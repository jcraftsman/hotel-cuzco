package hotel.cuzco.booking.usecase.query;

import hotel.cuzco.booking.domain.command.MakeReservationCommand;
import hotel.cuzco.booking.domain.reservation.Room;
import hotel.cuzco.booking.infrastructure.database.inmemory.RoomInMemoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

class GetAvailableRoomsQueryHandlerTest {

    private static final String NUMBER_1 = "1";
    private static final String NUMBER_2 = "2";
    private static final String NUMBER_3 = "3";
    private static final String NUMBER_4 = "4";
    private static final LocalDate DEC_25_18 = LocalDate.parse("2018-12-25");
    private static final LocalDate JAN_1ST_19 = LocalDate.parse("2019-01-01");
    private static final LocalDate JAN_2ND_19 = LocalDate.parse("2019-01-02");
    private static final int ONE_GUEST = 1;
    private static final int TWO_GUESTS = 2;

    private static Room SINGLE_ROOM;
    private static Room ROOM_FOR_2;
    private static Room ROOM_FOR_3;
    private static Room ROOM_FOR_4;

    private GetAvailableRoomsQueryHandler getAvailableRoomsQueryHandler;

    @BeforeEach
    void setUp() {
        SINGLE_ROOM = new Room(NUMBER_1, "Single room", ONE_GUEST);
        ROOM_FOR_2 = new Room(NUMBER_2, "a room for two guests", TWO_GUESTS);
        ROOM_FOR_3 = new Room(NUMBER_3, "a room for three guests", 3);
        ROOM_FOR_4 = new Room(NUMBER_4, "a room for four guests", 4);
        var ALL_ROOMS = asList(SINGLE_ROOM, ROOM_FOR_2, ROOM_FOR_3, ROOM_FOR_4);

        var roomInMemoryRepository = RoomInMemoryRepository.build();
        roomInMemoryRepository.addAll(ALL_ROOMS);

        getAvailableRoomsQueryHandler = new GetAvailableRoomsQueryHandler(roomInMemoryRepository);
    }

    @Test
    void it_returns_all_rooms_available_for_single_guest_when_there_is_no_reservation() {
        // Given
        var getAvailableRoomsQuery = new GetAvailableRoomsQuery(JAN_1ST_19, JAN_2ND_19, ONE_GUEST);

        // When
        Iterable<Room> availableRooms = getAvailableRoomsQueryHandler.handle(getAvailableRoomsQuery);

        // Then
        assertThat(availableRooms).containsExactlyInAnyOrder(SINGLE_ROOM, ROOM_FOR_2, ROOM_FOR_3, ROOM_FOR_4);
    }

    @Test
    void it_returns_only_rooms_with_enough_capacity_to_host_2_guests() {
        // Given
        var getAvailableRoomsQuery = new GetAvailableRoomsQuery(JAN_1ST_19, JAN_2ND_19, TWO_GUESTS);

        // When
        Iterable<Room> availableRooms = getAvailableRoomsQueryHandler.handle(getAvailableRoomsQuery);

        // Then
        assertThat(availableRooms).containsExactlyInAnyOrder(ROOM_FOR_2, ROOM_FOR_3, ROOM_FOR_4);
    }

    @Test
    void it_returns_only_rooms_without_any_reservation_in_the_same_period() {
        // Given
        var getAvailableRoomsQuery = new GetAvailableRoomsQuery(JAN_1ST_19, JAN_2ND_19, ONE_GUEST);
        ROOM_FOR_3.makeReservation(new MakeReservationCommand(NUMBER_3, JAN_1ST_19, JAN_2ND_19, ONE_GUEST));

        // When
        Iterable<Room> availableRooms = getAvailableRoomsQueryHandler.handle(getAvailableRoomsQuery);

        // Then
        assertThat(availableRooms).containsExactlyInAnyOrder(SINGLE_ROOM, ROOM_FOR_2, ROOM_FOR_4);
    }

    @Test
    void it_returns_all_rooms_when_their_reservations_dont_conflict_with_requested_availability_period() {
        // Given
        var getAvailableRoomsQuery = new GetAvailableRoomsQuery(JAN_1ST_19, JAN_2ND_19, ONE_GUEST);
        ROOM_FOR_2.makeReservation(new MakeReservationCommand(NUMBER_2, DEC_25_18, JAN_1ST_19, ONE_GUEST));

        // When
        Iterable<Room> availableRooms = getAvailableRoomsQueryHandler.handle(getAvailableRoomsQuery);

        // Then
        assertThat(availableRooms).containsExactlyInAnyOrder(SINGLE_ROOM, ROOM_FOR_2, ROOM_FOR_3, ROOM_FOR_4);
    }
}