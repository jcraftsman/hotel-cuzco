package hotel.cuzco.middleware.commands.fixtures;

import common.ddd.patterns.Command;
import lombok.Data;

@Data
public class OrderFlowersDeliveryCommand implements Command {

    private final int numberOfFlowers;
    private final String flowerType;

    public OrderFlowersDeliveryCommand(int numberOfFlowers, String flowerType) {
        this.numberOfFlowers = numberOfFlowers;
        this.flowerType = flowerType;
    }
}
