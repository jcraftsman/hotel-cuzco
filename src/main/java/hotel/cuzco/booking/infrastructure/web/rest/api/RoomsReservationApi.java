package hotel.cuzco.booking.infrastructure.web.rest.api;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import hotel.cuzco.booking.command.CancelReservationCommand;
import hotel.cuzco.booking.command.CancelReservationCommandHandler;
import hotel.cuzco.booking.command.MakeReservationCommand;
import hotel.cuzco.booking.command.MakeReservationCommandHandler;
import hotel.cuzco.booking.domain.ReservationMade;
import hotel.cuzco.booking.infrastructure.web.rest.json.ReservationMadeDto;
import spark.Request;
import spark.Response;

import java.time.LocalDate;

import static org.eclipse.jetty.http.HttpStatus.CREATED_201;
import static org.eclipse.jetty.http.HttpStatus.NO_CONTENT_204;
import static org.eclipse.jetty.http.MimeTypes.Type.APPLICATION_JSON;

public class RoomsReservationApi {
    private static final String ROOM_NUMBER_PARAM_KEY = "room-number";
    private static final String CHECK_IN_PARAM_KEY = "check-in";
    private static final String CHECK_OUT_PARAM_KEY = "check-out";
    private static final String NUMBER_OF_GUESTS_PARAM_KEY = "number-of-guests";

    private final MakeReservationCommandHandler makeReservationCommandHandler;
    private final CancelReservationCommandHandler cancelReservationCommandHandler;

    public RoomsReservationApi(MakeReservationCommandHandler makeReservationCommandHandler, CancelReservationCommandHandler cancelReservationCommandHandler) {
        this.makeReservationCommandHandler = makeReservationCommandHandler;
        this.cancelReservationCommandHandler = cancelReservationCommandHandler;
    }

    public ReservationMadeDto makeReservation(Request request, Response response) {
        ReservationMade reservationMade = makeReservationCommandHandler.handle(commandFrom(request));
        response.status(CREATED_201);
        response.type(APPLICATION_JSON.toString());
        return new ReservationMadeDto(reservationMade);
    }

    public String cancelReservation(Request request, Response response) {
        var reservationId = request.params("id");
        cancelReservationCommandHandler.handle(CancelReservationCommand.of(reservationId));
        response.status(NO_CONTENT_204);
        return "";
    }

    private MakeReservationCommand commandFrom(Request request) {
        JsonObject json = Json.parse(request.body()).asObject();
        return new MakeReservationCommand(
                json.getString(ROOM_NUMBER_PARAM_KEY, ""),
                parseLocalDate(json, CHECK_IN_PARAM_KEY),
                parseLocalDate(json, CHECK_OUT_PARAM_KEY),
                json.getInt(NUMBER_OF_GUESTS_PARAM_KEY, -1)
        );
    }

    private LocalDate parseLocalDate(JsonObject jsonObject, String dateAsString) {
        return LocalDate.parse(jsonObject.getString(dateAsString, ""));
    }
}