package hotel.cuzco.booking.domain;

import lombok.Data;

@Data
public class RoomId {
    private final String hotelId;
    private final String roomNumber;

    public RoomId(String hotelId, String roomNumber) {
        this.hotelId = hotelId;
        this.roomNumber = roomNumber;
    }

}
