package naturallysilly;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileOutputStream;
import java.io.PrintWriter;
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
 * @author Patrick Bednarski - 40002239
 * @author Youssef Akallal - 25988322
 * @author Ali Douch - 27578253
 * @author Anthony Dubois - 26647375
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
    private static final String VALID_MOVES = "Valid moves: ";
    private static final String CURRENT_EVALUATION = "Current evaluation: ";
    private static final char NULL = '\u0000';

    private final char[][] grid; //actual game grid
    private final Keys[][] gridKeys; //grid displaying input keys, also used 
    private long startTime;
    private long endTime;

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
        gridKeys = new Keys[HEIGHT][WIDTH];
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
        for (Keys key : Keys.values()) {
            gridKeys[key.HEIGHT][key.WIDTH] = key;
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
            startTime = System.nanoTime();
            while (!exit) {
                display();
                displayValidMoves();
                System.out.println(CURRENT_EVALUATION + evaluatePosition());
                if (isFinished()) {
                    WriteOutputFile();
                    endTime = System.nanoTime();
                    System.out.println(FINISHED_GAME + "true");
                    break;
                }
                System.out.println(ENTER_NEXT_MOVE);
                char value = keyboard.next().charAt(0);
                if (value == 'x') {
                    endTime = System.nanoTime();
                    WriteOutputFile();
                    exit = true;
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
        return (Math.abs(target.HEIGHT - initial.HEIGHT) == 1
                && Math.abs(target.WIDTH - initial.WIDTH) == 0)
                ^ (Math.abs(target.WIDTH - initial.WIDTH) == 1
                && Math.abs(target.HEIGHT - initial.HEIGHT) == 0);
    }

    /*
     * Computes the valid moves fromt the current empty position
     * IMPORTANT: always returns an array of length 4
     * Valid moves will be inserted sequentially into the array
     * Don't be clever and change this to an arraylist
     * This is done to keep the heuristic evaluation as fast as possible
     * @return the valid moves
     */
    private Keys[] getValidMoves() {
        int currentArrayPosition = 0;
        Keys[] validMoves = new Keys[4];
        Keys empty = getEmptyKey();
        if (empty != null) {
            if (empty.HEIGHT - 1 >= 0) {
                validMoves[currentArrayPosition] = gridKeys[empty.HEIGHT - 1][empty.WIDTH];
                ++currentArrayPosition;
            }
            if (empty.WIDTH + 1 < WIDTH) {
                validMoves[currentArrayPosition] = gridKeys[empty.HEIGHT][empty.WIDTH + 1];
                ++currentArrayPosition;
            }
            if (empty.HEIGHT + 1 < HEIGHT) {
                validMoves[currentArrayPosition] = gridKeys[empty.HEIGHT + 1][empty.WIDTH];
                ++currentArrayPosition;
            }
            if (empty.WIDTH - 1 >= 0) {
                validMoves[currentArrayPosition] = gridKeys[empty.HEIGHT][empty.WIDTH - 1];
                ++currentArrayPosition;
            }
        }
        return validMoves;
    }

    /**
     * Displays the valid moves to console
     */
    private void displayValidMoves() {
        Keys[] validMoves = getValidMoves();
        System.out.print(VALID_MOVES);
        for (Keys key : validMoves) {
            if (key != null) {
                System.out.print(key.VALUE + ", ");
            } else {
                break;
            }
        }
        System.out.println();
    }
    
    /*
     * Heuristic, searches for the closest matching charater
     * for each character in the bottom row
     * @return the evaluation
     */
    private int evaluatePosition() {
        int result = 0, bottomRow = HEIGHT - 1, toMatchY, toMatchX;
        char toMatch;
        for (int n = 0; n < WIDTH; ++n) {
            toMatch = grid[bottomRow][n];
            if (toMatch == NULL) {
                continue;
            }
            outerloop:
            for (int y = 0; y < HEIGHT; ++y) {
                //search row towards the left
                for (int x = n; x < WIDTH; ++x) {
                    if (toMatch == grid[y][x]) {
                        toMatchY = bottomRow - (bottomRow - y); //this should always be positive
                        toMatchX = (n - x < 0 ? x - n : n - x); //might be negagive, make it positive
                        result += toMatchY + toMatchX;
                        break outerloop;
                    }
                }
                //search row towards the right
                for (int x = n - 1; x >= 0; --x) {
                    if (toMatch == grid[y][x]) {
                        toMatchY = bottomRow - (bottomRow - y); //this should always be positive
                        toMatchX = (n - x < 0 ? x - n : n - x); //might be negagive, make it positive
                        result += toMatchY + toMatchX;
                        break outerloop;
                    }
                }
            }
        }
        return result;
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
     * @param output the output file to also write the path too
     */
    private void displayPath(PrintWriter output) {
        Iterator<Character> iterator = moves.iterator();
        while (iterator.hasNext()) {
            char element = moves.remove();
            System.out.print(element + " ");
            output.print(element + " ");
        }
        System.out.println();
        output.println();
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

    /**
     * Open the output file and check for error
     */
    public void WriteOutputFile() {
        try (PrintWriter output = new PrintWriter(new FileOutputStream("output.txt"))) {
            long totalTime = endTime - startTime;
            displayPath(output);
            output.println(totalTime + "ms");
        } catch (FileNotFoundException e) {
            System.out.println("Error opening the file output.txt");
            System.exit(0);
        }
    }
}
