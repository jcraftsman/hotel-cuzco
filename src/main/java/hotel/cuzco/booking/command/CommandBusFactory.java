package hotel.cuzco.booking.command;

import hotel.cuzco.booking.domain.MailSender;
import hotel.cuzco.booking.domain.ReservationMadeNotifier;
import hotel.cuzco.booking.domain.RoomRepository;
import hotel.cuzco.middleware.commands.CommandDispatcher;
import hotel.cuzco.middleware.commands.EventDispatcherBusMiddleware;
import hotel.cuzco.middleware.events.EventBus;

import java.util.List;

import static java.util.Arrays.asList;

public class CommandBusFactory {
    public static EventDispatcherBusMiddleware build(RoomRepository roomRepository,
                                                     MailSender mailSender) {
        var commandHandlers = asList(
                new MakeReservationCommandHandler(roomRepository),
                new CancelReservationCommandHandler(roomRepository));
        var commandDispatcher = new CommandDispatcher(commandHandlers);
        var eventBus = new EventBus(List.of(new ReservationMadeNotifier(mailSender)));
        return new EventDispatcherBusMiddleware(commandDispatcher, eventBus);
    }
}
