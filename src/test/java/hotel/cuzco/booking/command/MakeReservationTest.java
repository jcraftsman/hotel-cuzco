package hotel.cuzco.booking.command;

import hotel.cuzco.booking.infrastructure.ReservationInMemoryRespository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class MakeReservationTest {

    private static final LocalDate SEP_1ST_18 = LocalDate.parse("2018-09-01");
    private static final LocalDate SEP_2ND_18 = LocalDate.parse("2018-09-02");


    @Test
    void it_saves_the_reservation_made() {
        // Given
        var numberOfGuests = 1;
        String roomNumber = "101";
        var makeReservationCommand = new MakeReservationCommand(roomNumber, SEP_1ST_18, SEP_2ND_18, numberOfGuests);
        var reservationRespository = new ReservationInMemoryRespository();
        var makeReservationCommandHandler = new MakeReservationCommandHandler(reservationRespository);

        // When
        var reservationMade = makeReservationCommandHandler.handle(makeReservationCommand);

        // Then
        var savedReservation = reservationRespository.get(reservationMade.id());
        assertThat(savedReservation).isNotNull();
    }
}
