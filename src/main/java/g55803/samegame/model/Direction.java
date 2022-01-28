package g55803.samegame.model;

/**
 * Provides an enumeration of possible direction on a 2D grid. Those directions include left, right, up and down.
 * Each direction can be added to a position object to move it.
 *
 * @author Nathan Furnal
 */
enum Direction {
    UP(-1, 0),
    DOWN(1, 0),
    LEFT(0, -1),
    RIGHT(0, 1);

    public final int deltaRow;
    public final int deltaCol;

    /**
     * Constructs a direction based on deltas.
     *
     * @param deltaRow the delta on the x-axis.
     * @param deltaCol the delta on the y-axis.
     */
    Direction(int deltaRow, int deltaCol) {
        this.deltaRow = deltaRow;
        this.deltaCol = deltaCol;
    }
}
