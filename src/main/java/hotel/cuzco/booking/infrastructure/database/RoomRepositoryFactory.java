package hotel.cuzco.booking.infrastructure.database;

import hotel.cuzco.booking.domain.reservation.RoomRepository;
import hotel.cuzco.booking.infrastructure.database.redis.RoomRedisRepository;
import redis.clients.jedis.Jedis;

import java.net.URI;
import java.net.URISyntaxException;

public class RoomRepositoryFactory {

    private static final String REDIS_URL_ENV_VARIABLE = "REDIS_URL";

    public static RoomRepository create() {
        try {
            var redisURI = new URI(System.getenv(REDIS_URL_ENV_VARIABLE));
            var redisClient = new Jedis(redisURI);
            return new RoomRedisRepository(redisClient);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            throw new RuntimeException("Invalid URL set in environment variable REDIS_URL");
        }
    }
}
