package hotel.cuzco.booking.domain;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ReservationPeriod {
    private final LocalDate checkInDate;
    private final LocalDate checkOutDate;

    private ReservationPeriod(LocalDate checkInDate, LocalDate checkOutDate) {
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    public static ReservationPeriodBuilder from(LocalDate checkInDate) {
        return new ReservationPeriodBuilder(checkInDate);
    }

    boolean isOverlappingWith(ReservationPeriod anotherReservationPeriod) {
        return isStrictlyOverlappingWith(anotherReservationPeriod) ||
                anotherReservationPeriod.isStrictlyOverlappingWith(this) ||
                equals(anotherReservationPeriod);
    }

    private boolean isStrictlyOverlappingWith(ReservationPeriod anotherReservationPeriod) {
        return isOverlappingWith(anotherReservationPeriod.checkInDate) ||
                isOverlappingWith(anotherReservationPeriod.checkOutDate);
    }

    private boolean isOverlappingWith(LocalDate stayDate) {
        return stayDate.isAfter(checkInDate) && stayDate.isBefore(checkOutDate);
    }

    @Override
    public String toString() {
        return "[" + checkInDate + " to " + checkOutDate + "]";
    }

    public static class ReservationPeriodBuilder {

        private LocalDate checkInDate;

        ReservationPeriodBuilder(LocalDate checkInDate) {
            this.checkInDate = checkInDate;
        }

        public ReservationPeriod to(LocalDate checkOutDate) {
            checkIsValid(checkOutDate);
            return new ReservationPeriod(checkInDate, checkOutDate);
        }

        private void checkIsValid(LocalDate checkOutDate) {
            if (checkInDate.isAfter(checkOutDate)) {
                throw new InvalidReservationPeriodException();
            }
            if (checkInDate.isEqual(checkOutDate)) {
                throw new OneNightReservationIsNotAllowedException();
            }
        }

    }

}

class InvalidReservationPeriodException extends RuntimeException {
}

class OneNightReservationIsNotAllowedException extends InvalidReservationPeriodException {
}
