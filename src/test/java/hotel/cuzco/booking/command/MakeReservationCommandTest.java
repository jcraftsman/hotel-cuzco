package hotel.cuzco.booking.command;

import hotel.cuzco.booking.domain.ReservationRepository;
import hotel.cuzco.booking.infrastructure.ReservationInMemoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class MakeReservationCommandTest {

    private static final LocalDate SEP_1ST_18 = LocalDate.parse("2018-09-01");
    private static final LocalDate SEP_2ND_18 = LocalDate.parse("2018-09-02");

    private ReservationRepository reservationRepository;
    private MakeReservationCommandHandler makeReservationCommandHandler;

    @BeforeEach
    void setUp() {
        reservationRepository = new ReservationInMemoryRepository();
        makeReservationCommandHandler = new MakeReservationCommandHandler(reservationRepository);
    }

    @Test
    void it_saves_the_reservation_made() {
        // Given
        var numberOfGuests = 1;
        String roomNumber = "101";
        var makeReservationCommand = new MakeReservationCommand(roomNumber, SEP_1ST_18, SEP_2ND_18, numberOfGuests);

        // When
        var reservationMade = makeReservationCommandHandler.handle(makeReservationCommand);

        // Then
        var savedReservation = reservationRepository.get(reservationMade.id());
        assertThat(savedReservation).isNotNull();
    }
}
