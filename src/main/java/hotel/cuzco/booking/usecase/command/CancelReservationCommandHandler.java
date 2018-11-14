package hotel.cuzco.booking.usecase.command;

import common.ddd.patterns.CommandHandler;
import common.ddd.patterns.CommandResponse;
import hotel.cuzco.booking.domain.command.CancelReservationCommand;
import hotel.cuzco.booking.domain.reservation.ReservationId;
import hotel.cuzco.booking.domain.reservation.Room;
import hotel.cuzco.booking.domain.reservation.RoomRepository;

public class CancelReservationCommandHandler implements CommandHandler<CommandResponse<Void>, CancelReservationCommand> {
    private final RoomRepository roomRepository;

    CancelReservationCommandHandler(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public CommandResponse<Void> handle(CancelReservationCommand cancelReservationCommand) {
        ReservationId reservationId = cancelReservationCommand.getReservationId();
        Room room = roomRepository.getByReservation(reservationId).orElseThrow(InvalidCommandException::new);
        return room.cancelReservation(cancelReservationCommand);
    }

    @Override
    public Class listenTo() {
        return CancelReservationCommand.class;
    }
}
