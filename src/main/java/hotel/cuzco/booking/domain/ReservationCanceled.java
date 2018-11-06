package hotel.cuzco.booking.domain;

import hotel.cuzco.middleware.events.Event;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ReservationCanceled implements Event {
    private final ReservationId reservationId;
    private final LocalDate checkInDate;
    private final LocalDate checkOutDate;
    private final int numberOfGuests;
    private final MainContact mainContact;

    public ReservationCanceled(ReservationId reservationId, LocalDate checkInDate, LocalDate checkOutDate, int numberOfGuests, MainContact mainContact) {
        this.reservationId = reservationId;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.numberOfGuests = numberOfGuests;
        this.mainContact = mainContact;
    }

    public ReservationCanceled(Reservation reservationToCancel) {
        this(
                reservationToCancel.id(),
                reservationToCancel.period().getCheckInDate(),
                reservationToCancel.period().getCheckOutDate(),
                reservationToCancel.numberOfGuests(),
                reservationToCancel.mainContact()
        );
    }
}
