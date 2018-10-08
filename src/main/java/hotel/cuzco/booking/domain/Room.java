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
        boolean hasEnoughCapacity = getCapacity() >= numberOfGuests;
        boolean hasNoReservationIsMade = this.reservations.stream().noneMatch(reservation -> reservation.period().overlaps(ReservationPeriod.from(checkInDate).to(checkOutDate)));
        return hasEnoughCapacity && hasNoReservationIsMade;
    }

    public ReservationMade makeReservation(LocalDate checkInDate, LocalDate checkOutDate, int numberOfGuests) {
        var reservation = new Reservation(this, ReservationPeriod.from(checkInDate).to(checkOutDate), numberOfGuests);
        this.reservations.add(reservation);
        return new ReservationMade(reservation);
    }

}
