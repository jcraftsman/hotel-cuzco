package integration.database;

import hotel.cuzco.booking.domain.reservation.Room;
import hotel.cuzco.booking.domain.reservation.RoomId;
import hotel.cuzco.booking.infrastructure.database.redis.RoomRedisRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;
import redis.embedded.RedisServer;

import static org.assertj.core.api.Assertions.assertThat;

class RoomRedisRepositoryIntTest {

    private static final String ROOM_NUMBER = "N1";
    private static final String ROOM_DESCRIPTION = "A full description";
    private static final int ROOM_CAPACITY = 5;
    private static final int REDIS_PORT = 6379;
    private static final String REDIS_HOST = "localhost";

    private RoomRedisRepository roomRedisRepository;

    private RedisServer redisServer;
    private Jedis redisClient;

    @BeforeEach
    void setUp() {
        redisServer = RedisServer.builder()
                .port(REDIS_PORT)
                .build();
        redisServer.start();
        redisClient = new Jedis(REDIS_HOST, REDIS_PORT);
        roomRedisRepository = new RoomRedisRepository(redisClient);
    }

    @AfterEach
    void tearDown() {
        redisClient.close();
        redisServer.stop();
    }

    @Test
    void it_should_fetch_room_object_with_the_same_attributes_after_saving() {
        // Given
        Room savedRoom = new Room(ROOM_NUMBER, ROOM_DESCRIPTION, ROOM_CAPACITY);
        roomRedisRepository.save(savedRoom);

        // When
        Room fetchedRoom = roomRedisRepository.get(new RoomId(ROOM_NUMBER));

        // Then
        assertThat(fetchedRoom).isEqualToComparingFieldByFieldRecursively(savedRoom);
    }
}