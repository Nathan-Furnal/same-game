package g55803.samegame.terminal.controller;

import g55803.samegame.model.Facade;
import g55803.samegame.model.Position;
import g55803.samegame.terminal.termview.TerminalView;

/**
 * Provides a controller for the terminal view part of the game. It uses hard-coded commands with switches
 * to allow basic interaction.
 *
 * @author Nathan Furnal
 */
public class TermController {
    private static final String showCommand = "show";
    private static final String playCommand = "play";
    private static final String undoCommand = "undo";
    private static final String redoCommand = "redo";
    private static final String quitCommand = "quit";
    private static final String exitCommand = "exit";
    private static final String againCommand = "again";
    private static final String resetCommand = "reset";
    private static final String helpCommand = "help";
    private final TerminalView view;
    private final Facade model;

    public TermController(Facade model, TerminalView view) {
        this.view = view;
        this.model = model;
    }


    /**
     * Reads and parses the user input. The first argument defines the command and the behavior of the method
     * changes accordingly. There is also an option to quit the program. Default behavior for unrecognized commands
     * is to ask the user to enter something different.
     *
     * @param args command line arguments.
     */
    private void parseCalls(String[] args) {
        String command = args[0].strip();
        switch (command) {
            case showCommand -> view.show();
            case playCommand -> parsePlayCommand(args);
            case resetCommand, againCommand -> {
                view.setUpBoard();
                view.show();
            }
            case undoCommand -> undo();
            case redoCommand -> redo();
            case helpCommand -> view.showHelp();
            case quitCommand, exitCommand -> exit();
            default -> System.out.println("This command is not known. Please try something else.");
        }
    }

    /**
     * Exits the console with a message.
     */
    private void exit() {
        System.out.println("Thanks for playing, bye!");
        System.exit(0);
    }

    /**
     * Utility method to parse the play command.
     *
     * @param args the arguments to pass to the command.
     */
    private void parsePlayCommand(String[] args) {
        try {
            int x = Integer.parseInt(args[1].strip()) - 1;
            int y = Integer.parseInt(args[2].strip()) - 1;
            model.play(new Position(x, y));
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number to play.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Please enter a sufficient number of arguments.");
        }
        view.show();
    }

    /**
     * Utility method asking the user to play again.
     */
    private void parsePlayAgain() {
        String out = view.readUserString("Do you want to play again? (y/n)");
        while (!(out.equals("y") || out.equals("n"))) {
            out = view.readUserString("Do you want to play again? (y/n)");
        }
        if (out.equals("y")) {
            view.setUpBoard();
            view.show();
        } else {
            exit();
        }
    }

    /**
     * Undoes the last action by calling the facade.
     */
    public void undo() {
        if (model.isEmptyUndo()) {
            System.out.println("There is nothing to undo!");
        } else {
            model.undo();
            view.show();
        }
    }

    /**
     * Redoes the last action by calling the facade.
     */
    public void redo() {
        if (model.isEmptyRedo()) {
            System.out.println("There is nothing to redo!");
        } else {
            model.redo();
            view.show();
        }
    }

    /**
     * Main application loop.
     */
    public void start() {
        view.startBanner();
        view.setUpBoard();
        view.show();
        while (true) {
            // Read and parse user input.
            String command = view.readUserString("> ");
            String[] commands = view.splitString(command);
            parseCalls(commands);
            // Asks to play again whether the game is won or lost, condition order is important.
            if (model.isGameWon()) {
                System.out.println("AWESOME, You've won!");
                parsePlayAgain();
            }
            if (model.isGameOver()) {
                System.out.println("Oh no, you've lost :(");
                parsePlayAgain();
            }
        }
    }
}
