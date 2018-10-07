package hotel.cuzco.booking.domain;

public class Reservation {

    private Room room;
    private ReservationId reservationId;

    public Reservation(Room room) {
        this.room = room;
        this.reservationId = new ReservationId();
    }

    public ReservationId id() {
        return reservationId;
    }

    public Room room() {
        return room;
    }
}
