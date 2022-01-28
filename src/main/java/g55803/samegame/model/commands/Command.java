package g55803.samegame.model.commands;

/**
 * Provides a short interface for in-game commands. Only reversible commands are supposed to implement this interface.
 *
 * @author Nathan Furnal
 */
public interface Command {
    /**
     * Executes a command through a receiver that provides action for the command.
     */
    void execute();

    /**
     * Cancels a command and reverts the state to a similar state, equivalent to the before execution state.
     */
    void cancel();
}
