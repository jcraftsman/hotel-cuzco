package hotel.cuzco.booking.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class ReservationPeriodTest {

    private static final LocalDate DEC_25_18 = LocalDate.parse("2018-12-25");
    private static final LocalDate JAN_01_19 = LocalDate.parse("2019-01-01");
    private static final LocalDate JAN_02_19 = LocalDate.parse("2019-01-02");
    private static final LocalDate JAN_03_19 = LocalDate.parse("2019-01-03");
    private static final LocalDate JAN_15_19 = LocalDate.parse("2019-01-15");
    private static final LocalDate JAN_20_19 = LocalDate.parse("2019-01-20");

    private static final ReservationPeriod DEC_25_TO_JAN_01_19 = ReservationPeriod.from(DEC_25_18).to(JAN_01_19);
    private static final ReservationPeriod JAN_01_TO_JAN_15_19 = ReservationPeriod.from(JAN_01_19).to(JAN_15_19);
    private static final ReservationPeriod JAN_01_TO_JAN_02_19 = ReservationPeriod.from(JAN_01_19).to(JAN_02_19);
    private static final ReservationPeriod JAN_01_TO_JAN_03_19 = ReservationPeriod.from(JAN_01_19).to(JAN_03_19);
    private static final ReservationPeriod JAN_02_TO_JAN_03_19 = ReservationPeriod.from(JAN_02_19).to(JAN_03_19);
    private static final ReservationPeriod JAN_03_TO_JAN_20_19 = ReservationPeriod.from(JAN_03_19).to(JAN_20_19);
    private static final ReservationPeriod JAN_15_TO_JAN_20_19 = ReservationPeriod.from(JAN_15_19).to(JAN_20_19);


    private static Stream<Arguments> reservationPeriods() {
        // Given
        return Stream.of(
                arguments(" ===   ", JAN_01_TO_JAN_15_19, JAN_01_TO_JAN_15_19, true),
                arguments(" --__  ", JAN_01_TO_JAN_15_19, DEC_25_TO_JAN_01_19, false),
                arguments(" =___  ", JAN_01_TO_JAN_15_19, JAN_01_TO_JAN_02_19, true),
                arguments(" _=__  ", JAN_01_TO_JAN_15_19, JAN_02_TO_JAN_03_19, true),
                arguments(" __=-- ", JAN_01_TO_JAN_15_19, JAN_03_TO_JAN_20_19, true),
                arguments(" __ -- ", DEC_25_TO_JAN_01_19, JAN_15_TO_JAN_20_19, false),
                arguments(" ___-- ", JAN_01_TO_JAN_15_19, JAN_15_TO_JAN_20_19, false),
                arguments(" _==-- ", JAN_01_TO_JAN_15_19, JAN_03_TO_JAN_20_19, true),
                arguments(" =--   ", JAN_01_TO_JAN_03_19, JAN_01_TO_JAN_15_19, true),
                arguments(" -=-   ", JAN_02_TO_JAN_03_19, JAN_01_TO_JAN_15_19, true)
        );
    }

    @ParameterizedTest(name = "| {0} | ==> {1} conflictsWith {2} should be {3}")
    @MethodSource("reservationPeriods")
    void conflictsWith(String periodsRepresentation,
                       ReservationPeriod reservationPeriod,
                       ReservationPeriod anotherReservationPeriod,
                       boolean shouldItConflict) {

        // When
        boolean doesItConflict = reservationPeriod
                .conflictsWith(anotherReservationPeriod);

        // Then
        assertThat(doesItConflict).isEqualTo(shouldItConflict);
    }
}