package hotel.cuzco.middleware.commands;

import hotel.cuzco.middleware.commands.fixtures.OrderFlowersDeliveryCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

class LoggerMiddlewareTest {

    private CommandBusMiddleware nextCommandBus;
    private Logger logger;

    @BeforeEach
    void setUp() {
        nextCommandBus = mock(CommandBusMiddleware.class);
        logger = mock(Logger.class);
    }

    @Test
    void it_should_return_the_next_commandBus_response_and_log() {
        // Given
        var command = new OrderFlowersDeliveryCommand(4, "Orchidea");
        var loggerMiddleware = new LoggerMiddleware(nextCommandBus, logger);
        var nextCommandBusCommandResponse = new CommandResponse("NEXT RESPONSE", emptyList());
        given(nextCommandBus.dispatch(command)).willReturn(nextCommandBusCommandResponse);

        // When
        var commandResponse = loggerMiddleware.dispatch(command);

        // Then
        assertThat(commandResponse).isEqualTo(nextCommandBusCommandResponse);
        then(logger).should().info(anyString());
    }
}