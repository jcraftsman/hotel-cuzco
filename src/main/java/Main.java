import hotel.cuzco.booking.domain.Hotel;
import hotel.cuzco.booking.infrastructure.database.inmemory.ReservationInMemoryRepository;
import hotel.cuzco.booking.infrastructure.database.inmemory.RoomInMemoryRepository;
import hotel.cuzco.booking.infrastructure.mailing.MailGunSender;
import hotel.cuzco.booking.infrastructure.web.BookingWebServer;

public class Main {
    private static final Integer SERVER_PORT = 9986;

    public static void main(String[] args) {
        var roomRepository = setupCuzcoRoomsRepository();
        var mailSender = MailGunSender.build();
        var bookingRestApiServer = new BookingWebServer(SERVER_PORT, roomRepository, mailSender);
        bookingRestApiServer.start();
    }

    private static RoomInMemoryRepository setupCuzcoRoomsRepository() {
        var reservationRepository = new ReservationInMemoryRepository();
        var roomRepository = new RoomInMemoryRepository(reservationRepository);
        roomRepository.addAll(Hotel.CUZCO().allRooms());
        return roomRepository;
    }
}
