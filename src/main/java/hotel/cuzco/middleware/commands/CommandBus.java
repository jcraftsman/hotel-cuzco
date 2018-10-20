package hotel.cuzco.middleware.commands;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CommandBus {
    private final Map<Class, CommandHandler> commandHandlers;

    public CommandBus(List<? extends CommandHandler> commandHandlers) {
        this.commandHandlers = commandHandlers.stream().collect(Collectors
                .toMap(CommandHandler::listenTo, commandHandler -> commandHandler));
    }

    public <R, C extends Command> R dispatch(C command) {
        CommandHandler<R, C> commandHandler = this.commandHandlers.get(command.getClass());
        return commandHandler.handle(command);
    }
}
