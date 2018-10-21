package hotel.cuzco.booking.command;

import hotel.cuzco.booking.domain.Reservation;
import hotel.cuzco.booking.domain.ReservationCanceled;
import hotel.cuzco.booking.domain.ReservationId;
import hotel.cuzco.booking.domain.ReservationRepository;
import hotel.cuzco.middleware.commands.CommandHandler;
import hotel.cuzco.middleware.commands.CommandResponse;

public class CancelReservationCommandHandler implements CommandHandler<CommandResponse<ReservationId>, CancelReservationCommand> {
    private final ReservationRepository reservationRepository;

    public CancelReservationCommandHandler(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public CommandResponse<ReservationId> handle(CancelReservationCommand cancelReservationCommand) {
        ReservationId reservationId = cancelReservationCommand.getReservationId();
        Reservation reservation = reservationRepository.get(reservationId).orElseThrow(InvalidCommandException::new);
        reservation.room().cancelReservation(reservationId);
        ReservationCanceled reservationCanceled = new ReservationCanceled(reservationId);
        return CommandResponse.<ReservationId>builder().value(reservationId).build();
    }

    @Override
    public Class listenTo() {
        return CancelReservationCommand.class;
    }
}
