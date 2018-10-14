import hotel.cuzco.booking.domain.Hotel;
import hotel.cuzco.booking.infrastructure.database.inmemory.ReservationInMemoryRepository;
import hotel.cuzco.booking.infrastructure.database.inmemory.RoomInMemoryRepository;
import hotel.cuzco.booking.infrastructure.web.BookingWebServer;

public class Main {
    private static final Integer SERVER_PORT = 9986;

    public static void main(String[] args) {
        var roomRepository = new RoomInMemoryRepository(new ReservationInMemoryRepository());
        roomRepository.addAll(Hotel.CUZCO().allRooms());
        var bookingRestApiServer = new BookingWebServer(roomRepository, SERVER_PORT);
        bookingRestApiServer.start();
    }
}
