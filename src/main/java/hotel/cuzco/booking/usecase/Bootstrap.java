package hotel.cuzco.booking.usecase;

import hotel.cuzco.booking.domain.reservation.Hotel;
import hotel.cuzco.booking.domain.reservation.RoomRepository;

public class Bootstrap {
    public static void setupCuzcoRooms(RoomRepository roomRepository) {
        if (roomRepository.isEmpty()) {
            Hotel.CUZCO().allRooms().forEach(roomRepository::save);
        }
    }
}