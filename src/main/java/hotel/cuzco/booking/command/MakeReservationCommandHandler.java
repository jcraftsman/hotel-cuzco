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
        Reservation reservation = new Reservation(room);
        this.reservationRepository.add(reservation);
        return new ReservationMade(reservation);
    }
}
