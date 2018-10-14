package hotel.cuzco.booking.infrastructure.web.rest.api;

import hotel.cuzco.booking.domain.Room;
import hotel.cuzco.booking.infrastructure.web.rest.json.RoomDto;
import hotel.cuzco.booking.query.GetAvailableRoomsQuery;
import hotel.cuzco.booking.query.GetAvailableRoomsQueryHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Request;
import spark.Response;

import java.time.LocalDate;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

class RoomsAvailabilityApiTest {

    private static final String SEP_01_20_STRING = "2020-09-01";
    private static final LocalDate SEP_01_20 = LocalDate.parse(SEP_01_20_STRING);
    private static final String SEP_12_20_STRING = "2020-09-12";
    private static final int TWO_GUESTS = 2;
    private static final LocalDate SEP_12_20 = LocalDate.parse("2020-09-12");

    private RoomsAvailabilityApi roomsAvailabilityApi;

    private GetAvailableRoomsQueryHandler availableRoomsQueryHandler;
    private Request request;
    private Response response;

    @BeforeEach
    void setUp() {
        availableRoomsQueryHandler = mock(GetAvailableRoomsQueryHandler.class);
        request = mock(Request.class);
        response = mock(Response.class);
        roomsAvailabilityApi = new RoomsAvailabilityApi(availableRoomsQueryHandler);
    }

    @Test
    void getAvailableRooms_should_return_room_dto_list_of_available_rooms_returned_by_the_query_handler() {
        // Given
        var firstAvailableRoom = new Room("1", "first available room", 5);
        var secondAvailableRoom = new Room("2", "second available room", 3);
        var getAvailableRoomsQuery = new GetAvailableRoomsQuery(SEP_01_20, SEP_12_20, TWO_GUESTS);
        given(availableRoomsQueryHandler.handle(getAvailableRoomsQuery))
                .willReturn(asList(firstAvailableRoom, secondAvailableRoom));
        given(request.queryParams("check-in")).willReturn(SEP_01_20_STRING);
        given(request.queryParams("check-out")).willReturn(SEP_12_20_STRING);
        given(request.queryParams("number-of-guests")).willReturn("2");

        // When
        var availableRoomsFromTheApi = roomsAvailabilityApi.getAvailableRooms(request, response);

        // Then
        then(response).should().type("application/json");
        assertThat(availableRoomsFromTheApi)
                .containsExactlyInAnyOrder(
                        new RoomDto(firstAvailableRoom),
                        new RoomDto(secondAvailableRoom)
                );
    }
}