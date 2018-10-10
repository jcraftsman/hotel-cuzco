package hotel.cuzco.booking.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class RoomTest {

    private static final LocalDate DEC_25_18 = LocalDate.parse("2018-12-25");
    private static final LocalDate JAN_01_19 = LocalDate.parse("2019-01-01");
    private static final LocalDate JAN_02_19 = LocalDate.parse("2019-01-02");
    private static final int ONE_GUEST = 1;
    private static final int TWO_GUESTS = 2;
    private static final int THREE_GUESTS = 3;

    private Room singleRoom;
    private Room roomFor2;

    @BeforeEach
    void setUp() {
        singleRoom = new Room("101", "Single room", 1);
        roomFor2 = new Room("102", "Twin room", 2);
    }

    @Test
    void it_is_available_for_one_guest_stay_when_single_room() {
        // When
        boolean isSingleRoomAvailableForOneGuest = singleRoom.isAvailableFor(ONE_GUEST, JAN_01_19, JAN_02_19);

        // Then
        assertThat(isSingleRoomAvailableForOneGuest).isTrue();
    }

    @Test
    void it_is_never_available_for_two_guests_stay_when_single_room() {
        // When
        boolean isSingleRoomAvailableForTwoGuests = singleRoom.isAvailableFor(TWO_GUESTS, JAN_01_19, JAN_02_19);

        // Then
        assertThat(isSingleRoomAvailableForTwoGuests).isFalse();
    }

    @Test
    void it_is_available_for_one_guest_stay_when_room_for_two() {
        // When
        boolean isRoomForTwoAvailableForOneGuest = roomFor2.isAvailableFor(ONE_GUEST, JAN_01_19, JAN_02_19);

        // Then
        assertThat(isRoomForTwoAvailableForOneGuest).isTrue();
    }

    @Test
    void it_is_available_for_two_guests_stay_when_room_for_two() {
        // When
        boolean isRoomForTwoAvailableForTwoGuests = roomFor2.isAvailableFor(TWO_GUESTS, JAN_01_19, JAN_02_19);

        // Then
        assertThat(isRoomForTwoAvailableForTwoGuests).isTrue();
    }

    @Test
    void it_is_never_available_for_three_guests_stay_when_room_for_two() {
        // When
        boolean isRoomForTwoAvailableForThreeGuests = roomFor2.isAvailableFor(THREE_GUESTS, JAN_01_19, JAN_02_19);

        // Then
        assertThat(isRoomForTwoAvailableForThreeGuests).isFalse();
    }

    @Test
    void it_is_unavailable_for_one_guest_stay_when_single_room_has_a_conflicting_reservation() {
        // Given
        singleRoom.makeReservation(DEC_25_18, JAN_02_19, ONE_GUEST);

        // When
        boolean isSingleRoomAvailableForOneGuest = singleRoom.isAvailableFor(ONE_GUEST, JAN_01_19, JAN_02_19);

        // Then
        assertThat(isSingleRoomAvailableForOneGuest).isFalse();
    }

    @Test
    void it_cannot_make_reservation_when_not_available() {
        // When
        Throwable raisedException = catchThrowable(() -> singleRoom.makeReservation(DEC_25_18, JAN_02_19, TWO_GUESTS));

        // Then
        assertThat(raisedException).isInstanceOf(UnavailableForReservationException.class);
    }

}