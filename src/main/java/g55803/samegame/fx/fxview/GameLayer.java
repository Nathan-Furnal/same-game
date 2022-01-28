package g55803.samegame.fx.fxview;

import g55803.samegame.fx.fxcontroller.Controller;
import g55803.samegame.model.Facade;
import g55803.samegame.model.Position;
import g55803.samegame.utils.Observer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Provides a layer to play the game proper. Most of the game interaction happens here.
 *
 * @author Nathan Furnal
 */
public class GameLayer extends GridPane implements Observer {
    private final Facade model;
    private final Controller controller;
    private final Rectangle[][] recArray;
    private final Color backColor = Color.DARKSLATEGRAY;
    private final HashMap<String, Color> colorMap = new HashMap<>();

    /**
     * Creates the board area with a color map to use JavaFX colors. It also keeps tracks of the rectangles
     * in a 2D array to help mirror the underlying model when updating the UI.
     *
     * @param model  the model.
     * @param c      the controller.
     * @param width  the width of the window.
     * @param height the height of the window.
     */
    public GameLayer(Facade model, Controller c, int width, int height) {
        this.model = model;
        this.controller = c;
        this.setAlignment(Pos.CENTER);
        this.setBackground(new Background(new BackgroundFill(backColor, CornerRadii.EMPTY, Insets.EMPTY)));
        colorMap.put("r", Color.CORAL);
        colorMap.put("g", Color.LIGHTGREEN);
        colorMap.put("b", Color.DARKTURQUOISE);
        colorMap.put("y", Color.LIGHTGOLDENRODYELLOW);
        colorMap.put("p", Color.VIOLET);
        this.recArray = new Rectangle[model.getNRows()][model.getNCols()];
        float numSize = Math.min(width, height) - 100;
        float denomSize = Math.max(model.getNCols(), model.getNRows());
        float side = Math.min(numSize / denomSize - 2, 30); // trying to find a proper ratio
        for (int i = 0; i < model.getNRows(); i++) {
            for (int j = 0; j < model.getNCols(); j++) {
                Rectangle r = new Rectangle();
                r.setWidth(side);
                r.setHeight(side);
                r.setArcHeight(10);
                r.setArcWidth(10);
                recArray[i][j] = r;
                if (model.getTile(new Position(i, j)) != null) {
                    r.setFill(colorMap.get(model.getTile(new Position(i, j)).getColor().code));
                } else {
                    r.setFill(backColor);
                }
                r.addEventHandler(MouseEvent.MOUSE_ENTERED, makeMouseEvent(r, .6));
                r.addEventHandler(MouseEvent.MOUSE_EXITED, makeMouseEvent(r, 1));
                r.addEventHandler(MouseEvent.MOUSE_CLICKED, makeClickEvent(r));
                this.add(r, j, i); // GridPane adds the column before the row
            }
        }
        this.setMaxSize((side - 2) * model.getNCols(),
                (side - 2) - model.getNRows());
        this.setBorder(new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderStroke.MEDIUM)));
    }

    /**
     * Refreshes the board area.
     */
    public void refresh() {
        for (int i = 0; i < model.getNRows(); i++) {
            for (int j = 0; j < model.getNCols(); j++) {
                if (model.getTile(new Position(i, j)) != null) {
                    recArray[i][j].setFill(colorMap.get(model.getTile(new Position(i, j)).getColor().code));
                    recArray[i][j].setOpacity(1.);
                    recArray[i][j].setDisable(false);
                } else {
                    recArray[i][j].setFill(backColor);
                    recArray[i][j].setDisable(true);
                }
            }
        }
    }

    /**
     * Utility handler to change the opacity of a visited rectangle on mouse event.
     *
     * @param rec     the rectangle.
     * @param opacity the new opacity to set.
     * @return the mouse event handler.
     */
    private EventHandler<MouseEvent> makeMouseEvent(Rectangle rec, double opacity) {
        return event -> {
            rec.setOpacity(opacity);
            int row = GridPane.getRowIndex(rec);
            int col = GridPane.getColumnIndex(rec);
            HashSet<Position> positions = model.groupColor(new Position(row, col));
            for (Position p : positions) {
                recArray[p.getRow()][p.getCol()].setOpacity(opacity);
            }
        };
    }

    /**
     * Utility handler to color a rectangle when it's clicked. Used when a move can't be played
     * and inform the user.
     *
     * @param rec the rectangle.
     * @return the mouse event handler.
     */
    private EventHandler<MouseEvent> makeClickEvent(Rectangle rec) {
        return event -> {
            int row = GridPane.getRowIndex(rec);
            int col = GridPane.getColumnIndex(rec);
            Position p = new Position(row, col);
            HashSet<Position> positions = model.groupColor(p);
            if (positions.size() == 0) {
                Paint c = rec.getFill();
                Timeline flash = new Timeline(
                        new KeyFrame(Duration.seconds(0), e -> rec.setFill(Color.RED)),
                        new KeyFrame(Duration.seconds(.5), e -> rec.setFill(c))
                );
                flash.play();
            } else {
                controller.play(p);
            }
        };
    }

    @Override
    public void update() {
        refresh();
    }
}
