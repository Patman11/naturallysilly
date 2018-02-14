package naturallysilly;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

/**
 *
 * @author Patrick Bednarski
 * @author Youssef Akallal - 25988322
 *
 */
public class CandyCrisis {

    //maps input keys to grid array positions
    private static enum Keys {
        A('a', 0, 0),
        B('b', 0, 1),
        C('c', 0, 2),
        D('d', 0, 3),
        E('e', 0, 4),
        F('f', 1, 0),
        G('g', 1, 1),
        H('h', 1, 2),
        I('i', 1, 3),
        J('j', 1, 4),
        K('k', 2, 0),
        L('l', 2, 1),
        M('m', 2, 2),
        N('n', 2, 3),
        O('o', 2, 4);
        private final char VALUE;
        private final int HEIGHT;
        private final int WIDTH;

        Keys(char value, int height, int width) {
            this.VALUE = value;
            this.HEIGHT = height;
            this.WIDTH = width;
        }
    }

    private static final int WIDTH = 5;
    private static final int HEIGHT = 3;
    private static final String PADDING = "  ";
    private static final String V_LINE = "-------------------------------";
    private static final String H_LINE = "|";
    private static final String FILE_ERROR = "File not found or not permitted to read";
    private static final String ENTER_NEXT_MOVE = "Enter your next move or press x to exit: ";
    private static final String INVALID_MOVE = "Invalid move. Please try again.";
    private static final String FINISHED_GAME = "Finished game: ";
    private static final char NULL = '\u0000';

    private final char[][] grid;

    /* Create queue for storing user's moves */
    private final Queue<Character> moves;

    /**
     * Builds a new game using the passed string The empty spot will be set as null in the grid Call
     * startGame() to actually begin playing
     *
     *
     * @param gameString
     */
    public CandyCrisis(String gameString) {
        moves = new LinkedList<>();
        grid = new char[HEIGHT][WIDTH];
        char c;
        for (int n = 0; n < HEIGHT; ++n) {
            for (int m = 0; m < WIDTH; ++m) {
                c = gameString.charAt((n * WIDTH) + m);
                if (c == 'e') {
                    grid[n][m] = NULL;
                } else {
                    grid[n][m] = c;
                }
            }
        }
    }

    /*
     * Prints the grid to the console
     */
    private void display() {
        System.out.println(V_LINE);
        for (int n = 0; n < HEIGHT; ++n) {
            for (int m = 0; m < WIDTH; ++m) {
                System.out.print(H_LINE + PADDING + grid[n][m] + PADDING);
            }
            System.out.println(H_LINE);
            System.out.println(V_LINE);
        }
    }

    /*
     * Checks if the current configuration is a win
     *
     * @return result
     */
    private boolean isFinished() {
        boolean result = true;
        for (int n = 0; n < WIDTH; ++n) {
            if (grid[0][n] != grid[2][n]) {
                result = false;
                break;
            }
        }
        return result;
    }

    /**
     * Starts accepting user input so you can play the game
     */
    public final void start() {
        try (Scanner keyboard = new Scanner(System.in)) {
            boolean exit = false;
            while (!exit) {
                display();
                System.out.println(ENTER_NEXT_MOVE);
                char value = keyboard.next().charAt(0);
                if (value == 'x') {
                    exit = true;
                    displayPath();
                    System.out.println(FINISHED_GAME + isFinished());
                } else {
                    /*This is where we need to check that the move is valid*/
                    if (move(value)) {
                        moves.add(value);
                    } else {
                        System.out.println(INVALID_MOVE);
                    }
                }
            }
        }
    }
    
    /*
     * attempts to move the empty space
     * @param position the position entered by the user
     * @return if the move was executed
     */
    private boolean move(char position) {
        Keys key = getKeyByValue(position);
        Keys empty = getEmptyKey();
        if (key == null || empty == null) {
            return false;
        }
        if (validate(empty, key)) {
            swap(empty, key);
            return true;
        }
        return false;
    }
    
    /*
     * Checks if the target move is +/- 1 height XOR width from current empty position
     * @param target the target key
     * @return 
     */
    private boolean validate(Keys initial, Keys target) {
        if (initial != null) {
            return (Math.abs(target.HEIGHT - initial.HEIGHT) == 1) ^ (Math.abs(target.WIDTH - initial.WIDTH) == 1);
        }
        return false;
    }
    
    /*
     * Unvalidated swap of 2 characters, should not be used
     * from user input
     * @param initial position of empty space
     * @param target desired position of empty space
     */
    private void swap(Keys initial, Keys target) {
        char temp = grid[initial.HEIGHT][initial.WIDTH];
        grid[initial.HEIGHT][initial.WIDTH] = grid[target.HEIGHT][target.WIDTH];
        grid[target.HEIGHT][target.WIDTH] = temp;
    }
    
    /*
     * Loops through keys and returns a key if it matches the character value
     * @param value the key to be found
     * @return 
     */
    private Keys getKeyByValue(char value) {
        for (Keys key : Keys.values()) {
            if (value == key.VALUE) {
                return key;
            }
        }
        return null;
    }
    
    /*
     * Loops through all key positions to find empty box
     * @return key for the empty box
     */
    private Keys getEmptyKey() {
        for (Keys key : Keys.values()) {
            if (grid[key.HEIGHT][key.WIDTH] == NULL) {
                return key;
            }
        }
        return null;
    }

    /*
     * Display the path taken
     */
    private void displayPath() {
        Iterator<Character> iterator = moves.iterator();
        while (iterator.hasNext()) {
            char element = moves.remove();
            System.out.print(element + " ");
        }
        System.out.println();
    }

    /**
     * Generate game strings from the passed file location
     *
     * @param filename the location of the file to be read
     * @return an unmodifiable List of game strings
     */
    public static final List<String> parseFile(String filename) {
        String temp, parsed;
        List<String> gameStrings = new ArrayList<>();
        File gameFile = new File(filename);
        if (gameFile.exists() && gameFile.isFile() && gameFile.canRead()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
                temp = reader.readLine();
                while (temp != null) {
                    parsed = temp.replaceAll("\\s+", "").trim();
                    if (parsed.length() == WIDTH * HEIGHT) {
                        gameStrings.add(parsed);
                    }
                    temp = reader.readLine();
                }
            } catch (FileNotFoundException ex) {
                System.err.println(ex.toString());
            } catch (IOException ex) {
                System.err.println(ex.toString());
            }
        } else {
            System.err.println(FILE_ERROR);
            System.exit(1);
        }
        return Collections.unmodifiableList(gameStrings);
    }
}
