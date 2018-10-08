package hotel.cuzco.booking.domain;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ReservationPeriod {
    private final LocalDate checkInDate;
    private final LocalDate checkOutDate;

    public ReservationPeriod(LocalDate checkInDate, LocalDate checkOutDate) {

        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    public static ReservationPeriodBuilder from(LocalDate checkInDate) {
        return new ReservationPeriodBuilder(checkInDate);
    }

    public boolean overlaps(ReservationPeriod reservationPeriod) {
        return !checkInDate.isBefore(reservationPeriod.checkInDate);
    }

    public static class ReservationPeriodBuilder {

        private LocalDate checkInDate;

        ReservationPeriodBuilder(LocalDate checkInDate) {
            this.checkInDate = checkInDate;
        }

        public ReservationPeriod to(LocalDate checkOutDate) {
            return new ReservationPeriod(checkInDate, checkOutDate);
        }
    }
}
