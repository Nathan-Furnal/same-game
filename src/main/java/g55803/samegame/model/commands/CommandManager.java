package g55803.samegame.model.commands;

import java.util.Deque;
import java.util.LinkedList;

/**
 * @author Nathan Furnal
 */
public class CommandManager {
    private final Deque<Command> undoHistory;
    private final Deque<Command> redoHistory;

    public CommandManager() {
        this.undoHistory = new LinkedList<>();
        this.redoHistory = new LinkedList<>();
    }

    public void execute(Command action) {
        undoHistory.addFirst(action);
        redoHistory.clear();
        action.execute();
    }

    public void undo() {
        if (!undoHistory.isEmpty()) {
            Command action = undoHistory.pop();
            action.cancel();
            redoHistory.addFirst(action);
        }
    }

    public void redo() {
        if (!redoHistory.isEmpty()) {
            Command action = redoHistory.pop();
            action.execute();
            undoHistory.addFirst(action);
        }
    }

    public boolean isEmptyUndo(){
        return undoHistory.isEmpty();
    }

    public boolean isEmptyRedo(){
        return redoHistory.isEmpty();
    }
}
