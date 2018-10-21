package hotel.cuzco.booking.command;

import hotel.cuzco.booking.domain.ReservationId;
import hotel.cuzco.booking.domain.ReservationMade;
import hotel.cuzco.booking.domain.RoomId;
import hotel.cuzco.booking.domain.RoomRepository;
import hotel.cuzco.middleware.commands.CommandHandler;
import hotel.cuzco.middleware.commands.CommandResponse;

public class MakeReservationCommandHandler implements CommandHandler<CommandResponse<ReservationId>, MakeReservationCommand> {
    private final RoomRepository roomRepository;

    public MakeReservationCommandHandler(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public CommandResponse<ReservationId> handle(MakeReservationCommand makeReservationCommand) {
        var room = roomRepository.get(new RoomId(makeReservationCommand.getRoomNumber()));
        var reservationId = room.makeReservation(
                makeReservationCommand.getCheckIn(),
                makeReservationCommand.getCheckoutOut(),
                makeReservationCommand.getNumberOfGuests());
        this.roomRepository.add(room);

        return toCommandResponse(makeReservationCommand, reservationId);
    }

    private CommandResponse<ReservationId> toCommandResponse(MakeReservationCommand makeReservationCommand,
                                                             ReservationId reservationId) {
        ReservationMade event = ReservationMade.from(makeReservationCommand, reservationId);
        return CommandResponse.<ReservationId>builder()
                .value(reservationId)
                .event(event)
                .build();
    }

    @Override
    public Class listenTo() {
        return MakeReservationCommand.class;
    }
}
