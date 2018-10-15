package integration.web.rest;

import com.eclipsesource.json.Json;
import hotel.cuzco.booking.domain.Hotel;
import hotel.cuzco.booking.infrastructure.database.inmemory.ReservationInMemoryRepository;
import hotel.cuzco.booking.infrastructure.database.inmemory.RoomInMemoryRepository;
import hotel.cuzco.booking.infrastructure.web.BookingWebServer;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;

class BookingRoutesTest {

    private static final int SERVER_PORT = 4567;
    private static final String[] ALL_ROOM_NUMBERS =
            {"101", "102", "103", "201", "202", "203", "204", "205", "301", "302", "303", "304"};
    private static final String ROOM_FOR_FIVE = "203";
    private static final String ONE_GUEST = "1";
    private static final int FIVE_GUESTS = 5;
    private static final String JAN_02_19 = "2019-01-02";
    private static final String JAN_01_19 = "2019-01-01";
    private static final String ROOM_NUMBER_PARAM = "room-number";
    private static final String CHECK_IN_PARAM = "check-in";
    private static final String CHECK_OUT_PARAM = "check-out";
    private static final String NUMBER_OF_GUESTS_PARAM = "number-of-guests";
    private static final String ROOM_FOR_FIVE_DESCRIPTION = "1 king size bed + 3 single beds - A/C - Wi-Fi - private bathroom";

    private static BookingWebServer bookingWebServer;
    private static RoomInMemoryRepository roomRepository;

    @BeforeAll
    static void globalSetup() {
        var reservationRepository = new ReservationInMemoryRepository();
        roomRepository = new RoomInMemoryRepository(reservationRepository);
        bookingWebServer = new BookingWebServer(roomRepository, SERVER_PORT);
        bookingWebServer.start();
        RestAssured.port = SERVER_PORT;
    }

    @AfterAll
    static void globalTearDown() {
        bookingWebServer.stop();
    }

    @BeforeEach
    void setUp() {
        roomRepository.deleteAll();
        roomRepository.addAll(Hotel.CUZCO().allRooms());
    }

    @Test
    void get_all_available_rooms_for_1() {
        given()
                .param(CHECK_IN_PARAM, JAN_01_19)
                .param(CHECK_OUT_PARAM, JAN_02_19)
                .param(NUMBER_OF_GUESTS_PARAM, ONE_GUEST)
        .when()
                .get("/rooms/available")
        .then()
                .statusCode(200)
                .contentType(JSON)
                .body("$.size()", is(12))
                .body("room-number", hasItems(ALL_ROOM_NUMBERS));
    }

    @Test
    void get_all_available_rooms_for_5() {
        given()
                .param(CHECK_IN_PARAM, JAN_01_19)
                .param(CHECK_OUT_PARAM, JAN_02_19)
                .param(NUMBER_OF_GUESTS_PARAM, FIVE_GUESTS)
        .when()
                .get("/rooms/available")
        .then()
                .statusCode(200)
                .contentType(JSON)
                .body("$.size()", is(1))
                .body("room-number", hasItem(ROOM_FOR_FIVE))
                .body("description", hasItem(ROOM_FOR_FIVE_DESCRIPTION))
                .body("capacity", hasItem(FIVE_GUESTS));
    }

    @Test
    void make_a_reservation() {
        given()
                .contentType(ContentType.JSON)
                .body(Json.object().
                        add(CHECK_IN_PARAM, JAN_01_19).
                        add(CHECK_OUT_PARAM, JAN_02_19).
                        add(NUMBER_OF_GUESTS_PARAM, FIVE_GUESTS).
                        add(ROOM_NUMBER_PARAM, ROOM_FOR_FIVE).toString())
        .when()
                .post("/reservation")
        .then()
                .statusCode(201)
                .contentType(JSON)
                .body("", hasKey("id"));
    }

    @Test
    void make_a_reservation_with_invalid_reservation_period() {
        given()
                .contentType(ContentType.JSON)
                .body(Json.object().
                        add(CHECK_IN_PARAM, JAN_02_19).
                        add(CHECK_OUT_PARAM, JAN_01_19).
                        add(NUMBER_OF_GUESTS_PARAM, FIVE_GUESTS).
                        add(ROOM_NUMBER_PARAM, ROOM_FOR_FIVE).toString())
        .when()
                .post("/reservation")
        .then()
                .statusCode(400)
                .contentType(JSON)
                .body("message", is("check-in date should be before the check-out date."));
    }

    @Test
    void make_a_reservation_for_less_than_one_night() {
        given()
                .contentType(ContentType.JSON)
                .body(Json.object().
                        add(CHECK_IN_PARAM, JAN_01_19).
                        add(CHECK_OUT_PARAM, JAN_01_19).
                        add(NUMBER_OF_GUESTS_PARAM, FIVE_GUESTS).
                        add(ROOM_NUMBER_PARAM, ROOM_FOR_FIVE).toString())
        .when()
                .post("/reservation")
        .then()
                .statusCode(400)
                .contentType(JSON)
                .body("message", is("Cannot checkout the same day. the reservation period should be at least for one night."));
    }

    @Test
    void make_a_reservation_for_an_unavailable_room() {
        given()
                .contentType(ContentType.JSON)
                .body(Json.object().
                        add(CHECK_IN_PARAM, JAN_01_19).
                        add(CHECK_OUT_PARAM, JAN_02_19).
                        add(NUMBER_OF_GUESTS_PARAM, FIVE_GUESTS).
                        add(ROOM_NUMBER_PARAM, ROOM_FOR_FIVE).toString())
                .post("/reservation");
        given()
                .contentType(ContentType.JSON)
                .body(Json.object().
                        add(CHECK_IN_PARAM, JAN_01_19).
                        add(CHECK_OUT_PARAM, JAN_02_19).
                        add(NUMBER_OF_GUESTS_PARAM, FIVE_GUESTS).
                        add(ROOM_NUMBER_PARAM, ROOM_FOR_FIVE).toString())
        .when()
                .post("/reservation")
        .then()
                .statusCode(403)
                .contentType(JSON)
                .body("message", is("Requested room is unavailable during the reservation period."));
    }
}
