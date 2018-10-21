package hotel.cuzco.booking.command;

import hotel.cuzco.booking.domain.ReservationMade;
import hotel.cuzco.booking.domain.ReservationRepository;
import hotel.cuzco.booking.domain.Room;
import hotel.cuzco.booking.domain.RoomRepository;
import hotel.cuzco.booking.infrastructure.database.inmemory.ReservationInMemoryRepository;
import hotel.cuzco.booking.infrastructure.database.inmemory.RoomInMemoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class CancelReservationCommandHandlerTest {

    private static final LocalDate OCT_15_19 = LocalDate.parse("2019-10-15");
    private static final LocalDate OCT_20_19 = LocalDate.parse("2019-10-20");
    private static final String NUMBER_1 = "N1";
    private static final int TWO_GUESTS = 2;
    private CancelReservationCommandHandler cancelReservationCommandHandler;

    private ReservationRepository reservationRepository;
    private RoomRepository roomRepository;

    @BeforeEach
    void setUp() {
        reservationRepository = new ReservationInMemoryRepository();
        roomRepository = new RoomInMemoryRepository(reservationRepository);
        cancelReservationCommandHandler = new CancelReservationCommandHandler(reservationRepository);
    }

    @Test
    void it_should_cancel_an_existing_reservation() {
        // Given
        var room = new Room(NUMBER_1, "", TWO_GUESTS);
        var reservationMade = room.makeReservation(OCT_15_19, OCT_20_19, TWO_GUESTS);
        roomRepository.add(room);
        var reservationId = reservationMade.id();
        var cancelReservationCommand = new CancelReservationCommand(reservationId);

        // When
        var reservationCanceled = cancelReservationCommandHandler.handle(cancelReservationCommand);

        // Then
        assertThat(reservationCanceled.getValue()).isEqualTo(reservationId);
        assertThat(reservationRepository.get(reservationId).orElseThrow().isCanceled()).isTrue();
    }

    @Test
    void it_should_an_unknown_reservation() {
        // Given
        var room = new Room(NUMBER_1, "", TWO_GUESTS);
        roomRepository.add(room);
        var reservationId = ReservationMade.random().id();
        var cancelRandomReservationCommand = new CancelReservationCommand(reservationId);

        // When
        Throwable raisedException = catchThrowable(() -> cancelReservationCommandHandler.handle(cancelRandomReservationCommand));

        // Then
        assertThat(raisedException).isInstanceOf(InvalidCommandException.class);
    }
}