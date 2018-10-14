package hotel.cuzco.booking.infrastructure.web.rest.api;

import hotel.cuzco.booking.domain.Room;
import hotel.cuzco.booking.infrastructure.web.rest.json.RoomDto;
import hotel.cuzco.booking.query.GetAvailableRoomsQuery;
import hotel.cuzco.booking.query.GetAvailableRoomsQueryHandler;
import spark.Request;
import spark.Response;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.eclipse.jetty.http.MimeTypes.Type.APPLICATION_JSON;

public class RoomsAvailabilityApi {
    private static final String CHECK_IN_PARAM_KEY = "check-in";
    private static final String CHECK_OUT_PARAM_KEY = "check-out";
    private static final String NUMBER_OF_GUESTS_PARAM_KEY = "number-of-guests";

    private GetAvailableRoomsQueryHandler getAvailableRoomsQueryHandler;

    public RoomsAvailabilityApi(GetAvailableRoomsQueryHandler getAvailableRoomsQueryHandler) {
        this.getAvailableRoomsQueryHandler = getAvailableRoomsQueryHandler;
    }

    public List<RoomDto> getAvailableRooms(Request request, Response response) {
        var getAvailableRoomsQuery = queryFrom(request);
        var rooms = getAvailableRoomsQueryHandler.handle(getAvailableRoomsQuery);
        response.type(APPLICATION_JSON.toString());
        return toRoomDtoList(rooms);
    }

    private GetAvailableRoomsQuery queryFrom(Request request) {
        return new GetAvailableRoomsQuery(
                LocalDate.parse(request.queryParams(CHECK_IN_PARAM_KEY)),
                LocalDate.parse(request.queryParams(CHECK_OUT_PARAM_KEY)),
                Integer.parseInt(request.queryParams(NUMBER_OF_GUESTS_PARAM_KEY))
        );
    }

    private List<RoomDto> toRoomDtoList(Iterable<Room> rooms) {
        return StreamSupport.stream(rooms.spliterator(), false)
                .map(RoomDto::new)
                .collect(Collectors.toList());
    }
}