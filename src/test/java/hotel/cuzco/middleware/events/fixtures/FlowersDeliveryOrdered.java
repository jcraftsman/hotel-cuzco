package hotel.cuzco.middleware.events.fixtures;

import common.ddd.patterns.Event;
import lombok.Data;

@Data
public class FlowersDeliveryOrdered implements Event {

    private final String flowersDeliveryOrdered;

    public FlowersDeliveryOrdered(String flowersDeliveryInformation) {
        this.flowersDeliveryOrdered = flowersDeliveryInformation;
    }
}
