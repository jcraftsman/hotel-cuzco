package hotel.cuzco.booking.domain;

public class Reservation {

    private Room room;

    public Reservation(Room room) {
        this.room = room;
    }

    public ReservationId id() {
        return null;
    }

    public Room room() {
        return room;
    }
}
