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

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        List<String> gameStrings = CandyCrisis.parseFile("game.txt");
        CandyCrisis game = new CandyCrisis(gameStrings.get(0));
        game.start();
    }
}
