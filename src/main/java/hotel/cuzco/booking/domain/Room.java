package hotel.cuzco.booking.domain;

import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

@Getter
public class Room {

    private RoomId roomId;
    private String description;
    private int capacity;
    private Collection<Reservation> reservations;

    public Room(String number, String description, int capacity) {
        this.roomId = new RoomId(number);
        this.description = description;
        this.capacity = capacity;
        this.reservations = new ArrayList<>();
    }

    public RoomId id() {
        return this.roomId;
    }

    public boolean isAvailableFor(int numberOfGuests, LocalDate checkInDate, LocalDate checkOutDate) {
        return hasEnoughCapacity(numberOfGuests) && hasNoOverlappingReservation(checkInDate, checkOutDate);
    }

    public ReservationMade makeReservation(LocalDate checkInDate, LocalDate checkOutDate, int numberOfGuests) {
        var reservation = new Reservation(this, ReservationPeriod.from(checkInDate).to(checkOutDate), numberOfGuests);
        this.reservations.add(reservation);
        return new ReservationMade(reservation);
    }

    private boolean hasEnoughCapacity(int numberOfGuests) {
        return getCapacity() >= numberOfGuests;
    }

    private boolean hasNoOverlappingReservation(LocalDate checkInDate, LocalDate checkOutDate) {
        var reservationPeriod = ReservationPeriod.from(checkInDate).to(checkOutDate);
        return this.reservations.stream()
                .noneMatch(reservation -> reservation.isOverlappingWith(reservationPeriod));
    }

}
