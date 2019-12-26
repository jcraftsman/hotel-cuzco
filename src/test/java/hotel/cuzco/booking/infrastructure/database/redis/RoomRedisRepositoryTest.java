package hotel.cuzco.booking.infrastructure.database.redis;

import hotel.cuzco.booking.domain.command.MakeReservationCommand;
import hotel.cuzco.booking.domain.reservation.ReservationId;
import hotel.cuzco.booking.domain.reservation.Room;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Map.entry;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

class RoomRedisRepositoryTest {
    private static final String ROOM_DESCRIPTION = "describe room A";
    private static final String ROOM_NUMBER = "A";
    private static final int TWO_GUESTS = 2;
    private static final int THREE_GUESTS = 3;
    private static final int FIVE_GUESTS_CAPACITY = 5;
    private static final LocalDate JAN_1ST_19 = LocalDate.parse("2019-01-01");
    private static final LocalDate JAN_15_19 = LocalDate.parse("2019-01-15");
    private static final LocalDate FEB_15_19 = LocalDate.parse("2019-02-15");
    private static final LocalDate FEB_20_19 = LocalDate.parse("2019-02-20");
    private static final String GUEST_NAME = "Mr toto";
    private static final String GUEST_EMAIL = "toto@gmail.com";
    private static final String ANOTHER_GUEST_NAME = "Mr titi";
    private static final String ANOTHER_GUEST_EMAIL = "titi@gmail.com";

    private RoomRedisRepository roomRedisRepository;

    private Jedis redisClient;

    private Room room;
    private String roomKey;
    private Map<String, String> roomFieldsValues;

    @BeforeEach
    void setUp() {
        redisClient = mock(Jedis.class);
        roomRedisRepository = new RoomRedisRepository(redisClient);
        room = new Room(ROOM_NUMBER, ROOM_DESCRIPTION, FIVE_GUESTS_CAPACITY);
        roomKey = "room:" + room.id();
        roomFieldsValues = Map.ofEntries(
                entry("roomNumber", ROOM_NUMBER),
                entry("description", ROOM_DESCRIPTION),
                entry("capacity", "5")
        );
    }

    @Test
    void save_should_set_a_new_key_with_roomId_and_a_hash_with_room_fields() {
        // When
        roomRedisRepository.save(room);

        // Then
        then(redisClient).should().hmset(roomKey, roomFieldsValues);
    }

    @Test
    void save_should_add_roomId_to_rooms_set() {
        // Given
        String roomId = room.id().toString();

        // When
        roomRedisRepository.save(room);

        // Then
        then(redisClient).should().sadd("rooms", roomId);
    }

    @Test
    void get_should_return_a_room_object_with_all_its_fields_mapped_from_stored_hash() {
        // Given
        given(redisClient.hgetAll(roomKey)).willReturn(roomFieldsValues);

        // When
        Room roomFetchedFromRedis = roomRedisRepository.get(room.id());

        // Then
        assertThat(roomFetchedFromRedis).isEqualToComparingFieldByField(room);
    }

    @Test
    void save_should_set_active_reservation_related_to_room() {
        // Given
        var reservationId = makeReservation(JAN_1ST_19, JAN_15_19, THREE_GUESTS, GUEST_NAME, GUEST_EMAIL);

        // When
        roomRedisRepository.save(room);

        // Then
        var reservationKey = "reservation:" + reservationId;
        var reservationFieldsValues = Map.ofEntries(
                entry("roomId", room.id().toString()),
                entry("numberOfGuests", "3"),
                entry("checkInDate", "2019-01-01"),
                entry("checkOutDate", "2019-01-15"),
                entry("fullName", GUEST_NAME),
                entry("email", GUEST_EMAIL)
        );
        then(redisClient).should().hmset(reservationKey, reservationFieldsValues);
    }

    @Test
    void save_should_update_active_room_reservations_list() {
        // Given
        var firstReservationId = makeReservation(JAN_1ST_19, JAN_15_19, THREE_GUESTS, GUEST_NAME, GUEST_EMAIL);
        var secondReservationId = makeReservation(FEB_15_19, FEB_20_19, TWO_GUESTS, GUEST_NAME, GUEST_EMAIL);

        // When
        roomRedisRepository.save(room);

        // Then
        var inOrder = inOrder(redisClient);
        var roomActiveReservationsKey = "roomActiveReservations:" + room.id();
        then(redisClient).should(inOrder).del(roomActiveReservationsKey);
        then(redisClient).should(inOrder).lpush(roomActiveReservationsKey, firstReservationId, secondReservationId);
    }

    @Test
    void save_should_set_all_active_reservations_related_to_room() {
        // Given
        var firstReservationId = makeReservation(JAN_1ST_19, JAN_15_19, THREE_GUESTS, GUEST_NAME, GUEST_EMAIL);
        var secondReservationId = makeReservation(FEB_15_19, FEB_20_19, TWO_GUESTS, ANOTHER_GUEST_NAME, ANOTHER_GUEST_EMAIL);

        // When
        roomRedisRepository.save(room);

        // Then
        var reservationKey = "reservation:" + firstReservationId;
        var reservationFieldsValues = Map.ofEntries(
                entry("roomId", room.id().toString()),
                entry("numberOfGuests", "3"),
                entry("checkInDate", "2019-01-01"),
                entry("checkOutDate", "2019-01-15"),
                entry("fullName", GUEST_NAME),
                entry("email", GUEST_EMAIL)
        );
        var secondReservationKey = "reservation:" + secondReservationId;
        var secondReservationFieldsValues = Map.ofEntries(
                entry("roomId", room.id().toString()),
                entry("numberOfGuests", "2"),
                entry("checkInDate", "2019-02-15"),
                entry("checkOutDate", "2019-02-20"),
                entry("fullName", ANOTHER_GUEST_NAME),
                entry("email", ANOTHER_GUEST_EMAIL)
        );
        then(redisClient).should().hmset(reservationKey, reservationFieldsValues);
        then(redisClient).should().hmset(secondReservationKey, secondReservationFieldsValues);
    }

    @Test
    void get_should_return_a_room_object_with_all_active_reservations_related_to_room() {
        // Given
        given(redisClient.hgetAll(roomKey)).willReturn(roomFieldsValues);

        var firstReservationId = makeReservation(JAN_1ST_19, JAN_15_19, THREE_GUESTS, GUEST_NAME, GUEST_EMAIL);
        var secondReservationId = makeReservation(FEB_15_19, FEB_20_19, TWO_GUESTS, ANOTHER_GUEST_NAME, ANOTHER_GUEST_EMAIL);
        var roomActiveReservationsKey = "roomActiveReservations:" + room.id();
        List<String> activeReservationIds = List.of(firstReservationId, secondReservationId);
        given(redisClient.lrange(roomActiveReservationsKey, 0, -1)).willReturn(activeReservationIds);

        var firstReservationKey = "reservation:" + firstReservationId;
        var firstReservationFieldsValues = Map.ofEntries(
                entry("roomId", room.id().toString()),
                entry("numberOfGuests", "3"),
                entry("checkInDate", "2019-01-01"),
                entry("checkOutDate", "2019-01-15"),
                entry("fullName", GUEST_NAME),
                entry("email", GUEST_EMAIL)
        );
        var secondReservationKey = "reservation:" + secondReservationId;
        var secondReservationFieldsValues = Map.ofEntries(
                entry("roomId", room.id().toString()),
                entry("numberOfGuests", "2"),
                entry("checkInDate", "2019-02-15"),
                entry("checkOutDate", "2019-02-20"),
                entry("fullName", ANOTHER_GUEST_NAME),
                entry("email", ANOTHER_GUEST_EMAIL)
        );
        given(redisClient.hgetAll(firstReservationKey)).willReturn(firstReservationFieldsValues);
        given(redisClient.hgetAll(secondReservationKey)).willReturn(secondReservationFieldsValues);


        // When
        Room roomFetchedFromRedis = roomRedisRepository.get(room.id());

        // Then
        assertThat(roomFetchedFromRedis).isEqualToComparingFieldByFieldRecursively(room);

    }

    @Test
    void save_should_never_try_to_save_reservations_if_there_is_no_active_reservation_related_to_the_room() {
        // When
        roomRedisRepository.save(room);

        // Then
        then(redisClient).should().del(anyString());
        then(redisClient).should(never()).lpush(anyString());
    }

    @Test
    void all_should_return_a_collection_of_all_room_objects() {
        // Given
        given(redisClient.hgetAll(roomKey)).willReturn(roomFieldsValues);
        Set<String> allRoomIdsInRedis = Set.of(room.id().toString());
        given(redisClient.smembers("rooms")).willReturn(allRoomIdsInRedis);

        // When
        var allRoomsFetchedFromRedis = roomRedisRepository.all();

        // Then
        assertThat(allRoomsFetchedFromRedis.iterator().next()).isEqualToComparingFieldByFieldRecursively(room);
    }

    @Test
    void getByReservation_should_be_empty_when_reservation_key_does_not_exist() {
        // Given
        String unknownReservationKey = "reservation:unknown";
        given(redisClient.hget(unknownReservationKey, "roomId")).willReturn(null);


        // When
        var roomFetchedByReservation = roomRedisRepository.getByReservation(new ReservationId("unknown"));

        // Then
        assertThat(roomFetchedByReservation).isEmpty();

    }

    @Test
    void getByReservation_should_return_room_object_related_to_given_reservation() {
        // Given
        var reservationId = makeReservation(JAN_1ST_19, JAN_15_19, THREE_GUESTS, GUEST_NAME, GUEST_EMAIL);
        given(redisClient.hgetAll(roomKey)).willReturn(roomFieldsValues);
        var reservationKey = "reservation:" + reservationId;
        var reservationFieldsValues = Map.ofEntries(
                entry("numberOfGuests", "3"),
                entry("checkInDate", "2019-01-01"),
                entry("checkOutDate", "2019-01-15"),
                entry("fullName", GUEST_NAME),
                entry("email", GUEST_EMAIL)
        );
        given(redisClient.hgetAll(reservationKey)).willReturn(reservationFieldsValues);
        var roomActiveReservationsKey = "roomActiveReservations:" + room.id();
        given(redisClient.lrange(roomActiveReservationsKey, 0, -1))
                .willReturn(List.of(reservationId));

        given(redisClient.hget(reservationKey, "roomId"))
                .willReturn(room.id().toString());


        // When
        var roomFetchedByReservation = roomRedisRepository.getByReservation(new ReservationId(reservationId));

        // Then
        assertThat(roomFetchedByReservation.get()).isEqualToComparingFieldByFieldRecursively(room);

    }

    private String makeReservation(LocalDate jan1st19, LocalDate jan1519, int threeGuests, String guestName, String guestEmail) {
        var command = new MakeReservationCommand(ROOM_NUMBER, jan1st19, jan1519, threeGuests, guestName, guestEmail);
        var firstReservationResponse = room.makeReservation(command);
        return firstReservationResponse.getValue().toString();
    }
}