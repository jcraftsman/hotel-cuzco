package hotel.cuzco.booking.command;

import hotel.cuzco.booking.domain.*;
import hotel.cuzco.middleware.commands.CommandHandler;
import hotel.cuzco.middleware.commands.CommandResponse;

public class MakeReservationCommandHandler implements CommandHandler<CommandResponse<ReservationId>, MakeReservationCommand> {
    private final RoomRepository roomRepository;

    public MakeReservationCommandHandler(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public CommandResponse<ReservationId> handle(MakeReservationCommand makeReservationCommand) {
        var room = roomRepository.get(new RoomId(makeReservationCommand.getRoomNumber()));
        var reservationMade = room.makeReservation(
                makeReservationCommand.getCheckIn(),
                makeReservationCommand.getCheckoutOut(),
                makeReservationCommand.getNumberOfGuests());
        this.roomRepository.add(room);

        ReservationMade event = buildReservationMadeEvent(makeReservationCommand, reservationMade.id());
        return CommandResponse.<ReservationId>builder()
                .value(reservationMade.id())
                .event(event)
                .build();
    }

    private ReservationMade buildReservationMadeEvent(MakeReservationCommand makeReservationCommand, ReservationId reservationId) {
        var mainContact = new MainContact(makeReservationCommand.getGuestName(), makeReservationCommand.getGuestEmail());
        return ReservationMade.builder()
                .reservationId(reservationId)
                .checkIn(makeReservationCommand.getCheckIn())
                .checkOut(makeReservationCommand.getCheckoutOut())
                .numberOfGuests(makeReservationCommand.getNumberOfGuests())
                .mainContact(mainContact)
                .build();
    }

    @Override
    public Class listenTo() {
        return MakeReservationCommand.class;
    }
}
