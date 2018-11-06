package features;

import hotel.cuzco.booking.command.*;
import hotel.cuzco.booking.domain.*;
import hotel.cuzco.booking.infrastructure.database.inmemory.ReservationInMemoryRepository;
import hotel.cuzco.booking.infrastructure.database.inmemory.RoomInMemoryRepository;
import hotel.cuzco.booking.query.GetAvailableRoomsQuery;
import hotel.cuzco.booking.query.GetAvailableRoomsQueryHandler;
import hotel.cuzco.middleware.commands.CommandBusMiddleware;
import hotel.cuzco.middleware.commands.CommandResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

class BookingTest {

    private static Iterable<Room> ALL_ROOMS_IN_CUZCO_HOTEL;
    private static final int NUMBER_OF_ROOMS_IN_CUZCO_HOTEL = 12;
    private static final int ONE_GUEST = 1;
    private static final String NUMBER_101 = "101";
    private static final LocalDate SEP_1ST_18 = LocalDate.parse("2018-09-01");
    private static final LocalDate SEP_2ND_18 = LocalDate.parse("2018-09-02");

    private GetAvailableRoomsQueryHandler availableRoomsQueryHandler;
    private CommandBusMiddleware commandBus;
    private MailSender mailSender;

    @BeforeEach
    void setUp() {
        ALL_ROOMS_IN_CUZCO_HOTEL = Hotel.CUZCO().allRooms();
        var reservationRepository = new ReservationInMemoryRepository();
        var roomRepository = new RoomInMemoryRepository(reservationRepository);
        mailSender = mock(MailSender.class);
        roomRepository.addAll(ALL_ROOMS_IN_CUZCO_HOTEL);
        this.commandBus = CommandBusFactory.build(roomRepository, mailSender);
        availableRoomsQueryHandler = new GetAvailableRoomsQueryHandler(roomRepository);
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
        commandBus.dispatch(makeReservationCommand);

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
        CommandResponse<ReservationId> reservationMade = commandBus.dispatch(makeReservationCommand);
        var cancelReservationCommand = new CancelReservationCommand(reservationMade.getValue());

        // When
        commandBus.dispatch(cancelReservationCommand);

        // Then
        Iterable<Room> availableRooms = availableRoomsQueryHandler.handle(getAvailableRoomsQuery);
        assertThat(availableRooms).containsExactlyInAnyOrderElementsOf(ALL_ROOMS_IN_CUZCO_HOTEL);
    }

    @Test
    void it_sends_a_mail_after_making_reservation() {
        // Given
        var makeReservationCommand = MakeReservationCommand.builder()
                .roomNumber(NUMBER_101)
                .checkIn(SEP_1ST_18)
                .checkoutOut(SEP_2ND_18)
                .numberOfGuests(ONE_GUEST)
                .guestEmail("awesome.guest@mail.com")
                .guestName("Ibrahim Connor")
                .build();

        // When
        CommandResponse<ReservationId> commandResponse = commandBus.dispatch(makeReservationCommand);

        // Then
        then(mailSender).should().send(
                "awesome.guest@mail.com",
                "Welcome to Hotel Cuzco",
                "Dear Ibrahim Connor,\n\n" +
                        "Thank you very much for choosing Hotel Cuzco for your stay in Cusco city (from 2018-09-01 to 2018-09-02).\n" +
                        "Your reservation number is " + commandResponse.getValue().getId() + ".\n" +
                        "Hotel Cuzco staff wish you a wonderful stay in our hotel.\n\n" +
                        "Best regards,\n" +
                        "Jose\n" +
                        "Recepcion");
    }
}
