package g55803.samegame.fx.fxview;

import g55803.samegame.model.Facade;
import g55803.samegame.model.Position;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.Objects;

/**
 * Provides a horizontal box with information about the scores and the game.
 *
 * @author Nathan Furnal
 */
public class ScoreBox extends HBox {
    private final Text score;
    private final Text nbRemaining;
    private final Text lastMoveScore;
    private final Facade model;
    private int tempScore;

    /**
     * Creates a view of the current, the last move's score and the number of remaining tiles.
     *
     * @param model the model since calls are required to update the score.
     */
    public ScoreBox(Facade model) {
        this.model = model;
        this.tempScore = model.getScore();
        this.score = new Text("Current score: " + tempScore);
        this.lastMoveScore = new Text("Last move score: " + tempScore);
        this.nbRemaining = new Text("Remaining tiles: " + model.getNRows() * model.getNCols());
        Font retroFont = Font.loadFont(
                Objects.requireNonNull(getClass().getResource("/fonts/retro-font.ttf")).toString(),
                11);
        score.setFont(retroFont);
        lastMoveScore.setFont(retroFont);
        nbRemaining.setFont(retroFont);
        this.getChildren().addAll(score, lastMoveScore, nbRemaining);
        this.setAlignment(Pos.BOTTOM_CENTER);
    }

    /**
     * Refreshes the scores.
     */
    public void refresh() {
        score.setText("Current score: " + model.getScore());
        nbRemaining.setText("Remaining tiles: " + getNbRemainingTiles());
        lastMoveScore.setText("Last move score: " + Math.abs(model.getScore() - tempScore));
        tempScore = model.getScore();
    }

    /**
     * Gets the number of in-game remaining tiles.
     *
     * @return the number of remaining tiles.
     */
    int getNbRemainingTiles() {
        int count = 0;
        for (int i = 0; i < model.getNRows(); i++) {
            for (int j = 0; j < model.getNCols(); j++) {
                if (model.getTile(new Position(i, j)) != null)
                    count++;
            }
        }
        return count;
    }
}
