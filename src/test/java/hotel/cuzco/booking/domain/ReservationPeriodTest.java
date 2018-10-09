package hotel.cuzco.booking.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class ReservationPeriodTest {

    private static final LocalDate DEC_23_18 = LocalDate.parse("2018-12-23");
    private static final LocalDate DEC_26_18 = LocalDate.parse("2018-12-26");

    @Test
    void it_creates_a_reservationPeriod_from_checkIn_to_checkOut_dates() {
        // Given
        LocalDate checkInDate = DEC_23_18;
        LocalDate checkOutDate = DEC_26_18;

        // When
        var reservationPeriod = ReservationPeriod.from(checkInDate).to(checkOutDate);

        // Then
        assertThat(reservationPeriod.getCheckInDate()).isEqualTo(checkInDate);
        assertThat(reservationPeriod.getCheckOutDate()).isEqualTo(checkOutDate);
    }

    @Test
    void it_raises_an_invalidReservationPeriodException_when_checkInDate_is_before_the_checkout_date() {
        // When
        var raisedException = catchThrowable(() -> ReservationPeriod.from(DEC_26_18).to(DEC_23_18));

        // Then
        assertThat(raisedException).isInstanceOf(InvalidReservationPeriodException.class);
    }


}