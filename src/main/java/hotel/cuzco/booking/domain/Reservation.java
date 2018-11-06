package hotel.cuzco.booking.domain;

import hotel.cuzco.booking.command.MakeReservationCommand;

public class Reservation {

    private Room room;
    private ReservationId reservationId;
    private ReservationPeriod reservationPeriod;
    private Integer numberOfGuests;
    private boolean canceled;
    private MainContact mainContact;

    private Reservation(Room room, ReservationPeriod reservationPeriod, int numberOfGuests, MainContact mainContact) {
        this.numberOfGuests = numberOfGuests;
        this.mainContact = mainContact;
        this.reservationId = new ReservationId();
        this.room = room;
        this.reservationPeriod = reservationPeriod;
        canceled = false;
    }

    static Reservation from(MakeReservationCommand command, Room room) {
        var reservationPeriod = ReservationPeriod
                .from(command.getCheckIn())
                .to(command.getCheckoutOut());
        var mainContact = new MainContact(command.getGuestName(), command.getGuestEmail());
        return new Reservation(room, reservationPeriod, command.getNumberOfGuests(), mainContact);
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

    boolean conflictsWith(ReservationPeriod reservationPeriod) {
        return period().conflictsWith(reservationPeriod);
    }

    public boolean isCanceled() {
        return canceled;
    }

    void cancel() {
        canceled = true;
    }

    public MainContact mainContact() {
        return this.mainContact;
    }
}
