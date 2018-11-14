package hotel.cuzco.booking.usecase.command;

import common.ddd.patterns.CommandHandler;
import common.ddd.patterns.CommandResponse;
import hotel.cuzco.booking.domain.command.MakeReservationCommand;
import hotel.cuzco.booking.domain.reservation.ReservationId;
import hotel.cuzco.booking.domain.reservation.RoomId;
import hotel.cuzco.booking.domain.reservation.RoomRepository;

public class MakeReservationCommandHandler implements CommandHandler<CommandResponse<ReservationId>, MakeReservationCommand> {
    private final RoomRepository roomRepository;

    MakeReservationCommandHandler(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public CommandResponse<ReservationId> handle(MakeReservationCommand makeReservationCommand) {
        var room = roomRepository.get(new RoomId(makeReservationCommand.getRoomNumber()));
        var commandResponse = room.makeReservation(makeReservationCommand);
        this.roomRepository.save(room);
        return commandResponse;
    }

    @Override
    public Class listenTo() {
        return MakeReservationCommand.class;
    }
}
