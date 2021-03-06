package hotel.cuzco.middleware.events.fixtures;

import common.ddd.patterns.EventHandler;

public class FlowersDeliveryOrdersStatsUpdater implements EventHandler<FlowersDeliveryOrdered> {

    private final StatsRepository statsRepository;

    public FlowersDeliveryOrdersStatsUpdater(StatsRepository statsRepository) {
        this.statsRepository = statsRepository;
    }

    @Override
    public void handle(FlowersDeliveryOrdered flowersDeliveryOrdered) {
        statsRepository.save(flowersDeliveryOrdered);
    }

    @Override
    public Class listenTo() {
        return FlowersDeliveryOrdered.class;
    }
}
