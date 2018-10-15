package hotel.cuzco.booking.infrastructure.web.rest;

import com.eclipsesource.json.Json;
import hotel.cuzco.booking.command.MakeReservationCommandHandler;
import hotel.cuzco.booking.domain.InvalidReservationPeriodException;
import hotel.cuzco.booking.domain.OneNightReservationIsNotAllowedException;
import hotel.cuzco.booking.domain.RoomRepository;
import hotel.cuzco.booking.domain.UnavailableForReservationException;
import hotel.cuzco.booking.infrastructure.web.rest.api.RoomsAvailabilityApi;
import hotel.cuzco.booking.infrastructure.web.rest.api.RoomsReservationApi;
import hotel.cuzco.booking.query.GetAvailableRoomsQueryHandler;

import static org.eclipse.jetty.http.MimeTypes.Type.APPLICATION_JSON;
import static spark.Spark.*;

public class BookingRoutes {

    private final RoomRepository roomRepository;

    private RoomsAvailabilityApi roomsAvailabilityApi;
    private RoomsReservationApi roomsReservationApi;

    public BookingRoutes(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public void create() {
        createAPIs();
        get("/rooms/available", roomsAvailabilityApi::getAvailableRooms);
        post("/reservation", "application/json", roomsReservationApi::makeReservation);

        routeException(InvalidReservationPeriodException.class, 400);
        routeException(OneNightReservationIsNotAllowedException.class, 400);
        routeException(UnavailableForReservationException.class, 403);
    }

    private void createAPIs() {
        var getAvailableRoomsQueryHandler = new GetAvailableRoomsQueryHandler(this.roomRepository);
        roomsAvailabilityApi = new RoomsAvailabilityApi(getAvailableRoomsQueryHandler);
        var makeReservationCommandHandler = new MakeReservationCommandHandler(this.roomRepository);
        roomsReservationApi = new RoomsReservationApi(makeReservationCommandHandler);
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