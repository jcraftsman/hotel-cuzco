package hotel.cuzco.booking.infrastructure.web.rest.api;

import com.eclipsesource.json.Json;
import hotel.cuzco.booking.command.CancelReservationCommand;
import hotel.cuzco.booking.command.CancelReservationCommandHandler;
import hotel.cuzco.booking.command.MakeReservationCommand;
import hotel.cuzco.booking.command.MakeReservationCommandHandler;
import hotel.cuzco.booking.domain.ReservationId;
import hotel.cuzco.booking.domain.ReservationMade;
import hotel.cuzco.booking.infrastructure.web.rest.json.ReservationMadeDto;
import hotel.cuzco.middleware.commands.CommandDispatcher;
import hotel.cuzco.middleware.commands.CommandResponse;
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

class RoomsReservationApiTest {

    private static final int THREE_GUESTS = 3;
    private static final String DEC_25_19_STRING = "2019-12-25";
    private static final String JAN_05_20_STRING = "2020-01-05";
    private static final LocalDate DEC_25_19 = LocalDate.parse(DEC_25_19_STRING);
    private static final LocalDate JAN_05_20 = LocalDate.parse(JAN_05_20_STRING);
    private static final String NUMBER_101 = "101";
    private static final String GUEST_NAME = "Mr Quintino ALAZHAR";
    private static final String GUEST_EMAIL = "quintino.alazhar@mail.com";

    private RoomsReservationApi roomsReservationApi;

    private MakeReservationCommandHandler makeReservationCommandHandler;
    private Request request;
    private Response response;

    @BeforeEach
    void setUp() {
        makeReservationCommandHandler = mock(MakeReservationCommandHandler.class);
        var cancelReservationCommandHandler = mock(CancelReservationCommandHandler.class);
        var commandHandlers = asList(makeReservationCommandHandler, cancelReservationCommandHandler);
        given(cancelReservationCommandHandler.listenTo()).willReturn(CancelReservationCommand.class);
        given(makeReservationCommandHandler.listenTo()).willReturn(MakeReservationCommand.class);
        var commandDispatcher = new CommandDispatcher(commandHandlers);
        roomsReservationApi = new RoomsReservationApi(commandDispatcher);
        request = mock(Request.class);
        response = mock(Response.class);
    }

    @Test
    void makeReservation_should_handle_the_command_in_request_and_return_the_reservation_made_dto() {
        // Given
        given(request.body()).willReturn(Json.object()
                .add("room-number", NUMBER_101)
                .add("check-in", DEC_25_19_STRING)
                .add("check-out", JAN_05_20_STRING)
                .add("number-of-guests", THREE_GUESTS)
                .add("guest-name", GUEST_NAME)
                .add("guest-email", GUEST_EMAIL)
                .toString());

        var makeReservationCommand = new MakeReservationCommand(NUMBER_101, DEC_25_19, JAN_05_20, THREE_GUESTS, GUEST_NAME, GUEST_EMAIL);
        var reservationMade = ReservationMade.random();
        given(makeReservationCommandHandler.handle(makeReservationCommand))
                .willReturn(CommandResponse.<ReservationId>builder().value(reservationMade.id()).build());

        // When
        var actualReservationMade = roomsReservationApi.makeReservation(request, response);

        // Then
        then(response).should().status(201);
        then(response).should().type("application/json");
        assertThat(actualReservationMade).isEqualTo(new ReservationMadeDto(reservationMade.id()));
    }
}