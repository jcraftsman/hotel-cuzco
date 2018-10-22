package hotel.cuzco.booking.infrastructure.database.inmemory;

import hotel.cuzco.booking.domain.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RoomInMemoryRepository implements RoomRepository {

    private Map<RoomId, Room> rooms;
    private ReservationRepository reservationRepository;

    public RoomInMemoryRepository(ReservationRepository reservationRepository) {
        this.rooms = new HashMap<>();
        this.reservationRepository = reservationRepository;
    }

    @Override
    public Room get(RoomId roomId) {
        return rooms.get(roomId);
    }

    @Override
    public void add(Room room) {
        rooms.put(room.id(), room);
        room.getActiveReservations().forEach(reservationRepository::add);
    }

    @Override
    public Iterable<Room> all() {
        return rooms.values();
    }

    @Override
    public Optional<Room> getByReservation(ReservationId reservationId) {
        var reservation = reservationRepository.get(reservationId);
        return reservation.map(Reservation::room);
    }

    public void addAll(Iterable<Room> allRooms) {
        allRooms.forEach(this::add);
    }

    public void deleteAll() {
        rooms.clear();
    }
}
