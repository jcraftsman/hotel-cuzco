package hotel.cuzco.middleware.commands;

import hotel.cuzco.middleware.events.Event;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.util.List;

@Data
@Builder
public class CommandResponse<T> {
    private T value;
    @Singular
    private List<Event> events;
}
