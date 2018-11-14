package common.ddd.patterns;


public interface EventHandler<E extends Event> {

    void handle(E event);

    Class listenTo();
}
