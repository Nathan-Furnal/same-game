package g55803.samegame.fx.fxcontroller;

import g55803.samegame.fx.fxview.FXView;
import g55803.samegame.model.Facade;
import g55803.samegame.model.Position;
import javafx.stage.Stage;

/**
 * Provides a controller to manage the interaction between model and view.
 *
 * @author Nathan Furnal
 */
public class Controller {
    private final Facade model;
    private final FXView view;
    private final Stage stage;

    /**
     * Creates the controller with a model and a view to implement an MVC.
     *
     * @param model the model.
     * @param stage the JavaFX stage.
     */
    public Controller(Facade model, Stage stage) {
        this.model = model;
        this.stage = stage;
        this.view = new FXView(this, model);
        view.show(stage);
    }

    /**
     * Plays a move on the field, through the model.
     *
     * @param p the position to play the move at.
     */
    public void play(Position p) {
        model.play(p);
    }

    /**
     * Undoes an action through the model.
     */
    public void undo() {
        model.undo();
    }

    /**
     * Redoes an action through the model.
     */
    public void redo() {
        model.redo();
    }

    /**
     * Manages the beginning of the game.
     */
    public void start() {
        int row = view.getRowValue();
        int col = view.getColValue();
        int nColors = view.getColorsValue();
        model.newField(row, col, nColors);
        view.startup();
    }

    /**
     * Resets the view and updates the model parameters to create a new game.
     */
    public void reset() {
        view.reset();
        int row = view.getRowValue();
        int col = view.getColValue();
        int ncColors = view.getColorsValue();
        model.newField(row, col, ncColors);
    }

    /**
     * Closes the current window.
     */
    public void exit() {
        stage.close();
    }
}
