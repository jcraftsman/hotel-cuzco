package hotel.cuzco.booking.infrastructure.web.rest;

import com.eclipsesource.json.Json;
import hotel.cuzco.booking.domain.exceptions.InvalidReservationPeriodException;
import hotel.cuzco.booking.domain.exceptions.OneNightReservationIsNotAllowedException;
import hotel.cuzco.booking.domain.exceptions.UnavailableForReservationException;
import hotel.cuzco.booking.domain.notification.MailSender;
import hotel.cuzco.booking.domain.reservation.RoomRepository;
import hotel.cuzco.booking.infrastructure.web.rest.api.RoomsAvailabilityApi;
import hotel.cuzco.booking.infrastructure.web.rest.api.RoomsReservationApi;
import hotel.cuzco.booking.usecase.command.CommandBusFactory;
import hotel.cuzco.booking.usecase.command.InvalidCommandException;
import hotel.cuzco.booking.usecase.query.GetAvailableRoomsQueryHandler;

import static org.eclipse.jetty.http.MimeTypes.Type.APPLICATION_JSON;
import static spark.Spark.*;

public class BookingRoutes {

    private final RoomRepository roomRepository;
    private final MailSender mailSender;

    private RoomsAvailabilityApi roomsAvailabilityApi;
    private RoomsReservationApi roomsReservationApi;

    public BookingRoutes(RoomRepository roomRepository, MailSender mailSender) {
        this.roomRepository = roomRepository;
        this.mailSender = mailSender;
    }

    public void create() {
        createAPIs();
        get("/rooms/available", roomsAvailabilityApi::getAvailableRooms);
        post("/reservation", "application/json", roomsReservationApi::makeReservation);
        delete("/reservation/:id", roomsReservationApi::cancelReservation);

        routeException(InvalidReservationPeriodException.class, 400);
        routeException(OneNightReservationIsNotAllowedException.class, 400);
        routeException(UnavailableForReservationException.class, 403);
        routeException(InvalidCommandException.class, 400);
    }

    private void createAPIs() {
        var getAvailableRoomsQueryHandler = new GetAvailableRoomsQueryHandler(this.roomRepository);
        roomsAvailabilityApi = new RoomsAvailabilityApi(getAvailableRoomsQueryHandler);
        var commandDispatcher = CommandBusFactory.build(roomRepository, mailSender);
        roomsReservationApi = new RoomsReservationApi(commandDispatcher);
    }

    private void routeException(Class<? extends Exception> exceptionClass, int statusCode) {
        exception(exceptionClass, (exception, request, response) -> {
            response.status(statusCode);
            response.type(APPLICATION_JSON.toString());
            String messageBody = Json.object()
                    .add("message", exception.getMessage())
                    .toString();
            response.body(messageBody);
        });
    }

}