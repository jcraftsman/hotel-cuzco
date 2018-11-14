package hotel.cuzco.booking.usecase.command;

import common.ddd.patterns.CommandBus;
import hotel.cuzco.booking.domain.notification.MailSender;
import hotel.cuzco.booking.domain.notification.ReservationMadeNotifier;
import hotel.cuzco.booking.domain.reservation.RoomRepository;
import hotel.cuzco.middleware.commands.CommandDispatcher;
import hotel.cuzco.middleware.commands.EventDispatcherBusMiddleware;
import hotel.cuzco.middleware.commands.LoggerMiddleware;
import hotel.cuzco.middleware.events.EventBus;

import java.util.List;

import static java.util.Arrays.asList;

public class CommandBusFactory {
    public static CommandBus build(RoomRepository roomRepository,
                                   MailSender mailSender) {
        var commandHandlers = asList(
                new MakeReservationCommandHandler(roomRepository),
                new CancelReservationCommandHandler(roomRepository));
        var commandDispatcher = new CommandDispatcher(commandHandlers);
        var loggerCommandBusMiddleware = LoggerMiddleware.create(commandDispatcher);
        var eventBus = new EventBus(List.of(new ReservationMadeNotifier(mailSender)));
        return new EventDispatcherBusMiddleware(loggerCommandBusMiddleware, eventBus);
    }
}
