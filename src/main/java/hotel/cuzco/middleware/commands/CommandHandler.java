package hotel.cuzco.middleware.commands;

public interface CommandHandler<R, C extends Command> {

    R handle(C command);

    Class listenTo();
}
