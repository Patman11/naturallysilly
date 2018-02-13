package naturallysilly;

import java.util.List;

/**
 * COMP 472 - Artificial Intelligence
 * Candy Crisis
 * @author Patrick Bednarski - 40002239
 * @author Youssef Akallal - 25988322
 * @author 
 * @author 
 */
public class NaturallySilly {
    
    public static boolean DEBUG = false; //add "--debug" as command line argument to enable verbose operation

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        for (String arg : args) {
            if (arg.equals("--debug")) {
                DEBUG = true;
            }
        }
        List<String> gameStrings = CandyCrisis.parseFile("src/naturallysilly/game.txt");
        CandyCrisis game = new CandyCrisis(gameStrings.get(0));
        game.display();
        
        game.getUserInput();
        game.displayPath();
        
        System.out.println("Is game finished: " + game.isFinished());
    }
}
