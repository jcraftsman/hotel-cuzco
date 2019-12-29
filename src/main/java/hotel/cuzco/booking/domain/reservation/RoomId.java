package hotel.cuzco.booking.domain.reservation;

import lombok.Data;

@Data
public class RoomId {
    private static final String ID_PARTS_SEPARATOR = "-";

    private final String hotelId;
    private final String roomNumber;

    public RoomId(String roomNumber) {
        this(Hotel.CUZCO_HOTEL_ID, roomNumber);
    }

    private RoomId(String hotelId, String roomNumber) {
        this.hotelId = hotelId;
        this.roomNumber = roomNumber;
    }

    public static RoomId parse(String roomIdStr) {
        String[] idParts = roomIdStr.split(ID_PARTS_SEPARATOR);
        return new RoomId(idParts[0], idParts[1]);
    }

    @Override
    public String toString() {
        return hotelId + ID_PARTS_SEPARATOR + roomNumber;
    }
}
