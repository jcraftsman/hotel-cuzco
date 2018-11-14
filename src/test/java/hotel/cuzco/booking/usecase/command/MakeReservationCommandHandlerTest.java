package hotel.cuzco.booking.usecase.command;

import hotel.cuzco.booking.domain.reservation.Room;
import hotel.cuzco.booking.domain.reservation.RoomRepository;
import hotel.cuzco.booking.domain.command.MakeReservationCommand;
import hotel.cuzco.booking.domain.exceptions.UnavailableForReservationException;
import hotel.cuzco.booking.domain.reservation.ReservationPeriod;
import hotel.cuzco.booking.domain.reservation.ReservationRepository;
import hotel.cuzco.booking.infrastructure.database.inmemory.ReservationInMemoryRepository;
import hotel.cuzco.booking.infrastructure.database.inmemory.RoomInMemoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class MakeReservationCommandHandlerTest {

    private static final LocalDate SEP_1ST_18 = LocalDate.parse("2018-09-01");
    private static final LocalDate SEP_2ND_18 = LocalDate.parse("2018-09-02");
    private static final int ONE_GUEST = 1;
    private static final int TWO_GUESTS = 2;
    private static final int FIVE_GUESTS = 5;
    private static final String NUMBER_101 = "101";
    private static final String NUMBER_102 = "102";

    private ReservationRepository reservationRepository;
    private MakeReservationCommandHandler makeReservationCommandHandler;
    private RoomRepository roomRepository;

    @BeforeEach
    void setUp() {
        reservationRepository = new ReservationInMemoryRepository();
        roomRepository = new RoomInMemoryRepository(reservationRepository);
        makeReservationCommandHandler = new MakeReservationCommandHandler(roomRepository);
    }

    @Test
    void it_saves_the_reservation_made() {
        // Given
        Room room101 = givenRoom(NUMBER_101, "The room 101", TWO_GUESTS);
        var makeReservationCommand = new MakeReservationCommand(NUMBER_101, SEP_1ST_18, SEP_2ND_18, ONE_GUEST);

        // When
        var reservationMade = makeReservationCommandHandler.handle(makeReservationCommand);

        // Then
        var savedReservation = reservationRepository.get(reservationMade.getValue()).orElseThrow();
        assertThat(savedReservation.id()).isNotNull();
        assertThat(savedReservation.room()).isEqualTo(room101);
        assertThat(savedReservation.period()).isEqualTo(ReservationPeriod.from(SEP_1ST_18).to(SEP_2ND_18));
        assertThat(savedReservation.numberOfGuests()).isEqualTo(ONE_GUEST);
    }

    @Test
    void it_raises_an_UnavailableForReservationException_when_the_room_is_not_available_for_requested_reservation() {
        // Given
        givenRoom(NUMBER_102, "The small room", ONE_GUEST);
        var makeReservationCommand = new MakeReservationCommand(NUMBER_102, SEP_1ST_18, SEP_2ND_18, FIVE_GUESTS);

        // When
        Throwable raisedException = catchThrowable(() -> makeReservationCommandHandler.handle(makeReservationCommand));

        // Then
        assertThat(raisedException).isInstanceOf(UnavailableForReservationException.class);
    }

    private Room givenRoom(String roomNumber, String roomDescription, int capacity) {
        Room room = new Room(roomNumber, roomDescription, capacity);
        roomRepository.save(room);
        return room;
    }
}
