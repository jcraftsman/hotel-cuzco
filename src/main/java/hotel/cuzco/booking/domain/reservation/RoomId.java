package hotel.cuzco.booking.domain.reservation;

import lombok.Data;

@Data
public class RoomId {
    private final String hotelId;
    private final String roomNumber;

    public RoomId(String roomNumber) {
        this(Hotel.CUZCO_HOTEL_ID, roomNumber);
    }

    private RoomId(String hotelId, String roomNumber) {
        this.hotelId = hotelId;
        this.roomNumber = roomNumber;
    }

}
