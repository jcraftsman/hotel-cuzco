package common.ddd.patterns;

public interface CommandHandler<R extends CommandResponse, C extends Command> {

    R handle(C command);

    Class listenTo();
}
