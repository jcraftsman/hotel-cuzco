package hotel.cuzco.booking.infrastructure.web;

import hotel.cuzco.booking.domain.ReservationRepository;
import hotel.cuzco.booking.domain.RoomRepository;
import hotel.cuzco.booking.infrastructure.database.inmemory.ReservationInMemoryRepository;
import hotel.cuzco.booking.infrastructure.web.rest.BookingRoutes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Spark;

import static spark.Spark.*;

public class BookingWebServer {
    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;
    private final Integer serverPort;
    private final Logger logger;

    public BookingWebServer(Integer serverPort, RoomRepository roomRepository, ReservationInMemoryRepository reservationRepository) {
        this.roomRepository = roomRepository;
        this.reservationRepository = reservationRepository;
        this.serverPort = serverPort;
        logger = LoggerFactory.getLogger(this.getClass());
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
        var bookingRoutes = new BookingRoutes(roomRepository, reservationRepository);
        bookingRoutes.create();
    }

    private void configureTechnicalRoutes() {
        get("health", ((request, response) -> "UP and RUNNING"));
    }

    private void configureErrorsLogging() {
        configurePotentialFailureOnStart();
        exception(Exception.class, (exception, request, response) -> {
            logger.error("Something went wrong", exception);
        });
    }

    private void configurePotentialFailureOnStart() {
        initExceptionHandler((exception) -> logger.error("Uh-oh", exception));
    }

}