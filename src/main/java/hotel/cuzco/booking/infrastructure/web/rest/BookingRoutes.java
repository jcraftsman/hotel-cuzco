package hotel.cuzco.booking.infrastructure.web.rest;

import hotel.cuzco.booking.command.MakeReservationCommandHandler;
import hotel.cuzco.booking.domain.RoomRepository;
import hotel.cuzco.booking.infrastructure.web.rest.api.RoomsAvailabilityApi;
import hotel.cuzco.booking.infrastructure.web.rest.api.RoomsReservationApi;
import hotel.cuzco.booking.infrastructure.web.rest.json.JsonResponseTransformer;
import hotel.cuzco.booking.query.GetAvailableRoomsQueryHandler;

import static spark.Spark.get;
import static spark.Spark.post;

public class BookingRoutes {
    private final RoomRepository roomRepository;
    private RoomsAvailabilityApi roomsAvailabilityApi;
    private RoomsReservationApi roomsReservationApi;

    public BookingRoutes(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public void create() {
        createAPIs();
        var jsonTransformer = new JsonResponseTransformer();
        get("/rooms/available", roomsAvailabilityApi::getAvailableRooms, jsonTransformer);
        post("/reservation", "application/json", roomsReservationApi::makeReservation, jsonTransformer);
    }

    private void createAPIs() {
        var getAvailableRoomsQueryHandler = new GetAvailableRoomsQueryHandler(this.roomRepository);
        roomsAvailabilityApi = new RoomsAvailabilityApi(getAvailableRoomsQueryHandler);
        var makeReservationCommandHandler = new MakeReservationCommandHandler(this.roomRepository);
        roomsReservationApi = new RoomsReservationApi(makeReservationCommandHandler);
    }

}