package g55803.samegame.fx.fxview;

import g55803.samegame.model.Color;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * Provides an interaction layer to start the game and create new games.
 *
 * @author Nathan Furnal
 */
public class StartLayer extends VBox {
    private final Font retroFont = Font.loadFont(
            Objects.requireNonNull(getClass().getResource("/fonts/retro-font.ttf")).toString(),
            11);
    private final String message;
    private TextField rowField;
    private TextField colField;
    private ChoiceBox<Integer> colorChoice;
    private Button startButton;

    /**
     * Sets up the layer with a custom message to the user.
     *
     * @param message the message to display on the layer.
     */
    public StartLayer(String message) {
        this.message = message;
        this.setBackground(new Background(
                new BackgroundFill(javafx.scene.paint.Color.PALEGREEN, CornerRadii.EMPTY, Insets.EMPTY)));
        this.setBorder(new Border(new BorderStroke(javafx.scene.paint.Color.BLACK,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderStroke.MEDIUM)));
        setupTitle();
        setupRowField();
        setupColFiled();
        setupColorsChoice();
        setupButton();
    }

    /**
     * Sets up the title for the layer.
     */
    private void setupTitle() {
        Label title = new Label(message);
        title.setWrapText(true);
        title.setFont(Font.loadFont(
                Objects.requireNonNull(getClass().getResource("/fonts/retro-font.ttf")).toString(),
                13));
        title.setPadding(new Insets(20));
        title.setAlignment(Pos.TOP_CENTER);
        this.getChildren().add(title);
        Region spacer = new Region();
        this.getChildren().add(spacer);
        VBox.setVgrow(spacer, Priority.ALWAYS);
    }

    /**
     * Sets up the row text field to be completed.
     */
    private void setupRowField() {
        HBox box = new HBox();
        Label rowLbl = new Label("Number of rows (2 -> 19): ");
        rowLbl.setFont(retroFont);
        rowLbl.setPadding(new Insets(10));
        this.rowField = new TextField("12");
        rowField.setPrefColumnCount(5);
        box.getChildren().addAll(rowLbl, rowField);
        this.getChildren().add(box);
        box.setAlignment(Pos.CENTER_LEFT);
    }

    /**
     * Sets up the column text field to be completed.
     */
    private void setupColFiled() {
        HBox box = new HBox();
        Label colLbl = new Label("Number of columns (2 -> 19): ");
        colLbl.setFont(retroFont);
        colLbl.setPadding(new Insets(10));
        this.colField = new TextField("16");
        colField.setPrefColumnCount(5);
        box.getChildren().addAll(colLbl, colField);
        this.getChildren().add(box);
        box.setAlignment(Pos.CENTER_LEFT);
    }

    /**
     * Sets up the color choices.
     */
    private void setupColorsChoice() {
        HBox box = new HBox();
        Label colorLbl = new Label("Number of colors: ");
        colorLbl.setFont(retroFont);
        colorLbl.setPadding(new Insets(10));
        this.colorChoice = new ChoiceBox<>();
        List<Integer> choices = IntStream.rangeClosed(2, Color.values().length).boxed().toList(); // limit to existing choices
        colorChoice.getItems().addAll(choices);
        colorChoice.setValue(3);
        box.getChildren().addAll(colorLbl, colorChoice);
        this.getChildren().add(box);
        box.setAlignment(Pos.CENTER_LEFT);
    }

    /**
     * Sets up the starting button.
     */
    private void setupButton() {
        this.startButton = new Button("Let's go!");
        startButton.setFont(Font.loadFont(
                Objects.requireNonNull(getClass().getResource("/fonts/retro-font.ttf")).toString(),
                13));
        startButton.setBackground(new Background(
                new BackgroundFill(javafx.scene.paint.Color.PEACHPUFF, CornerRadii.EMPTY, Insets.EMPTY)));
        startButton.setBorder(new Border(new BorderStroke(javafx.scene.paint.Color.BLACK,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderStroke.THIN)));
        Region spacer = new Region();
        this.getChildren().add(spacer);
        VBox.setVgrow(spacer, Priority.ALWAYS);
        this.getChildren().add(startButton);
        startButton.setAlignment(Pos.CENTER);
        Region spacer2 = new Region();
        this.getChildren().add(spacer2);
        VBox.setVgrow(spacer2, Priority.ALWAYS);
        startButton.setOnMouseEntered(e -> startButton.setTextFill(javafx.scene.paint.Color.BROWN));
        startButton.setOnMouseExited(e -> startButton.setTextFill(javafx.scene.paint.Color.BLACK));
    }

    /**
     * Gets the starting button.
     *
     * @return the starting button.
     */
    public Button getStartButton() {
        return startButton;
    }

    /**
     * Validates the input of an input field, with custom limits. It has the side effect
     * of making the input field blink read to inform the user the input is incorrect.
     *
     * @param input the text input.
     * @return true if the input is valid and false otherwise.
     */
    private boolean isValidInput(TextField input) {
        if (input.getText() != null && input.getText().length() != 0) {
            String inp = input.getText().trim();
            try {
                int res = Integer.parseInt(inp);
                if (res < 2 || res > 19) { // Values required in the model
                    throw new IllegalArgumentException("Input not in bounds."); // Sneaky
                } else {
                    return true;
                }
            } catch (IllegalArgumentException e) {
                Border before = input.getBorder();
                Border bad = new Border(new BorderStroke(javafx.scene.paint.Color.RED,
                        BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderStroke.MEDIUM));
                Timeline flash = new Timeline(
                        new KeyFrame(Duration.seconds(0), event -> input.setBorder(bad)),
                        new KeyFrame(Duration.seconds(1.), event -> input.setBorder(before))
                );
                flash.play();
                return false;
            }
        }
        return false;
    }

    /**
     * Valides the row and column fields.
     *
     * @return True if both are valid inputs and false otherwise.
     */
    boolean validate() {
        // Get both functions to trigger when content is not validated
        boolean check = isValidInput(rowField);
        return isValidInput(colField) && check;
    }

    /**
     * Gets the row text field value.
     *
     * @return the row text field value.
     */
    int getRowValue() {
        return Integer.parseInt(rowField.getText().trim());
    }

    /**
     * Gets the column text field value.
     *
     * @return the column text field value.
     */
    int getColValue() {
        return Integer.parseInt(colField.getText().trim());
    }

    /**
     * Gets the number of colors from the choice box.
     *
     * @return the number of colors.
     */
    int getColorsValue() {
        return colorChoice.getValue();
    }
}
