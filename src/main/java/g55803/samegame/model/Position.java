package g55803.samegame.model;

/**
 * Provides a position class to be used on the game's field.
 *
 * @author Nathan Furnal
 */
public class Position {
    private final int row;
    private final int col;

    /**
     * Creates a position from a row and a column.
     *
     * @param row the row.
     * @param col the column.
     */
    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * Gets the row.
     *
     * @return the row.
     */
    public int getRow() {
        return row;
    }

    /**
     * Gets the column.
     *
     * @return the column.
     */
    public int getCol() {
        return col;
    }

    /**
     * Moves a position but returns a new position, to leave the original position untouched.
     *
     * @param d the direction in which to move.
     * @return a new position which has moved in the given direction.
     */
    Position move(Direction d) {
        return new Position(row + d.deltaRow, col + d.deltaCol);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Position position = (Position) o;

        if (row != position.getRow()) return false;
        return col == position.getCol();
    }

    @Override
    public int hashCode() {
        int result = row;
        result = 31 * result + col;
        return result;
    }

    @Override
    public String toString() {
        return "(" + row + ", " + col + ")";
    }
}
