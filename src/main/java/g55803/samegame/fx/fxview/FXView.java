package g55803.samegame.fx.fxview;

import g55803.samegame.fx.fxcontroller.Controller;
import g55803.samegame.model.Facade;
import g55803.samegame.model.State;
import g55803.samegame.utils.Observer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * Provides the main view and ties all the interaction together for the user.
 *
 * @author Nathan Furnal
 */
public class FXView extends BorderPane implements Observer {
    private final Facade model;
    private final Controller controller;
    private final int WIDTH = 1000;
    private final int HEIGHT = 750;
    private GameLayer gameLayer;
    private ScoreBox scoreBox;
    private StartLayer startLayer;

    /**
     * Creates a view with different layers, each managing a type of interaction (score, user interaction, buttons).
     * This view as well as its components all use a facade to the underlying model which is a Field object.
     *
     * @param controller the controller.
     * @param model      the model.
     */
    public FXView(Controller controller, Facade model) {
        this.controller = controller;
        this.model = model;
        model.subscribe(this);
        this.setBackground(new Background(new BackgroundFill(Color.SILVER, CornerRadii.EMPTY, Insets.EMPTY)));
        this.setBorder(new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderStroke.THICK)));
        setTitle();
        setStartLayer("Welcome to Same Game, make all the same tiles disappear!");
    }

    /**
     * Converts the current view to a JavaFX scene for display.
     *
     * @return a new 750 x 1000 scene.
     */
    public Scene toScene() {
        Scene scene = new Scene(this, WIDTH, HEIGHT);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/main.css")).toString());
        return scene;
    }

    /**
     * Shows the stage passed as parameter, its scene is the view.
     *
     * @param stage a stage to display the view on.
     */
    public void show(Stage stage) {
        stage.setScene(toScene());
        stage.show();
    }

    /**
     * Sets the start layer as half the size of the current layer and sets its interaction with the controller.
     *
     * @param message the message to pass to the layer.
     */
    private void setStartLayer(String message) {
        this.startLayer = new StartLayer(message);
        startLayer.setAlignment(Pos.CENTER);
        this.setCenter(startLayer);
        startLayer.setMaxSize((double) WIDTH / 2, (double) HEIGHT / 2);
        startLayer.getStartButton().setOnMouseClicked(e -> {
            if (startLayer.validate()) {
                controller.start();
            }
        });
    }

    /**
     * Sets the game layer.
     */
    private void setGameLayer() {
        this.gameLayer = new GameLayer(model, controller, WIDTH, HEIGHT);
        gameLayer.setAlignment(Pos.CENTER);
        this.setCenter(gameLayer);
    }

    /**
     * Sets the score box layer.
     */
    private void setScoreBox() {
        this.scoreBox = new ScoreBox(model);
        scoreBox.setPadding(new Insets(25, 0, 25, 0));
        scoreBox.setSpacing(30);
        this.setBottom(scoreBox);
    }

    /**
     * Sets the button layer with all the effects linked to clicking a button.
     */
    private void setButtonBox() {
        ButtonBox buttonBox = new ButtonBox();
        buttonBox.setPadding(new Insets(0, 25, 0, 25));
        buttonBox.setSpacing(40);
        this.setRight(buttonBox);
        buttonBox.getUndoButton().setOnMouseClicked(e -> controller.undo());
        buttonBox.getRedoButton().setOnMouseClicked(e -> controller.redo());
        buttonBox.getExitButton().setOnMouseClicked(e -> controller.exit());
        buttonBox.getResetButton().setOnMouseClicked(e -> {
            if (!startLayer.isVisible()) {
                controller.reset();
            }
        });
        buttonBox.getOpenButton().setOnMouseClicked(e -> {
            Stage other = new Stage();
            GameLayer layer = new GameLayer(model, controller, WIDTH, HEIGHT);
            model.subscribe(layer);
            other.setScene(new Scene(layer));
            other.show();
            other.setOnCloseRequest(ev -> model.unsubscribe(layer));
        });
    }

    /**
     * Starts up all the components and hide the starting layer.
     */
    public void startup() {
        startLayer.setVisible(false);
        setGameLayer();
        setScoreBox();
        setButtonBox();
    }

    /**
     * Refreshes the display.
     */
    public void refresh() {
        this.gameLayer.refresh();
        this.scoreBox.refresh();
    }

    /**
     * Sets the title of the layer.
     */
    private void setTitle() {
        Label title = new Label("Same Game");
        title.setFont(Font.loadFont(Objects.requireNonNull(getClass().getResource("/fonts/retro-font.ttf")).toString(),
                30));
        this.setTop(title);
        BorderPane.setAlignment(title, Pos.TOP_CENTER);
        title.setTextAlignment(TextAlignment.CENTER);
        title.setPadding(new Insets(25, 0, 10, 0));
    }

    /**
     * Resets the game on the UI side. This means removing the existing game layer and calling the controller
     * for a new start.
     */
    public void reset() {
        this.getChildren().remove(gameLayer);
        if (model.getState() == State.WON) {
            setStartLayer("AMAZING, YOU'VE BEATEN THE GAME. Fancy another play?");
        } else if (model.getState() == State.GAME_OVER) {
            setStartLayer("You've been beaten meatbag, more luck next time?");
        } else {
            setStartLayer("Wanna try again?");
        }
    }

    @Override
    public void update() {
        switch (model.getState()) {
            case PLAY, UNDO, REDO -> refresh();
            case WON, GAME_OVER -> {
                refresh();
                controller.reset();
            }
        }
    }

    /**
     * Gets the row text field without exposing the starting layer.
     *
     * @return the value of the row text field.
     */
    public int getRowValue() {
        return startLayer.getRowValue();
    }

    /**
     * Gets the column text field without exposing the starting layer.
     *
     * @return the value of the column text field.
     */
    public int getColValue() {
        return startLayer.getColValue();
    }

    /**
     * Gets the colors choice box without exposing the starting layer.
     *
     * @return the value of the colors choice box.
     */
    public int getColorsValue() {
        return startLayer.getColorsValue();
    }
}
