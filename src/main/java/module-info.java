/**
 * @author Nathan Furnal
 */

module SameGame {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens g55803.samegame.model;
    opens g55803.samegame.fx to javafx.fxml;
    exports g55803.samegame.fx;
}