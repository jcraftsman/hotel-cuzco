package hotel.cuzco.middleware;

public interface CommandHandler<R, C extends Command> {

    R handle(C command);

    Class listenTo();
}
