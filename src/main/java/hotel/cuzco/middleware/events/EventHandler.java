package hotel.cuzco.middleware.events;


public interface EventHandler<E extends Event> {

    void handle(E event);

    Class listenTo();
}
