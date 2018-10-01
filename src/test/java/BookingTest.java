import domain.GetAvailableRoomsQuery;
import domain.GetAvailableRoomsQueryHandler;
import domain.Hotel;
import domain.Room;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class BookingTest {

    private static final LocalDate SEP_1ST_18 = LocalDate.parse("2018-09-01");
    private static final LocalDate SEP_2ND_18 = LocalDate.parse("2018-09-02");

    @Test
    void it_returns_all_rooms_available_for_1_night_stay_for_1_guest() {
        // Given
        var availableRoomsQueryHandler = new GetAvailableRoomsQueryHandler();
        var numberOfGuests = 1;
        var getAvailableRoomsQuery = new GetAvailableRoomsQuery(SEP_1ST_18, SEP_2ND_18, numberOfGuests);

        // When
        Iterable<Room> availableRooms = availableRoomsQueryHandler.handle(getAvailableRoomsQuery);

        // Then
        assertThat(availableRooms).isEqualTo(Hotel.CUZCO().allRooms());
    }

}
