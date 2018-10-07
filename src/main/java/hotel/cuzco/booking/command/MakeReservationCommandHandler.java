package hotel.cuzco.booking.command;

import hotel.cuzco.booking.domain.*;

public class MakeReservationCommandHandler {
    private final RoomRepository roomRepository;
    private ReservationRepository reservationRepository;

    public MakeReservationCommandHandler(ReservationRepository reservationRepository, RoomRepository roomRepository) {
        this.reservationRepository = reservationRepository;
        this.roomRepository = roomRepository;
    }

    public ReservationMade handle(MakeReservationCommand makeReservationCommand) {
        Room room = roomRepository.get(new RoomId(makeReservationCommand.getRoomNumber()));
        ReservationPeriod reservationPeriod = ReservationPeriod
                .from(makeReservationCommand.getCheckIn())
                .to(makeReservationCommand.getCheckoutOut());
        Reservation reservation = new Reservation(
                room,
                reservationPeriod,
                makeReservationCommand.getNumberOfGuests()
        );
        this.reservationRepository.add(reservation);
        return new ReservationMade(reservation);
    }
}
