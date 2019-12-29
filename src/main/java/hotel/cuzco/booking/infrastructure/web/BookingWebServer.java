package hotel.cuzco.booking.infrastructure.web;

import hotel.cuzco.booking.domain.notification.MailSender;
import hotel.cuzco.booking.domain.reservation.RoomRepository;
import hotel.cuzco.booking.infrastructure.web.rest.BookingRoutes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Spark;

import static spark.Spark.*;

public class BookingWebServer {
    private static final String SERVER_PORT_ENV_VARIABLE = "PORT";

    private final RoomRepository roomRepository;
    private final Integer serverPort;
    private final Logger logger;
    private final MailSender mailSender;

    public BookingWebServer(Integer serverPort, RoomRepository roomRepository, MailSender mailSender) {
        this.roomRepository = roomRepository;
        this.serverPort = serverPort;
        this.mailSender = mailSender;
        logger = LoggerFactory.getLogger(this.getClass());
    }

    public static BookingWebServer create(RoomRepository roomRepository, MailSender mailSender) {
        int serverPort = Integer.parseInt(System.getenv(SERVER_PORT_ENV_VARIABLE));
        return new BookingWebServer(serverPort, roomRepository, mailSender);
    }

    public void start() {
        port(serverPort);
        configureErrorsLogging();
        configureRoutes();
    }

    public void stop() {
        Spark.stop();
    }

    private void configureRoutes() {
        configureTechnicalRoutes();
        configureBookingRoutes();
    }

    private void configureBookingRoutes() {
        var bookingRoutes = new BookingRoutes(roomRepository, mailSender);
        bookingRoutes.create();
    }

    private void configureTechnicalRoutes() {
        get("health", ((request, response) -> "UP and RUNNING"));
    }

    private void configureErrorsLogging() {
        configurePotentialFailureOnStart();
        exception(Exception.class, (exception, request, response) -> logger.error("Something went wrong", exception));
    }

    private void configurePotentialFailureOnStart() {
        initExceptionHandler((exception) -> logger.error("Uh-oh", exception));
    }

}
