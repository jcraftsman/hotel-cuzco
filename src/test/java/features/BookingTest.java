package features;

import hotel.cuzco.booking.command.CancelReservationCommand;
import hotel.cuzco.booking.command.CancelReservationCommandHandler;
import hotel.cuzco.booking.command.MakeReservationCommand;
import hotel.cuzco.booking.command.MakeReservationCommandHandler;
import hotel.cuzco.booking.domain.Hotel;
import hotel.cuzco.booking.domain.Room;
import hotel.cuzco.booking.infrastructure.database.inmemory.ReservationInMemoryRepository;
import hotel.cuzco.booking.infrastructure.database.inmemory.RoomInMemoryRepository;
import hotel.cuzco.booking.query.GetAvailableRoomsQuery;
import hotel.cuzco.booking.query.GetAvailableRoomsQueryHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class BookingTest {

    private static Iterable<Room> ALL_ROOMS_IN_CUZCO_HOTEL;
    private static final int NUMBER_OF_ROOMS_IN_CUZCO_HOTEL = 12;
    private static final int ONE_GUEST = 1;
    private static final String NUMBER_101 = "101";
    private static final LocalDate SEP_1ST_18 = LocalDate.parse("2018-09-01");
    private static final LocalDate SEP_2ND_18 = LocalDate.parse("2018-09-02");

    private MakeReservationCommandHandler makeReservationCommandHandler;
    private GetAvailableRoomsQueryHandler availableRoomsQueryHandler;
    private CancelReservationCommandHandler cancelReservationCommandHandler;

    @BeforeEach
    void setUp() {
        ALL_ROOMS_IN_CUZCO_HOTEL = Hotel.CUZCO().allRooms();
        var reservationRepository = new ReservationInMemoryRepository();
        var roomRepository = new RoomInMemoryRepository(reservationRepository);
        roomRepository.addAll(ALL_ROOMS_IN_CUZCO_HOTEL);
        makeReservationCommandHandler = new MakeReservationCommandHandler(roomRepository);
        availableRoomsQueryHandler = new GetAvailableRoomsQueryHandler(roomRepository);
        cancelReservationCommandHandler = new CancelReservationCommandHandler(reservationRepository);
    }

    @Test
    void it_returns_all_rooms_available_for_1_night_stay_for_1_guest() {
        // Given
        var getAvailableRoomsQuery = new GetAvailableRoomsQuery(SEP_1ST_18, SEP_2ND_18, ONE_GUEST);

        // When
        Iterable<Room> availableRooms = availableRoomsQueryHandler.handle(getAvailableRoomsQuery);

        // Then
        assertThat(availableRooms).containsExactlyInAnyOrderElementsOf(ALL_ROOMS_IN_CUZCO_HOTEL);
    }

    @Test
    void it_makes_a_room_unavailable_when_already_booked() {
        // Given
        var getAvailableRoomsQuery = new GetAvailableRoomsQuery(SEP_1ST_18, SEP_2ND_18, ONE_GUEST);
        var makeReservationCommand = new MakeReservationCommand(NUMBER_101, SEP_1ST_18, SEP_2ND_18, ONE_GUEST);

        // When
        makeReservationCommandHandler.handle(makeReservationCommand);

        // Then
        Iterable<Room> availableRooms = availableRoomsQueryHandler.handle(getAvailableRoomsQuery);
        int remainingAvailableRoomsAfterBookingOne = NUMBER_OF_ROOMS_IN_CUZCO_HOTEL - 1;
        assertThat(availableRooms).hasSize(remainingAvailableRoomsAfterBookingOne);
    }

    @Test
    void it_returns_all_available_rooms_ignoring_canceled_reservations() {
        // Given
        var getAvailableRoomsQuery = new GetAvailableRoomsQuery(SEP_1ST_18, SEP_2ND_18, ONE_GUEST);
        var makeReservationCommand = new MakeReservationCommand(NUMBER_101, SEP_1ST_18, SEP_2ND_18, ONE_GUEST);
        var reservationMade = makeReservationCommandHandler.handle(makeReservationCommand);
        var cancelReservationCommand = new CancelReservationCommand(reservationMade.getValue());

        // When
        cancelReservationCommandHandler.handle(cancelReservationCommand);

        // Then
        Iterable<Room> availableRooms = availableRoomsQueryHandler.handle(getAvailableRoomsQuery);
        assertThat(availableRooms).containsExactlyInAnyOrderElementsOf(ALL_ROOMS_IN_CUZCO_HOTEL);
    }
}
