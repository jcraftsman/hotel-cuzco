package hotel.cuzco.booking.usecase.command;

import hotel.cuzco.booking.domain.reservation.*;
import hotel.cuzco.booking.domain.command.CancelReservationCommand;
import hotel.cuzco.booking.domain.command.MakeReservationCommand;
import hotel.cuzco.booking.domain.event.ReservationCanceled;
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
    private static final String GUEST_NAME = "Chuck Norris";
    private static final String GUEST_MAIL = "chuck.norris@mail.com";
    private CancelReservationCommandHandler cancelReservationCommandHandler;

    private ReservationRepository reservationRepository;
    private RoomRepository roomRepository;

    @BeforeEach
    void setUp() {
        reservationRepository = new ReservationInMemoryRepository();
        roomRepository = new RoomInMemoryRepository(reservationRepository);
        cancelReservationCommandHandler = new CancelReservationCommandHandler(roomRepository);
    }

    @Test
    void it_should_cancel_an_existing_reservation() {
        // Given
        var room = new Room(NUMBER_1, "", TWO_GUESTS);
        var command = new MakeReservationCommand(NUMBER_1, OCT_15_19, OCT_20_19, TWO_GUESTS, GUEST_NAME, GUEST_MAIL);
        var reservationId = room.makeReservation(command).getValue();
        roomRepository.save(room);
        var cancelReservationCommand = new CancelReservationCommand(reservationId);

        // When
        var commandResponse = cancelReservationCommandHandler.handle(cancelReservationCommand);

        // Then
        var expectedReservationCanceledEvent = new ReservationCanceled(
                reservationId, OCT_15_19, OCT_20_19, TWO_GUESTS, new MainContact(GUEST_NAME, GUEST_MAIL)
        );
        assertThat(commandResponse.getEvents()).containsExactly(expectedReservationCanceledEvent);
        var reservationEntity = reservationRepository.get(reservationId).orElseThrow();
        assertThat(reservationEntity.isCanceled()).isTrue();
    }

    @Test
    void it_should_an_unknown_reservation() {
        // Given
        var room = new Room(NUMBER_1, "", TWO_GUESTS);
        roomRepository.save(room);
        var reservationId = ReservationId.random();
        var cancelRandomReservationCommand = new CancelReservationCommand(reservationId);

        // When
        Throwable raisedException = catchThrowable(() -> cancelReservationCommandHandler.handle(cancelRandomReservationCommand));

        // Then
        assertThat(raisedException).isInstanceOf(InvalidCommandException.class);
    }
}