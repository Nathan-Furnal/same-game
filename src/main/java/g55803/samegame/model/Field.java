package g55803.samegame.model;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Provides a field to play the same game on. This is where all the game logic lies and where possibles moves are defined.
 * This class is called "field" but it can also be considered a game board. Both words are used interchangeably in the docs.
 *
 * @author Nathan Furnal
 */
public class Field {
    private final int nRows;
    private final int nCols;
    private final Tile[][] field;
    private int score;

    /**
     * Creates the game field based on a number of rows, columns and a number of possible colors.
     *
     * @param nRows   the number of rows.
     * @param nCols   the number of columns.
     * @param nColors the number of colors.
     */
    public Field(int nRows, int nCols, int nColors) {
        if (nRows < 2 || nCols < 2) {
            throw new IllegalArgumentException("There should a positive number of rows and columns larger than 2, received : " +
                    "nRows : " + nRows + ", nCols : " + nCols);
        }
        if (nRows > 19 || nCols > 19) {
            throw new IllegalArgumentException("THere should fewer than 20 rows or columns, received : " +
                    "nRows : " + nRows + ", nCols : " + nCols);
        }
        if (nColors < 2 || nColors > Color.values().length) {
            throw new IllegalArgumentException("Please define between 3 and 5 colors, received : " + nColors);
        }
        this.nRows = nRows;
        this.nCols = nCols;
        this.score = 0;
        this.field = new Tile[nRows][nCols];
        for (int i = 0; i < nRows; i++) {
            for (int j = 0; j < nCols; j++) {
                int rnd = new Random().nextInt(nColors);
                Color[] colors = Color.values();
                field[i][j] = new Tile(colors[rnd]);
            }
        }
    }

    /**
     * Copy constructor to copy a field from another one. This completely re-creates the field as well as the tiles
     * on it, which allows perfectly separate copies.
     *
     * @param other the field to copy.
     */
    public Field(Field other) {
        if (other == null) {
            throw new IllegalArgumentException("The field passed as argument can't be null.");
        }
        this.score = other.getScore();
        this.nRows = other.getNRows();
        this.nCols = other.getNCols();
        this.field = new Tile[nRows][nCols];
        for (int i = 0; i < nRows; i++) {
            for (int j = 0; j < nCols; j++) {
                Position p = new Position(i, j);
                if (other.getTile(p) != null) {
                    this.setTile(new Position(i, j), new Tile(other.getTile(p).getColor()));
                } else {
                    this.setTile(new Position(i, j), null);
                }
            }
        }
    }

    /**
     * Gets the number of rows of the field.
     *
     * @return the number of rows.
     */
    public int getNRows() {
        return nRows;
    }

    /**
     * Gets the number of columns of the field.
     *
     * @return the number of columns.
     */
    public int getNCols() {
        return nCols;
    }

    /**
     * Gets a tile at a given position.
     *
     * @param p the position to get the tile at.
     * @return the tile at the given position.
     */
    public Tile getTile(Position p) {
        checkPosition(p);
        return field[p.getRow()][p.getCol()];
    }

    /**
     * Sets a tile at a given position on the board.
     *
     * @param p    the position on the board.
     * @param tile the new tile to put at the given position.
     */
    public void setTile(Position p, Tile tile) {
        checkPosition(p);
        field[p.getRow()][p.getCol()] = tile;
    }

    /**
     * Gets the current score.
     *
     * @return the current score.
     */
    public int getScore() {
        return score;
    }

    /**
     * Sets the score.
     *
     * @param newScore the new value of the score.
     */
    public void setScore(int newScore) {
        if (newScore < 0) {
            throw new IllegalArgumentException("The score should be a positive integer, received : " + newScore);
        }
        this.score = newScore;
    }

    /**
     * Any time a move is made, this method is called. It checks if there are any empty columns and then left aligns
     * them until no column is left with empty space on its left.
     */
    private void fillFreeColumns() {
        for (int j = 0; j < nCols; j++) { // for every column
            if (field[nRows - 1][j] == null) { // if the bottom tile is empty (depends on tiles falling properly)
                int steps = 0; // start counting
                while (j + steps < nCols) { // while in the board
                    if (field[nRows - 1][j + steps] != null) { // at the first non-empty bottom tile
                        for (int i = 0; i < nRows; i++) { // bring all the tiles over from the non-empty column
                            field[i][j] = field[i][j + steps];
                            field[i][j + steps] = null; // cleanup after displacement
                        }
                        break; // break since the column has been moved
                    }
                    steps++; // if not, increase by one step on the right
                }
            }
        }
    }

    /**
     * Makes tiles fall at a given column. Any tile with empty space below it goes down until it meets the end of
     * the board or another tile.
     *
     * @param col the column where to make the tiles fall.
     */
    private void fall(int col) {
        colCheck(col);
        for (int i = nRows - 1; i >= 0; i--) {
            int steps = 0;
            while (i - steps >= 0) {
                if (field[i][col] == null && field[i - steps][col] != null) {
                    Tile temp = field[i - steps][col];
                    field[i - steps][col] = field[i][col];
                    field[i][col] = temp;
                }
                steps++;
            }
        }
    }

    /**
     * Checks that a given column is inside the board. Throws an exception otherwise.
     *
     * @param col the column to check.
     */
    private void colCheck(int col) {
        if (col < 0 || col >= nCols) {
            throw new IllegalArgumentException(
                    String.format("Invalid position, col is expected to be between 0 and %d but received %d", nCols, col));
        }
    }

    /**
     * Checks that a position is valid on the board. Which means the position is not null and that it lies within
     * the board. Throws an exception otherwise.
     *
     * @param p the position to check.
     */
    private void checkPosition(Position p) {
        if (p == null)
            throw new IllegalArgumentException("Can't get a null position.");
        if (p.getRow() < 0 || p.getRow() >= nRows || p.getCol() < 0 || p.getCol() >= nCols) {
            throw new IllegalArgumentException("This position is out of the board.");
        }
    }

    /**
     * Groups similarly colored tiles together when a tile is selected. Selects the tile at the given position and
     * then explores its surrounding in the four possible directions until it meets tiles of different colors. A history
     * of all valid positions (tiles of the same color) is kept and returned.
     *
     * @param p the position to check the tile at.
     * @return a set of all positions where tiles have the same color as the tile at the given position.
     */
    HashSet<Position> groupColor(Position p) {
        checkPosition(p);
        HashSet<Position> positions = new HashSet<>(); // Empty set holding all the positions after the recursion is done.
        return groupColor(p, positions);
    }

    /**
     * Underlying algorithm to group the colors, it calls itself until no valid surrounding color is left.
     *
     * @param p         the position to check the tile at.
     * @param positions the current history of valid already visited positions.
     * @return a set of valid positions.
     */
    private HashSet<Position> groupColor(Position p, HashSet<Position> positions) {
        checkPosition(p);
        int x = p.getRow();
        int y = p.getCol();
        Tile tile = field[x][y];
        for (Direction d : Direction.values()) {
            int xdx = x + d.deltaRow;
            int ydy = y + d.deltaCol;
            if (xdx >= 0 && xdx < nRows && ydy >= 0 && ydy < nCols) {
                Position newPos = p.move(d);
                if (tile != null && field[xdx][ydy] != null
                        && field[xdx][ydy].getColor().equals(tile.getColor()) && !positions.contains(newPos)) {
                    positions.add(newPos);
                    groupColor(newPos, positions);
                }
            }
        }
        return positions;
    }

    /**
     * Plays a move at a given position. First the position is checked for validity. Then, the group of valid color
     * is selected as well as the columns where falling will be necessary. Then, each tile in those positions is removed,
     * the tiles above the fall and the score is updated.
     *
     * @param p the position where to play the move at.
     */
    private void playMove(Position p) {
        checkPosition(p);
        if (getTile(p) == null) {
            throw new IllegalArgumentException("Can't play a null tile!");
        }
        HashSet<Position> positions = groupColor(p);
        Set<Integer> uniqueCols = positions.stream().map(Position::getCol).collect(Collectors.toSet());
        for (Position pos : positions) {
            field[pos.getRow()][pos.getCol()] = null;
        }
        for (int col : uniqueCols) {
            fall(col);
        }
        score += positions.size() * (positions.size() - 1);
    }

    /**
     * Plays a move using the playing algorithm then left aligns the columns if necessary.
     *
     * @param p the position to play the move at and to pass to the play algorithm.
     */
    public void play(Position p) {
        playMove(p);
        fillFreeColumns();
    }

    /**
     * Checks if the game is over, the game is over if there are no moves left to play. Meaning there are no groups
     * of at least 2 tiles left. A game is also over when the field is empty. This means that a game can be over
     * and won or over and lost.
     *
     * @return true if the game is over and false otherwise.
     */
    boolean isGameOver() {
        for (int i = 0; i < nRows; i++) {
            for (int j = 0; j < nCols; j++) {
                Tile curr = field[i][j];
                for (Direction d : Direction.values()) {
                    int idx = i + d.deltaRow;
                    int jdy = j + d.deltaCol;
                    if (curr != null && idx >= 0 && idx < nRows && jdy >= 0 && jdy < nCols && field[idx][jdy] != null) {
                        if (curr.getColor().equals(field[idx][jdy].getColor()))
                            return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Checks if the game is won. A game is won if there are no tiles left on the board. Since tiles fall and are left
     * aligned, it is sufficient to check if the bottom-most left tile is still there or not.
     *
     * @return true if the game is won and false otherwise.
     */
    boolean isGameWon() {
        return field[nRows - 1][0] == null;
    }

    /**
     * Utility method used for tests.
     *
     * @param c Color to set.
     */
    void setAllColors(Color c) {
        for (int i = 0; i < nRows; i++) {
            for (int j = 0; j < nCols; j++) {
                field[i][j] = new Tile(c);
            }
        }
    }
}
