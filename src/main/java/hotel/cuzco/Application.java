package hotel.cuzco;

import hotel.cuzco.booking.infrastructure.database.RoomRepositoryFactory;
import hotel.cuzco.booking.infrastructure.mailing.SmtpMailSender;
import hotel.cuzco.booking.infrastructure.web.BookingWebServer;
import hotel.cuzco.booking.usecase.Bootstrap;

public class Application {

    public static void main(String[] args) {
        var roomRepository = RoomRepositoryFactory.create();
        Bootstrap.setupCuzcoRooms(roomRepository);
        var mailSender = SmtpMailSender.build();
        BookingWebServer
                .create(roomRepository, mailSender)
                .start();
    }

}
