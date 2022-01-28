package g55803.samegame.utils;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Provides parsing utilities for user input.
 *
 * @author Nathan Furnal
 */
public class ParsingUtils {

    /**
     * Reads user input in a robust manner.
     *
     * @param message the message to display to the user.
     * @return the integer entered.
     */
    public static int readUserInt(String message) {
        Scanner s = new Scanner(System.in);
        do {
            try {
                System.out.println(message);
                return s.nextInt();
            } catch (InputMismatchException e) {
                s.nextLine();
                System.out.println("Please enter an integer.");
            }
        } while (!s.hasNextInt());

        return s.nextInt();
    }

    /**
     * Reads the string input by the user then turns it lowercase and trim the edges.
     *
     * @param message the message to display to the user.
     * @return the curated string.
     */
    public static String readUserString(String message) {
        Scanner s = new Scanner(System.in);
        System.out.printf("%s", message);
        return s.nextLine().toLowerCase().trim();
    }

    /**
     * Splits a string on whitespaces.
     *
     * @param str the string to split.
     * @return the split string.
     */
    public static String[] splitString(String str) {
        return str.split(" ");
    }

}
