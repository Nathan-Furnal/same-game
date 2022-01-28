package g55803.samegame.model;

import g55803.samegame.model.commands.Command;
import g55803.samegame.model.commands.CommandManager;
import g55803.samegame.model.commands.PlayCommand;
import g55803.samegame.utils.Observable;
import g55803.samegame.utils.Observer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Provides a facade for the Field object which is the underlying model of the game. The facade exposes public methods
 * to be used in other packages such as views and controllers while staying agnostic on how they are used.
 *
 * @author Nathan Furnal
 */
public class Facade implements Observable {
    private final List<Observer> observers;
    private final CommandManager commandManager;
    private Field model;
    private State state;

    /**
     * Constructs a facade based on the same arguments as the underlying model: number of rows, columns and colors.
     *
     * @param nRows   the number of rows of the grid.
     * @param nCols   the number of columns of the grid.
     * @param nColors the number of colors to play with.
     */
    public Facade(int nRows, int nCols, int nColors) {
        this.model = new Field(nRows, nCols, nColors);
        this.observers = new ArrayList<>();
        this.commandManager = new CommandManager();
    }

    /**
     * Default constructor with sane defaults.
     */
    public Facade() {
        // Sane defaults
        this(12, 16, 3);
    }

    /**
     * Checks if the game is over. A game is over if there are tiles left on the board but no move is possible. In the
     * case there are no tiles left, the game is won.
     *
     * @return true if the game is over and false otherwise.
     */
    public boolean isGameOver() {
        return model.isGameOver();
    }

    /**
     * Checks if the game is won. A game is won if there are no tiles left on the board. Since tiles fall and are left
     * aligned, it is sufficient to check if the bottom-most left tile is still there or not.
     *
     * @return true if the game is won and false otherwise.
     */
    public boolean isGameWon() {
        return model.isGameWon();
    }

    /**
     * Plays a move using the command pattern. As such, a play command is created with the relevant position to play
     * and the redo/undo stacks are updated accordingly.
     *
     * @param p the position to play the move at.
     */
    public void play(Position p) {
        Command pl = new PlayCommand(model, p);
        commandManager.execute(pl);
        if (isGameWon()) {
            state = State.WON;
            notifyObservers();
            state = State.STARTED;
        } else if (isGameOver()) {
            state = State.GAME_OVER;
            notifyObservers();
            state = State.STARTED;
        } else {
            state = State.PLAY;
            notifyObservers();
        }
    }

    /**
     * Gets the number of rows on the board.
     *
     * @return the number of rows.
     */
    public int getNRows() {
        return model.getNRows();
    }

    /**
     * Gets the number of columns on the board.
     *
     * @return the number of columns.
     */
    public int getNCols() {
        return model.getNCols();
    }

    /**
     * Gets a tile at a given position.
     *
     * @param p the position to get the tile at.
     * @return the tile at position <code>p</code>.
     */
    public Tile getTile(Position p) {
        return model.getTile(p);
    }

    /**
     * Gets the current score for the game.
     *
     * @return the current score.
     */
    public int getScore() {
        return model.getScore();
    }

    @Override
    public void subscribe(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void unsubscribe(Observer observer) {
        observers.remove(observer);
    }

    private void notifyObservers() {
        for (Observer obs : observers) {
            obs.update();
        }
    }

    /**
     * Undoes an action by popping the undo-stack. Since any player's action is tracked in the stack and as long
     * as the stack is not empty, it's possible to undo actions going from the most recent, to the most distant in time.
     */
    public void undo() {
        if(!commandManager.isEmptyUndo()) {
            commandManager.undo();
            state = State.UNDO;
            notifyObservers();
        }
    }

    /**
     * Redoes an action by popping the redo-stack. Since any player's undo is tracked in the stack and as long as
     * the stack is not cleared by a player action, it's possible to redo past undoes, from the most recent to the oldest.
     */
    public void redo() {
        if(!commandManager.isEmptyRedo()) {
            commandManager.redo();
            state = State.REDO;
            notifyObservers();
        }
    }

    /**
     * Creates a new field, this is basically a setter for the underlying model of the facade.
     *
     * @param nRows   the number of rows of the grid.
     * @param nCols   the number of columns of the grid.
     * @param nColors the number of colors to play with.
     */
    public void newField(int nRows, int nCols, int nColors) {
        model = new Field(nRows, nCols, nColors);
    }

    /**
     * Checks if the undo-history is empty.
     *
     * @return true if the undo-stack is empty and false otherwise.
     */
    public boolean isEmptyUndo() {
        return commandManager.isEmptyUndo();
    }

    /**
     * Checks if the redo-history is empty.
     *
     * @return true if the redo-stack is empty and false otherwise.
     */
    public boolean isEmptyRedo() {
        return commandManager.isEmptyRedo();
    }

    public HashSet<Position> groupColor(Position p) {
        return model.groupColor(p);
    }

    /**
     * Gets the current state of the field.
     *
     * @return the state of the field.
     */
    public State getState() {
        return state;
    }
}
