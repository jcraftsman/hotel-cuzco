package hotel.cuzco.booking.domain.reservation;

import hotel.cuzco.booking.domain.command.CancelReservationCommand;
import hotel.cuzco.booking.domain.command.MakeReservationCommand;
import hotel.cuzco.booking.domain.event.ReservationCanceled;
import hotel.cuzco.booking.domain.event.ReservationMade;
import hotel.cuzco.booking.domain.exceptions.UnavailableForReservationException;
import hotel.cuzco.booking.domain.exceptions.UnknownReservationException;
import common.ddd.patterns.CommandResponse;
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
    private static final String GUEST_NAME = "Chris Evert";
    private static final String GUEST_EMAIL = "chris.evert@mail.com";
    private static final String NUMBER_101 = "101";
    private static final String NUMBER_102 = "102";

    private Room singleRoom;
    private Room roomFor2;

    @BeforeEach
    void setUp() {
        singleRoom = new Room(NUMBER_101, "Single room", 1);
        roomFor2 = new Room(NUMBER_102, "Twin room", 2);
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
        var makeReservationCommand = new MakeReservationCommand(NUMBER_101, DEC_25_18, JAN_02_19, ONE_GUEST);

        // When
        singleRoom.makeReservation(makeReservationCommand);

        // Then
        boolean isSingleRoomAvailableForOneGuest = singleRoom.isAvailableFor(ONE_GUEST, JAN_01_19, JAN_02_19);
        assertThat(isSingleRoomAvailableForOneGuest).isFalse();
    }

    @Test
    void it_returns_a_command_response_with_reservation_id_and_reservation_made_event() {
        // Given
        var command = new MakeReservationCommand(NUMBER_101, DEC_25_18, JAN_02_19, ONE_GUEST, GUEST_NAME, GUEST_EMAIL);

        // When
        var commandResponse = singleRoom.makeReservation(command);

        // Then
        assertThat(commandResponse.getValue()).isNotNull();
        var expectedReservationMade = new ReservationMade(
                commandResponse.getValue(), DEC_25_18, JAN_02_19, ONE_GUEST, new MainContact(GUEST_NAME, GUEST_EMAIL)
        );
        assertThat(commandResponse.getEvents()).containsExactly(expectedReservationMade);
    }

    @Test
    void it_cannot_make_reservation_when_not_available() {
        // When
        var command = new MakeReservationCommand(NUMBER_101, DEC_25_18, JAN_02_19, TWO_GUESTS);
        Throwable raisedException = catchThrowable(() -> singleRoom.makeReservation(command));

        // Then
        assertThat(raisedException).isInstanceOf(UnavailableForReservationException.class);
    }

    @Test
    void it_is_available_when_room_has_a_conflicting_reservation_canceled() {
        // Given
        var makeReservationCommand = new MakeReservationCommand(NUMBER_101, DEC_25_18, JAN_02_19, ONE_GUEST);
        var reservationMade = singleRoom.makeReservation(makeReservationCommand);
        var reservationId = reservationMade.getValue();

        // When
        singleRoom.cancelReservation(new CancelReservationCommand(reservationId));

        // Then
        boolean isSingleRoomAvailable = singleRoom.isAvailableFor(ONE_GUEST, JAN_01_19, JAN_02_19);
        assertThat(isSingleRoomAvailable).isTrue();
    }

    @Test
    void it_returns_a_canceled_reservation_event_when_a_reservation_is_canceled() {
        // Given
        var command = new MakeReservationCommand(NUMBER_101, DEC_25_18, JAN_02_19, ONE_GUEST, GUEST_NAME, GUEST_EMAIL);
        var reservationMade = singleRoom.makeReservation(command);
        var reservationId = reservationMade.getValue();

        // When
        CommandResponse<Void> response = singleRoom.cancelReservation(new CancelReservationCommand(reservationId));

        // Then
        var expectedReservationCanceledEvent = new ReservationCanceled(
                reservationId, DEC_25_18, JAN_02_19, ONE_GUEST, new MainContact(GUEST_NAME, GUEST_EMAIL)
        );
        assertThat(response.getEvents()).containsExactly(expectedReservationCanceledEvent);
    }

    @Test
    void it_raises_an_UnknownReservationException_when_reservationId_is_not_related_to_any_active_reservation() {
        // Given
        var cancelUnknownReservation = new CancelReservationCommand(ReservationId.random());

        // When
        Throwable raisedException = catchThrowable(() -> singleRoom.cancelReservation(cancelUnknownReservation));

        // Then
        assertThat(raisedException).isInstanceOf(UnknownReservationException.class);
    }

}