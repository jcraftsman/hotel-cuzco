package hotel.cuzco.booking.infrastructure.web.rest.json;

import hotel.cuzco.booking.domain.ReservationMade;
import lombok.Data;

@Data
public class ReservationMadeDto {
    private String id;

    public ReservationMadeDto(ReservationMade reservationMade) {
        this.id = reservationMade.id().getId();
    }
}
