package g55803.samegame.model;

/**
 * Provides colors with the corresponding emoji code to display colored squares.
 *
 * @author Nathan Furnal
 */
public enum Color {
    RED("r"),
    GREEN("g"),
    BLUE("b"),
    YELLOW("y"),
    PURPLE("p");

    public final String code;

    /**
     * Creates a color enum value based on a string.
     *
     * @param code the corresponding code for the color.
     */
    Color(String code) {
        this.code = code;
    }
}
