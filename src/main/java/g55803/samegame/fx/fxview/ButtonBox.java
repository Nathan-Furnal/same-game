package g55803.samegame.fx.fxview;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Provides a vertical box with the buttons.
 *
 * @author Nathan Furnal
 */
public class ButtonBox extends VBox {
    private final Button undoButton;
    private final Button redoButton;
    private final Button resetButton;
    private final Button exitButton;
    private final Button openButton;

    /**
     * Generates a vertical box with utility buttons.
     */
    public ButtonBox() {
        this.setAlignment(Pos.CENTER_RIGHT);
        this.undoButton = new Button("Undo");
        this.redoButton = new Button("Redo");
        this.resetButton = new Button("Reset");
        this.exitButton = new Button("Exit");
        this.openButton = new Button("Open");
        List<Button> buttons = new ArrayList<>(List.of(undoButton, redoButton, resetButton, exitButton, openButton));
        this.getChildren().addAll(undoButton, redoButton, resetButton, exitButton, openButton);
        for (Button b : buttons) {
            b.setFont(Font.loadFont(Objects.requireNonNull(getClass().getResource("/fonts/retro-font.ttf")).toString(),
                    14));
            b.setTextFill(Color.WHITESMOKE);
            b.setBackground(new Background(new BackgroundFill(Color.ROYALBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
            b.setOnMouseEntered(e -> b.setTextFill(Color.PALEVIOLETRED));
            b.setOnMouseExited(e -> b.setTextFill(Color.WHITESMOKE));
        }
    }

    /**
     * Gets the undo button.
     *
     * @return the undo button.
     */
    public Button getUndoButton() {
        return undoButton;
    }

    /**
     * Gets the redo button.
     *
     * @return the redo button.
     */
    public Button getRedoButton() {
        return redoButton;
    }

    /**
     * Gets the reset button.
     *
     * @return the reset button.
     */
    public Button getResetButton() {
        return resetButton;
    }

    /**
     * Gets the exit button.
     *
     * @return the exit button.
     */
    public Button getExitButton() {
        return exitButton;
    }

    public Button getOpenButton() {
        return openButton;
    }
}
