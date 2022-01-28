package g55803.samegame.terminal.termview;

import g55803.samegame.model.Color;
import g55803.samegame.model.Facade;
import g55803.samegame.model.Position;
import g55803.samegame.utils.ParsingUtils;

import java.util.HashMap;

/**
 * Provides a view in the terminal for same game.
 *
 * @author Nathan Furnal
 */
public class TerminalView {
    private final HashMap<String, String> colorMap = new HashMap<>();
    private final Facade model;
    private boolean canUseEmojis = true;

    /**
     * Creates a view using a facade of the model (the field). The basic colors are mapped to emojis
     * in the terminal.
     *
     * @param model the model of the game.
     */
    public TerminalView(Facade model) {
        this.model = model;
        colorMap.put("r", "\uD83D\uDFE5");
        colorMap.put("g", "\uD83D\uDFE9");
        colorMap.put("b", "\uD83D\uDFE6");
        colorMap.put("y", "\uD83D\uDFE8");
        colorMap.put("p", "\uD83D\uDFEA");
    }

    /**
     * Displays a welcome banner with information about the game.
     */
    public void startBanner() {
        String banner = """
                =========================
                Welcome to the Same Game!
                =========================
                                
                In this game, you have to select tiles of the same color to make them disappear,
                the goal is to clean the board!
                                
                -----
                Help (You can call this again by entering 'help' in the terminal)
                -----
                                
                > show          --> shows the board
                > play row col  --> plays the move at (col, row) e.g : > play 1 2
                > undo          --> undoes a move
                > redo          --> redoes a move
                > reset         --> leaves the current game and starts a new one, you can also use 'again'
                > exit          --> exits the game, you can also use 'quit'
                > help          --> displays the help menu
                                
                Please enter the size of the board you'd like to play with and the number of colors (between 2 and 6).
                              
                """;
        System.out.println(banner);
    }

    /**
     * Makes the string representation of the board.
     *
     * @return the string repr of the board.
     */
    private String makeFieldString() {
        StringBuilder s = new StringBuilder();
        s.append(" ").append(1).append(" ".repeat(2 * (model.getNCols() - 1))).append(model.getNCols());
        s.append("\n");
        // Add a bit of padding
        String lining = "-".repeat(2 * model.getNCols() + 2);
        s.append(lining).append("\n");
        for (int i = 0; i < model.getNRows(); i++) {
            s.append("|");
            for (int j = 0; j < model.getNCols(); j++) {
                Position p = new Position(i, j);
                if (model.getTile(p) != null) {
                    if (canUseEmojis) {
                        s.append(colorMap.get(model.getTile(p).getColor().code));
                    } else {
                        s.append(model.getTile(p).getColor().code).append(" ");
                    }
                } else {
                    s.append("  ");
                }
            }
            s.append("| ").append(i + 1).append("\n");
        }
        s.append(lining).append("\n");
        s.append("Score : ").append(model.getScore());
        return s.toString();
    }

    /**
     * Asks the user for a positive number of rows.
     *
     * @return the number of rows input.
     */
    private int setupRows() {
        int out = ParsingUtils.readUserInt("Pick a number of rows for the board: ");
        int lim = 2;
        while (out < lim) {
            out = ParsingUtils.readUserInt("Please pick a positive number of rows over " + lim + ".");
        }
        return out;
    }

    /**
     * Asks the user for a positive number of columns.
     *
     * @return the number of columns input.
     */
    private int setupCols() {
        int out = ParsingUtils.readUserInt("Pick a number of columns for the board:");
        int lim = 2;
        while (out < lim) {
            out = ParsingUtils.readUserInt("Please pick a positive number of columns over " + lim + ".");
        }
        return out;
    }

    /**
     * Asks the user for a number of colors between 2 and 6.
     *
     * @return the number of colors input.
     */
    private int setupColors() {
        String msg = "between 2 and " + Color.values().length + ": ";
        int out = ParsingUtils.readUserInt("Please select a number of colors " + msg);
        while (out < 2 || out > Color.values().length) {
            out = ParsingUtils.readUserInt("Please select a value " + msg);
        }
        return out;
    }

    /**
     * Asks the user if the terminal is emoji capable in order to play the game.
     *
     * @return true if the user's answers 'y' and false otherwise.
     */
    private boolean setupEmojis() {
        String msg = "Can your terminal render emojis? (y/n) ";
        String out = ParsingUtils.readUserString(msg).toLowerCase();
        while (!(out.equals("y") || out.equals("n"))) {
            out = ParsingUtils.readUserString(msg);
        }
        return out.equals("y");
    }

    /**
     * Asks the user for the board parameters and set it up.
     */
    public void setUpBoard() {
        System.out.println("Playing with 12 rows and 16 columns are good values for the game.");
        canUseEmojis = setupEmojis();
        int nRows = setupRows();
        int nCols = setupCols();
        int nColors = setupColors();
        model.newField(nRows, nCols, nColors);
    }

    /**
     * Displays the help banner.
     */
    public void showHelp() {
        String helpBanner = """
                              --------
                                Help
                              --------
                                
                 > show          --> shows the board
                 > play row col  --> plays the move at (col, row) e.g : > play 1 2
                 > undo          --> undoes a move
                 > redo          --> redoes a move
                 > reset         --> leaves the current game and starts a new one, you can also use 'again'
                 > exit          --> exits the game, you can also use 'quit'
                 > help          --> displays the help menu
                """;
        System.out.println(helpBanner);
    }

    /**
     * Reads the string input by the user then turns it lowercase and trim the edges.
     *
     * @param msg the message to display to the user.
     * @return the curated string.
     */
    public String readUserString(String msg) {
        return ParsingUtils.readUserString(msg);
    }

    /**
     * Splits a string on whitespaces.
     *
     * @param str the string to split.
     * @return the split string.
     */
    public String[] splitString(String str) {
        return ParsingUtils.splitString(str);
    }

    public void show() {
        String field = makeFieldString();
        System.out.println(field);
    }
}
