package hotel.cuzco.booking.infrastructure.web.rest.api;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import hotel.cuzco.booking.command.CancelReservationCommand;
import hotel.cuzco.booking.command.MakeReservationCommand;
import hotel.cuzco.booking.domain.ReservationId;
import hotel.cuzco.booking.infrastructure.web.rest.json.ReservationMadeDto;
import hotel.cuzco.middleware.commands.CommandBus;
import hotel.cuzco.middleware.commands.CommandResponse;
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

    private final CommandBus commandDispatcher;

    public RoomsReservationApi(CommandBus commandDispatcher) {
        this.commandDispatcher = commandDispatcher;
    }

    public ReservationMadeDto makeReservation(Request request, Response response) {
        CommandResponse<ReservationId> commandResponse = commandDispatcher.dispatch(commandFrom(request));
        response.status(CREATED_201);
        response.type(APPLICATION_JSON.toString());
        return new ReservationMadeDto(commandResponse.getValue());
    }

    public String cancelReservation(Request request, Response response) {
        var reservationId = request.params("id");
        commandDispatcher.dispatch(CancelReservationCommand.of(reservationId));
        response.status(NO_CONTENT_204);
        return "";
    }

    private MakeReservationCommand commandFrom(Request request) {
        JsonObject json = Json.parse(request.body()).asObject();
        return new MakeReservationCommand(
                json.getString(ROOM_NUMBER_PARAM_KEY, ""),
                parseLocalDate(json, CHECK_IN_PARAM_KEY),
                parseLocalDate(json, CHECK_OUT_PARAM_KEY),
                json.getInt(NUMBER_OF_GUESTS_PARAM_KEY, -1),
                json.getString("guest-name", ""),
                json.getString("guest-email", "")
        );
    }

    private LocalDate parseLocalDate(JsonObject jsonObject, String dateAsString) {
        return LocalDate.parse(jsonObject.getString(dateAsString, ""));
    }
}