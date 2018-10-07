package hotel.cuzco.booking.domain;

public class Reservation {

    private Room room;
    private ReservationId reservationId;
    private ReservationPeriod reservationPeriod;

    public Reservation(Room room, ReservationPeriod reservationPeriod) {
        this.reservationId = new ReservationId();
        this.room = room;
        this.reservationPeriod = reservationPeriod;
    }

    public ReservationId id() {
        return reservationId;
    }

    public Room room() {
        return room;
    }

    public ReservationPeriod period() {
        return reservationPeriod;
    }
}
