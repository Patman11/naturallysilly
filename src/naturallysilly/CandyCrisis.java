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
    
    private static int objectCount = 1;
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
    private static final Keys[][] GRID_KEYS = new Keys[HEIGHT][WIDTH]; //grid displaying input keys

    private final char[][] grid; //actual game grid 
    private Keys lastPosition; //will store the last position upon invoking swap()
    private int cost;
    private long endTime;
    private long startTime;
    private final Queue<Character> moves;
    
    /**
     * This object's ID
     */
    public final int id; //id can be public since it's final anyway
    
    /**
     * 
     * @return the start time 
     */
    public final long getStartTime() {
        return startTime;
    }
    
    /**
     * Sets the start time
     * @param startTime 
     */
    public final void setStartTime(long startTime) {
        this.startTime = startTime;
    }
    
    /**
     * Sets the end time
     * @param endTime 
     */
    public final void setEndTime(long endTime) {
        this.endTime = endTime;
    }
    
    /**
     * 
     * @return the last position
     */
    public final Keys getLastPosition() {
        return lastPosition;
    }
    
    /**
     * 
     * @return the cost
     */
    public final int getCost() {
        return cost;
    }
    
    /**
     * 
     * @return a deep copy of the moves
     */
    public final Queue<Character> getMoves() {
        Queue<Character> movesCopy = new LinkedList<>();
        moves.forEach((move) -> {movesCopy.add(move);});
        return movesCopy;
    }
    
    /**
     * 
     * @return a deep copy of the moves
     */
    public final char[][] getGrid() {
        char[][] gridCopy = new char[HEIGHT][WIDTH];
        for (int n = 0; n < HEIGHT; ++n) {
            for (int m = 0; m < WIDTH; ++m) {
                gridCopy[n][m] = grid[n][m];
            }
        }
        return gridCopy;
    } 

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
        lastPosition = null;
        cost = 0;
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
        id = objectCount;
        ++objectCount;
        if (GRID_KEYS[0][0] == null) {
            generateKeyMap();
        }
    }
    
    /**
     * Copies a game, preserves the ID
     * @param orig 
     */
    public CandyCrisis(CandyCrisis orig) {
        moves = orig.getMoves();
        grid = orig.getGrid();
        lastPosition = orig.getLastPosition();
        cost = orig.getCost();
        id = orig.id; //DONT INCREMENT THE ID
    }
    
    /**
     * Starts accepting user input so you can play the game
     */
    public final void userStart() {
        try (Scanner keyboard = new Scanner(System.in)) {
            boolean exit = false;
            startTime = System.nanoTime();
            while (!exit) {
                display();
                displayValidMoves();
                System.out.println(CURRENT_EVALUATION + heuristic());
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

    /**
     * Checks if the current configuration is a win
     *
     * @return result
     */
    public final boolean isFinished() {
        boolean result = true;
        for (int n = 0; n < WIDTH; ++n) {
            if (grid[0][n] != grid[2][n]) {
                result = false;
                break;
            }
        }
        return result;
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
            swap(empty, key, true);
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

    /**
     * Computes the valid moves fromt the current empty position
     * IMPORTANT: always returns an array of length 4
     * Valid moves will be inserted sequentially into the array
     * Don't be clever and change this to an arraylist
     * This is done to keep the heuristic evaluation as fast as possible
     * @return the valid moves
     */
    public final Keys[] getValidMoves() {
        int currentArrayPosition = 0;
        Keys[] validMoves = new Keys[4];
        Keys empty = getEmptyKey();
        if (empty != null) {
            if (empty.HEIGHT - 1 >= 0) {
                validMoves[currentArrayPosition] = GRID_KEYS[empty.HEIGHT - 1][empty.WIDTH];
                ++currentArrayPosition;
            }
            if (empty.WIDTH + 1 < WIDTH) {
                validMoves[currentArrayPosition] = GRID_KEYS[empty.HEIGHT][empty.WIDTH + 1];
                ++currentArrayPosition;
            }
            if (empty.HEIGHT + 1 < HEIGHT) {
                validMoves[currentArrayPosition] = GRID_KEYS[empty.HEIGHT + 1][empty.WIDTH];
                ++currentArrayPosition;
            }
            if (empty.WIDTH - 1 >= 0) {
                validMoves[currentArrayPosition] = GRID_KEYS[empty.HEIGHT][empty.WIDTH - 1];
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
    
    /**
     * Runs the heuristic and adds the current cost
     * @return 
     */
    public final int cost() {
        return heuristic() + cost;
    }
    
    /*
     * Heuristic, searches for the closest matching charater
     * for each character in the bottom row
     * @return the evaluation
     */
    private int heuristic() {
        int result = 0, bottomRow = HEIGHT - 1, toMatchY, toMatchX;
        char toMatch;
        for (int n = 0; n < WIDTH; ++n) {
            toMatch = grid[bottomRow][n];
            if (toMatch == NULL) {
                ++result;
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
    
    private void steepestAscent() {
        Keys[] nextMoves;
        Keys empty;
        int currentEvaluation, nextEvaluation, bestEvaluationIndex;
        while (!isFinished()){
            currentEvaluation = heuristic();
            nextMoves = getValidMoves();
            bestEvaluationIndex = 0;
            empty = getEmptyKey();
            for (int n = 0; n < nextMoves.length; ++n) {
                if (nextMoves[n] == null) {
                    break;
                }
                if (nextMoves[n] == lastPosition) {
                    continue;
                }
                swap(empty, nextMoves[n], false);
                nextEvaluation = heuristic();
                if (nextEvaluation < currentEvaluation) {
                    currentEvaluation = nextEvaluation;
                    bestEvaluationIndex = n;
                }
                swap(nextMoves[n], empty, false);                
            }
            swap(empty, nextMoves[bestEvaluationIndex], true);
            moves.add(nextMoves[bestEvaluationIndex].VALUE);
        }
    }

    /**
     * Unvalidated swap of 2 characters, should not be used
     * from user input
     * @param initial position of empty space
     * @param target desired position of empty space
     * @param replaceLastPosition when committing to a move, set to true so the previous move is preserved
     */
    public void swap(Keys initial, Keys target, boolean replaceLastPosition) {
        char temp = grid[initial.HEIGHT][initial.WIDTH];
        grid[initial.HEIGHT][initial.WIDTH] = grid[target.HEIGHT][target.WIDTH];
        grid[target.HEIGHT][target.WIDTH] = temp;
        if (replaceLastPosition) {
            ++cost;
            lastPosition = initial;
            moves.add(target.VALUE);
        }
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

    /**
     * Loops through all key positions to find empty box
     * @return key for the empty box
     */
    public final Keys getEmptyKey() {
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
    
    /*
     * Builds the key map
     */
    private static void generateKeyMap() {
        for (Keys key : Keys.values()) {
            GRID_KEYS[key.HEIGHT][key.WIDTH] = key;
        }
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
        try (PrintWriter output = new PrintWriter(new FileOutputStream("output" + id + ".txt"))) {
            long totalTime = endTime - startTime;
            displayPath(output);
            output.println(totalTime + "ms");
        } catch (FileNotFoundException e) {
            System.out.println("Error opening the file output.txt");
            System.exit(0);
        }
    }
}
