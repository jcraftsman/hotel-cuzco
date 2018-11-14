package hotel.cuzco.middleware.commands.fixtures;

import common.ddd.patterns.CommandHandler;
import common.ddd.patterns.CommandResponse;
import hotel.cuzco.middleware.events.fixtures.FlowersDeliveryOrdered;

public class OrderFlowersDeliveryCommandHandler implements CommandHandler<CommandResponse<String>, OrderFlowersDeliveryCommand> {
    @Override
    public CommandResponse<String> handle(OrderFlowersDeliveryCommand command) {
        String deliveryMessage = command.getNumberOfFlowers() + " " + command.getFlowerType() + " flowers delivery ordered";
        return CommandResponse.<String>builder()
                .value(deliveryMessage)
                .event(new FlowersDeliveryOrdered(deliveryMessage))
                .build();
    }

    @Override
    public Class listenTo() {
        return OrderFlowersDeliveryCommand.class;
    }
}
