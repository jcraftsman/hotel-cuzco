package hotel.cuzco.booking.command;

import hotel.cuzco.booking.domain.ReservationMade;
import hotel.cuzco.booking.domain.RoomId;
import hotel.cuzco.booking.domain.RoomRepository;

public class MakeReservationCommandHandler {
    private final RoomRepository roomRepository;

    public MakeReservationCommandHandler(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public ReservationMade handle(MakeReservationCommand makeReservationCommand) {
        var room = roomRepository.get(new RoomId(makeReservationCommand.getRoomNumber()));
        var reservationMade = room.makeReservation(
                makeReservationCommand.getCheckIn(),
                makeReservationCommand.getCheckoutOut(),
                makeReservationCommand.getNumberOfGuests());
        this.roomRepository.add(room);
        return reservationMade;
    }
}
