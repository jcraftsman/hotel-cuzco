package hotel.cuzco.middleware.commands;

public interface CommandHandler<R extends CommandResponse, C extends Command> {

    R handle(C command);

    Class listenTo();
}
