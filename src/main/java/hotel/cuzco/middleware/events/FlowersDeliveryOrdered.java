package hotel.cuzco.middleware.events;

import lombok.Data;

@Data
public class FlowersDeliveryOrdered implements Event {

    private final String flowersDeliveryOrdered;

    public FlowersDeliveryOrdered(String flowersDeliveryOrdered) {
        this.flowersDeliveryOrdered = flowersDeliveryOrdered;
    }
}
