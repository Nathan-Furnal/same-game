package g55803.samegame.model.commands;

import g55803.samegame.model.Field;
import g55803.samegame.model.Position;

/**
 * Provides a command for when a move is played at a given position. Implements the command design pattern.
 *
 * @author Nathan Furnal
 */
public class PlayCommand implements Command {
    private final Field receiver;
    private final Position arg;
    private Field inMemoryField;

    /**
     * Creates a command with a given model and the necessary arguments for the command to be used.
     *
     * @param field the receiver which will execute the command.
     * @param arg   the argument of the command.
     */
    public PlayCommand(Field field, Position arg) {
        if (field == null) {
            throw new IllegalArgumentException("The field object in the play command can't be null.");
        }
        if (arg == null) {
            throw new IllegalArgumentException("The argument of the command should not be null");
        }
        this.receiver = field;
        this.arg = arg;
    }

    @Override
    public void execute() {
        // Constructor copy and then storing it in memory
        inMemoryField = new Field(receiver);
        receiver.play(arg);
    }

    @Override
    public void cancel() {
        if (inMemoryField == null) {
            throw new IllegalArgumentException("The execute command should be used before the cancel command.");
        }
        // Going back to the stored copy and extract its attributes into the receiver
        receiver.setScore(inMemoryField.getScore());
        for (int i = 0; i < receiver.getNRows(); i++) {
            for (int j = 0; j < receiver.getNCols(); j++) {
                Position p = new Position(i, j);
                receiver.setTile(p, inMemoryField.getTile(p));
            }
        }
    }
}
