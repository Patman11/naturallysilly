package naturallysilly;

import java.util.List;

/**
 * COMP 472 - Artificial Intelligence
 *
 * @author Patrick Bednarski - 40002239
 * @author Youssef Akallal - 25988322
 * @author Ali Douch - 27578253
 * @author Anthony Dubois - 26647375
 */
public class NaturallySilly {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        List<String> gameStrings = CandyCrisis.parseFile("game.txt");
        gameStrings.stream().map((gameString) -> new CandyCrisis(gameString)).map((game) -> new AlgorithmA(game)).forEachOrdered((a) -> {
            a.start();
        });
    }
}
