package features;

import hotel.cuzco.booking.command.MakeReservationCommand;
import hotel.cuzco.booking.command.MakeReservationCommandHandler;
import hotel.cuzco.booking.domain.Hotel;
import hotel.cuzco.booking.domain.Room;
import hotel.cuzco.booking.infrastructure.ReservationInMemoryRepository;
import hotel.cuzco.booking.infrastructure.RoomInMemoryRepository;
import hotel.cuzco.booking.query.GetAvailableRoomsQuery;
import hotel.cuzco.booking.query.GetAvailableRoomsQueryHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class BookingTest {

    private static final LocalDate SEP_1ST_18 = LocalDate.parse("2018-09-01");
    private static final LocalDate SEP_2ND_18 = LocalDate.parse("2018-09-02");

    private MakeReservationCommandHandler makeReservationCommandHandler;
    private GetAvailableRoomsQueryHandler availableRoomsQueryHandler;

    @BeforeEach
    void setUp() {
        RoomInMemoryRepository roomRepository = new RoomInMemoryRepository();
        roomRepository.addAll(Hotel.CUZCO().allRooms());
        makeReservationCommandHandler = new MakeReservationCommandHandler(new ReservationInMemoryRepository(), roomRepository);
        availableRoomsQueryHandler = new GetAvailableRoomsQueryHandler(roomRepository);
    }

    @Test
    void it_returns_all_rooms_available_for_1_night_stay_for_1_guest() {
        // Given
        var numberOfGuests = 1;
        var getAvailableRoomsQuery = new GetAvailableRoomsQuery(SEP_1ST_18, SEP_2ND_18, numberOfGuests);

        // When
        Iterable<Room> availableRooms = availableRoomsQueryHandler.handle(getAvailableRoomsQuery);

        // Then
        assertThat(availableRooms).containsExactlyInAnyOrderElementsOf(Hotel.CUZCO().allRooms());
    }

    @Test
    void it_makes_a_room_unavailable_when_already_booked() {
        // Given
        var numberOfGuests = 1;
        var getAvailableRoomsQuery = new GetAvailableRoomsQuery(SEP_1ST_18, SEP_2ND_18, numberOfGuests);
        String roomNumber = "101";
        var makeReservationCommand = new MakeReservationCommand(roomNumber, SEP_1ST_18, SEP_2ND_18, numberOfGuests);

        // When
        makeReservationCommandHandler.handle(makeReservationCommand);

        // Then
        Iterable<Room> availableRooms = availableRoomsQueryHandler.handle(getAvailableRoomsQuery);
        assertThat(availableRooms).hasSize(11);
    }
}
