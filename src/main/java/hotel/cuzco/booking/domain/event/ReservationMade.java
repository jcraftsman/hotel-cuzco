package hotel.cuzco.booking.domain.event;

import common.ddd.patterns.Event;
import hotel.cuzco.booking.domain.reservation.MainContact;
import hotel.cuzco.booking.domain.reservation.Reservation;
import hotel.cuzco.booking.domain.reservation.ReservationId;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ReservationMade implements Event {
    private final ReservationId reservationId;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private int numberOfGuests;
    private MainContact mainContact;

    public ReservationMade(Reservation reservation) {
        this.reservationId = reservation.id();
        this.checkIn = reservation.period().getCheckInDate();
        this.checkOut = reservation.period().getCheckOutDate();
        this.numberOfGuests = reservation.numberOfGuests();
        this.mainContact = reservation.mainContact();
    }

    public ReservationMade(ReservationId reservationId, LocalDate checkIn, LocalDate checkOut, int numberOfGuests, MainContact mainContact) {
        this.reservationId = reservationId;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.numberOfGuests = numberOfGuests;
        this.mainContact = mainContact;
    }

    public ReservationId id() {
        return reservationId;
    }

}
