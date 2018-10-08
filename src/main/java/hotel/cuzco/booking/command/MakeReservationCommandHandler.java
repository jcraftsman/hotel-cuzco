package hotel.cuzco.booking.command;

import hotel.cuzco.booking.domain.*;

public class MakeReservationCommandHandler {
    private final RoomRepository roomRepository;

    public MakeReservationCommandHandler(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public ReservationMade handle(MakeReservationCommand makeReservationCommand) {
        var room = roomRepository.get(new RoomId(makeReservationCommand.getRoomNumber()));
        var reservationPeriod = ReservationPeriod
                .from(makeReservationCommand.getCheckIn())
                .to(makeReservationCommand.getCheckoutOut());
        var reservation = new Reservation(room, reservationPeriod, makeReservationCommand.getNumberOfGuests());
        room.addReservation(reservation);
        this.roomRepository.add(room);
        return new ReservationMade(reservation);
    }
}
