package hotel.cuzco.booking.domain.reservation;

import hotel.cuzco.booking.domain.command.MakeReservationCommand;

import java.time.LocalDate;

public class Reservation {

    private RoomId roomId;
    private ReservationId reservationId;
    private ReservationPeriod reservationPeriod;
    private Integer numberOfGuests;
    private boolean canceled;
    private MainContact mainContact;

    static Reservation from(MakeReservationCommand command) {
        var reservationPeriod = ReservationPeriod
                .from(command.getCheckIn())
                .to(command.getCheckoutOut());
        var mainContact = new MainContact(command.getGuestName(), command.getGuestEmail());
        var roomId = new RoomId(command.getRoomNumber());
        return new Reservation(roomId, reservationPeriod, command.getNumberOfGuests(), mainContact);
    }

    public static Reservation deserialize(String serializedReservationId,
                                          RoomId roomId,
                                          LocalDate checkInDate,
                                          LocalDate checkOutDate,
                                          int numberOfGuests,
                                          String mainContactFullName,
                                          String mainContactEmail) {
        ReservationPeriod reservationPeriod = ReservationPeriod.from(checkInDate).to(checkOutDate);
        MainContact mainContact = new MainContact(mainContactFullName, mainContactEmail);
        ReservationId reservationId = new ReservationId(serializedReservationId);
        Reservation reservation = new Reservation(roomId, reservationPeriod, numberOfGuests, mainContact);
        reservation.reservationId = reservationId;
        return reservation;
    }

    public ReservationId id() {
        return reservationId;
    }

    public RoomId roomId() {
        return roomId;
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

    private Reservation(RoomId roomId, ReservationPeriod reservationPeriod, int numberOfGuests, MainContact mainContact) {
        this.numberOfGuests = numberOfGuests;
        this.mainContact = mainContact;
        this.reservationId = new ReservationId();
        this.roomId = roomId;
        this.reservationPeriod = reservationPeriod;
        canceled = false;
    }
}
