package hotel.cuzco.booking.infrastructure.web.rest;

import com.eclipsesource.json.Json;
import hotel.cuzco.booking.command.CancelReservationCommandHandler;
import hotel.cuzco.booking.command.InvalidCommandException;
import hotel.cuzco.booking.command.MakeReservationCommandHandler;
import hotel.cuzco.booking.domain.*;
import hotel.cuzco.booking.infrastructure.web.rest.api.RoomsAvailabilityApi;
import hotel.cuzco.booking.infrastructure.web.rest.api.RoomsReservationApi;
import hotel.cuzco.booking.query.GetAvailableRoomsQueryHandler;
import hotel.cuzco.middleware.commands.CommandDispatcher;

import static java.util.Arrays.asList;
import static org.eclipse.jetty.http.MimeTypes.Type.APPLICATION_JSON;
import static spark.Spark.*;

public class BookingRoutes {

    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;

    private RoomsAvailabilityApi roomsAvailabilityApi;
    private RoomsReservationApi roomsReservationApi;

    public BookingRoutes(RoomRepository roomRepository, ReservationRepository reservationRepository) {
        this.roomRepository = roomRepository;
        this.reservationRepository = reservationRepository;
    }

    public void create() {
        createAPIs();
        get("/rooms/available", roomsAvailabilityApi::getAvailableRooms);
        post("/reservation", "application/json", roomsReservationApi::makeReservation);
        delete("/reservation/:id", "application/json", roomsReservationApi::cancelReservation);

        routeException(InvalidReservationPeriodException.class, 400);
        routeException(OneNightReservationIsNotAllowedException.class, 400);
        routeException(UnavailableForReservationException.class, 403);
        routeException(InvalidCommandException.class, 400);
    }

    private void createAPIs() {
        var getAvailableRoomsQueryHandler = new GetAvailableRoomsQueryHandler(this.roomRepository);
        roomsAvailabilityApi = new RoomsAvailabilityApi(getAvailableRoomsQueryHandler);
        var makeReservationCommandHandler = new MakeReservationCommandHandler(this.roomRepository);
        var cancelReservationCommandHandler = new CancelReservationCommandHandler(reservationRepository);
        var commandDispatcher = new CommandDispatcher(asList(makeReservationCommandHandler, cancelReservationCommandHandler));
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