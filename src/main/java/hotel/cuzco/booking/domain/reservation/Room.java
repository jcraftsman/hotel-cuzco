package hotel.cuzco.booking.domain.reservation;

import common.ddd.patterns.CommandResponse;
import hotel.cuzco.booking.domain.command.CancelReservationCommand;
import hotel.cuzco.booking.domain.command.MakeReservationCommand;
import hotel.cuzco.booking.domain.event.ReservationCanceled;
import hotel.cuzco.booking.domain.event.ReservationMade;
import hotel.cuzco.booking.domain.exceptions.UnavailableForReservationException;
import hotel.cuzco.booking.domain.exceptions.UnknownReservationException;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

@Getter
public class Room {

    private RoomId roomId;
    private String description;
    private int capacity;
    private Collection<Reservation> activeReservations;
    private Collection<Reservation> canceledReservation;

    public Room(String number, String description, int capacity) {
        this.roomId = new RoomId(number);
        this.description = description;
        this.capacity = capacity;
        this.activeReservations = new ArrayList<>();
        this.canceledReservation = new ArrayList<>();
    }

    public RoomId id() {
        return this.roomId;
    }

    public boolean isAvailableFor(int numberOfGuests, LocalDate checkInDate, LocalDate checkOutDate) {
        return hasEnoughCapacity(numberOfGuests) && hasNoConflictingReservation(checkInDate, checkOutDate);
    }

    public CommandResponse<ReservationId> makeReservation(MakeReservationCommand command) {
        checkAvailability(command.getCheckIn(), command.getCheckoutOut(), command.getNumberOfGuests());
        var reservation = Reservation.from(command);
        this.activeReservations.add(reservation);
        return CommandResponse.<ReservationId>builder()
                .value(reservation.id())
                .event(new ReservationMade(reservation))
                .build();
    }

    public CommandResponse<Void> cancelReservation(CancelReservationCommand cancelReservationCommand) {
        var reservationToCancel = activeReservations.stream()
                .filter(reservation -> reservation.id().equals(cancelReservationCommand.getReservationId()))
                .findFirst()
                .orElseThrow(UnknownReservationException::new);
        reservationToCancel.cancel();
        this.activeReservations.remove(reservationToCancel);
        this.canceledReservation.add(reservationToCancel);
        return CommandResponse.<Void>builder()
                .event(new ReservationCanceled(reservationToCancel))
                .build();
    }

    private boolean hasEnoughCapacity(int numberOfGuests) {
        return getCapacity() >= numberOfGuests;
    }

    private boolean hasNoConflictingReservation(LocalDate checkInDate, LocalDate checkOutDate) {
        var reservationPeriod = ReservationPeriod.from(checkInDate).to(checkOutDate);
        return this.activeReservations.stream()
                .noneMatch(reservation -> reservation.conflictsWith(reservationPeriod));
    }

    private void checkAvailability(LocalDate checkInDate, LocalDate checkOutDate, int numberOfGuests) {
        if (!isAvailableFor(numberOfGuests, checkInDate, checkOutDate)) {
            throw new UnavailableForReservationException();
        }
    }
}
