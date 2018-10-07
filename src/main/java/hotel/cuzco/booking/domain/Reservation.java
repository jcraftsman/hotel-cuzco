package hotel.cuzco.booking.domain;

public class Reservation {

    private Room room;
    private ReservationId reservationId;
    private ReservationPeriod reservationPeriod;
    private Integer numberOfGuests;

    public Reservation(Room room, ReservationPeriod reservationPeriod, int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
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

    public Integer numberOfGuests() {
        return numberOfGuests;
    }
}
