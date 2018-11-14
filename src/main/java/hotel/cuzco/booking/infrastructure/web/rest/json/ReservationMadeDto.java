package hotel.cuzco.booking.infrastructure.web.rest.json;

import com.eclipsesource.json.Json;
import hotel.cuzco.booking.domain.reservation.ReservationId;
import lombok.Data;

@Data
public class ReservationMadeDto {
    private String id;

    public ReservationMadeDto(ReservationId reservationId) {
        this.id = reservationId.getId();
    }

    @Override
    public String toString() {
        return Json.object()
                .add("id", id)
                .toString();
    }
}
