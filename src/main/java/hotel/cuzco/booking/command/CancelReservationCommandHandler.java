package hotel.cuzco.booking.command;

import hotel.cuzco.booking.domain.*;
import hotel.cuzco.middleware.commands.CommandHandler;
import hotel.cuzco.middleware.commands.CommandResponse;

public class CancelReservationCommandHandler implements CommandHandler<CommandResponse<Void>, CancelReservationCommand> {
    private final RoomRepository roomRepository;

    public CancelReservationCommandHandler(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public CommandResponse<Void> handle(CancelReservationCommand cancelReservationCommand) {
        ReservationId reservationId = cancelReservationCommand.getReservationId();
        Room room = roomRepository.getByReservation(reservationId).orElseThrow(InvalidCommandException::new);
        room.cancelReservation(reservationId);
        ReservationCanceled reservationCanceled = new ReservationCanceled(reservationId);
        return CommandResponse.<Void>builder().event(reservationCanceled).build();
    }

    @Override
    public Class listenTo() {
        return CancelReservationCommand.class;
    }
}
