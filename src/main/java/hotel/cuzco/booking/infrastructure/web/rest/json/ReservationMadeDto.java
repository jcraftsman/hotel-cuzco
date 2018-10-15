package hotel.cuzco.booking.infrastructure.web.rest.json;

import com.eclipsesource.json.Json;
import hotel.cuzco.booking.domain.ReservationMade;
import lombok.Data;

@Data
public class ReservationMadeDto {
    private String id;

    public ReservationMadeDto(ReservationMade reservationMade) {
        this.id = reservationMade.id().getId();
    }

    @Override
    public String toString() {
        return Json.object()
                .add("id", id)
                .toString();
    }
}
