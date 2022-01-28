package g55803.samegame.fx;

import g55803.samegame.fx.fxcontroller.Controller;
import g55803.samegame.model.Facade;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main application loop for the game.
 *
 * @author Nathan Furnal
 */
public class Main extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Same Game");
        Facade model = new Facade();
        Controller ctrl = new Controller(model, primaryStage);
    }
}
