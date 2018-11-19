package hotel.cuzco.booking.infrastructure.database;

import hotel.cuzco.booking.domain.reservation.RoomRepository;
import hotel.cuzco.booking.infrastructure.database.inmemory.ReservationInMemoryRepository;
import hotel.cuzco.booking.infrastructure.database.inmemory.RoomInMemoryRepository;

public class RoomRepositoryFactory {

    public static RoomRepository create() {
        var reservationRepository = new ReservationInMemoryRepository();
        return new RoomInMemoryRepository(reservationRepository);
    }
}
