package naturallysilly;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Patrick Bednarski
 */
public final class CandyCrisis {

    private static final int WIDTH = 5;
    private static final int HEIGHT = 3;
    private static final String PADDING = "  ";
    private static final String V_LINE = "-------------------------------";
    private static final String H_LINE = "|";
    private static final char NULL = '\u0000';
    
    private final char[][] grid;
    
    /**
     * Builds a new game using the passed string
     * The empty spot will be set as null in the grid
     * @param gameString
     */
    public CandyCrisis(String gameString) {
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
    
    /**
     * Prints the grid to the console
     */
    public final void display() {
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
    
    /**
     * Generate game strings from the passed file location 
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
            } catch (FileNotFoundException ex ) {
                System.err.println(ex.toString());
            } catch (IOException ex) {
                System.err.println(ex.toString());
            }
        } else {
            System.err.println("File not found or not permitted to read");
            System.exit(1);
        }
        return Collections.unmodifiableList(gameStrings);
    }
}
