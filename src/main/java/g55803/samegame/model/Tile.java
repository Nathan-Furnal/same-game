package g55803.samegame.model;

/**
 * Provides a tile to be placed on the board. A tile is defined by its color.
 *
 * @author Nathan Furnal
 */
public class Tile {
    private final Color color;

    /**
     * Creates a tile with a given color.
     *
     * @param color the color of the tile.
     */
    public Tile(Color color) {
        this.color = color;
    }

    /**
     * Gets the color of the tile.
     *
     * @return the color of the tile.
     */
    public Color getColor() {
        return color;
    }

    @Override
    public String toString() {
        return color.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tile tile = (Tile) o;

        return color.equals(tile.getColor());
    }

    @Override
    public int hashCode() {
        return color != null ? color.hashCode() : 0;
    }
}
