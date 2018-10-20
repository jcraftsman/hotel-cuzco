package hotel.cuzco.middleware.events.fixtures;

import hotel.cuzco.middleware.events.Event;
import lombok.Data;

@Data
public class FlowersDeliveryOrdered implements Event {

    private final String flowersDeliveryOrdered;

    public FlowersDeliveryOrdered(String flowersDeliveryOrdered) {
        this.flowersDeliveryOrdered = flowersDeliveryOrdered;
    }
}
