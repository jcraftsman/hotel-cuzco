package hotel.cuzco.middleware.commands;

public interface CommandBus {
    <R extends CommandResponse, C extends Command> R dispatch(C command);
}
