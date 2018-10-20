package hotel.cuzco.middleware.commands.fixtures;

import hotel.cuzco.middleware.commands.CommandHandler;

public class OrderFlowersDeliveryCommandHandler implements CommandHandler<String, OrderFlowersDeliveryCommand> {
    @Override
    public String handle(OrderFlowersDeliveryCommand command) {
        return command.getNumberOfFlowers() + " " + command.getFlowerType() + " flowers delivery ordered";
    }

    @Override
    public Class listenTo() {
        return OrderFlowersDeliveryCommand.class;
    }
}
