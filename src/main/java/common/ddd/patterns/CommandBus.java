package common.ddd.patterns;

public interface CommandBus {
    <R extends CommandResponse, C extends Command> R dispatch(C command);
}
