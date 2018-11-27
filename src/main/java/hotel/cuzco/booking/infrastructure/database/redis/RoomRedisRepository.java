package hotel.cuzco.booking.infrastructure.database.redis;

import hotel.cuzco.booking.domain.reservation.*;
import redis.clients.jedis.Jedis;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;

public class RoomRedisRepository implements RoomRepository {

    private static final String ROOM_NUMBER_KEY = "roomNumber";
    private static final String ROOM_DESCRIPTION_KEY = "description";
    private static final String ROOM_CAPACITY_KEY = "capacity";
    private static final String RESERVATION_ROOM_ID_KEY = "roomId";
    private static final String RESERVATION_NUMBER_OF_GUESTS_KEY = "numberOfGuests";
    private static final String RESERVATION_CHECK_IN_DATE_KEY = "checkInDate";
    private static final String RESERVATION_CHECK_OUT_DATE_KEY = "checkOutDate";
    private static final String RESERVATION_CONTACT_FULL_NAME_KEY = "fullName";
    private static final String RESERVATION_CONTACT_EMAIL_KEY = "email";
    private static final String ALL_ROOMS_INDEX_KEY = "rooms";

    private final Jedis redisClient;

    public RoomRedisRepository(Jedis redisClient) {
        this.redisClient = redisClient;
    }

    @Override
    public Room get(RoomId roomId) {
        Map<String, String> roomFields = redisClient.hgetAll(keyOfRoom(roomId));
        String roomNumber = roomFields.get(ROOM_NUMBER_KEY);
        String description = roomFields.get(ROOM_DESCRIPTION_KEY);
        int capacity = parseInt(roomFields.get(ROOM_CAPACITY_KEY));
        List<Reservation> activeReservations = getActiveReservations(roomId);
        return Room.build(roomNumber, description, capacity, activeReservations);
    }

    @Override
    public void save(Room room) {
        String roomKey = keyOfRoom(room.id());
        saveRoomDirectFields(room, roomKey);
        saveRoomActiveReservations(room.id(), room.getActiveReservations());
        redisClient.sadd(ALL_ROOMS_INDEX_KEY, room.id().toString());
    }

    @Override
    public Iterable<Room> all() {
        return redisClient.smembers(ALL_ROOMS_INDEX_KEY)
                .stream().map(roomIdStr -> get(RoomId.parse(roomIdStr)))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Room> getByReservation(ReservationId reservationId) {
        String reservationKey = keyOfReservation(reservationId.toString());
        String serializedRoomId = redisClient.hget(reservationKey, RESERVATION_ROOM_ID_KEY);
        return Optional.ofNullable(serializedRoomId)
                .map(roomId -> get(RoomId.parse(roomId)));
    }

    private List<Reservation> getActiveReservations(RoomId roomId) {
        List<String> activeReservationIds = redisClient.lrange(keyOfRoomActiveReservations(roomId), 0, -1);
        return activeReservationIds.stream()
                .map(reservationId -> getReservation(reservationId, roomId))
                .collect(Collectors.toList());
    }

    private Reservation getReservation(String reservationId, RoomId roomId) {
        return toReservation(redisClient.hgetAll(keyOfReservation(reservationId)), reservationId, roomId);
    }

    private Reservation toReservation(Map<String, String> reservationFields, String reservationId, RoomId roomId) {
        int numberOfGuests = parseInt(reservationFields.get(RESERVATION_NUMBER_OF_GUESTS_KEY));
        LocalDate checkInDate = LocalDate.parse(reservationFields.get(RESERVATION_CHECK_IN_DATE_KEY));
        LocalDate checkOutDate = LocalDate.parse(reservationFields.get(RESERVATION_CHECK_OUT_DATE_KEY));
        String fullName = reservationFields.get(RESERVATION_CONTACT_FULL_NAME_KEY);
        String email = reservationFields.get(RESERVATION_CONTACT_EMAIL_KEY);
        return Reservation.deserialize(reservationId, roomId, checkInDate, checkOutDate, numberOfGuests, fullName, email);
    }

    private void saveRoomDirectFields(Room room, String roomKey) {
        Map<String, String> roomFieldsValues = mapRoomDirectFields(room);
        redisClient.hmset(roomKey, roomFieldsValues);
    }

    private void saveRoomActiveReservations(RoomId roomId, Collection<Reservation> activeReservations) {
        activeReservations.forEach(this::saveReservation);
        updateRoomActiveReservationList(roomId, activeReservations);
    }

    private Map<String, String> mapRoomDirectFields(Room room) {
        Map<String, String> roomFieldsValues = new HashMap<>();
        roomFieldsValues.put(ROOM_NUMBER_KEY, room.id().getRoomNumber());
        roomFieldsValues.put(ROOM_DESCRIPTION_KEY, room.getDescription());
        roomFieldsValues.put(ROOM_CAPACITY_KEY, String.valueOf(room.getCapacity()));
        return roomFieldsValues;
    }

    private void saveReservation(Reservation reservation) {
        Map<String, String> reservationFields = mapReservationFields(reservation);
        String reservationKey = keyOfReservation(reservation.id().toString());
        redisClient.hmset(reservationKey, reservationFields);
    }

    private void updateRoomActiveReservationList(RoomId roomId, Collection<Reservation> activeReservations) {
        redisClient.del(keyOfRoomActiveReservations(roomId));
        if (!activeReservations.isEmpty()) {
            String[] activeReservationIds = activeReservations.stream()
                    .map(reservation -> reservation.id().toString())
                    .toArray(String[]::new);
            redisClient.lpush(keyOfRoomActiveReservations(roomId), activeReservationIds);
        }
    }

    private Map<String, String> mapReservationFields(Reservation reservation) {
        Map<String, String> reservationFields = new HashMap<>();
        reservationFields.put(RESERVATION_ROOM_ID_KEY, reservation.roomId().toString());
        reservationFields.put(RESERVATION_NUMBER_OF_GUESTS_KEY, reservation.numberOfGuests().toString());
        reservationFields.put(RESERVATION_CHECK_IN_DATE_KEY, reservation.period().getCheckInDate().toString());
        reservationFields.put(RESERVATION_CHECK_OUT_DATE_KEY, reservation.period().getCheckOutDate().toString());
        reservationFields.put(RESERVATION_CONTACT_FULL_NAME_KEY, reservation.mainContact().getFullName());
        reservationFields.put(RESERVATION_CONTACT_EMAIL_KEY, reservation.mainContact().getEmail());
        return reservationFields;
    }

    private static String keyOfRoom(RoomId roomId) {
        return "room:" + roomId;
    }

    private static String keyOfReservation(String reservationId) {
        return "reservation:" + reservationId;
    }

    private static String keyOfRoomActiveReservations(RoomId roomId) {
        return "roomActiveReservations:" + roomId;
    }
}
