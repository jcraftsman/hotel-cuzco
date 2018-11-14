package hotel.cuzco.middleware.commands;

import common.ddd.patterns.Command;
import common.ddd.patterns.CommandBus;
import common.ddd.patterns.CommandBusMiddleware;
import common.ddd.patterns.CommandResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalDateTime;

public class LoggerMiddleware implements CommandBusMiddleware {

    private final CommandBus nextCommandBus;
    private final Logger logger;

    LoggerMiddleware(CommandBus nextCommandBus, Logger logger) {
        this.nextCommandBus = nextCommandBus;
        this.logger = logger;
    }

    public static LoggerMiddleware create(CommandBus nextCommandBus) {
        return new LoggerMiddleware(nextCommandBus, LoggerFactory.getLogger(LoggerMiddleware.class));
    }

    @Override
    public <R extends CommandResponse, C extends Command> R dispatch(C command) {
        var beforeDispatching = LocalDateTime.now();
        R dispatchedCommandResponse = nextCommandBus.dispatch(command);
        var commandHandlingDuration = Duration.between(LocalDateTime.now(), beforeDispatching);
        logger.info(durationLogMessage(command, commandHandlingDuration));
        return dispatchedCommandResponse;
    }

    private <C extends Command> String durationLogMessage(C command, Duration commandHandlingDuration) {
        return "command of type " + command.getClass().getName() +
                " dispatched in " + commandHandlingDuration.getNano() + " nanoseconds";
    }
}
