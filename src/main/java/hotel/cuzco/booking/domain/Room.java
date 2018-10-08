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

    public boolean isAvailableFor(int numberOfGuests) {
        boolean hasEnoughCapacity = getCapacity() >= numberOfGuests;
        boolean hasNoReservationIsMade = this.reservations.isEmpty();
        return hasEnoughCapacity && hasNoReservationIsMade;
    }

    public void makeReservation(LocalDate checkInDate, LocalDate checkOutDate, int numberOfGuests) {
        var reservation = new Reservation(this, ReservationPeriod.from(checkInDate).to(checkOutDate), numberOfGuests);
        this.addReservation(reservation);
    }

    public void addReservation(Reservation reservation) {
        this.reservations.add(reservation);
    }
}
