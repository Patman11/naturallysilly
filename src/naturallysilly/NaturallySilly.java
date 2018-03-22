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
        List<String> easyStrings = CandyCrisis.parseFile("input1.txt");
        List<String> mediumStrings = CandyCrisis.parseFile("input2.txt");
        List<String> expertStrings = CandyCrisis.parseFile("input3.txt");
        List<String> masterStrings = CandyCrisis.parseFile("input4.txt");
        easyStrings.stream().map((gameString) -> new CandyCrisis(gameString, 1)).map(AlgorithmA::new).forEachOrdered(AlgorithmA::start);
        mediumStrings.stream().map((gameString) -> new CandyCrisis(gameString, 2)).map(AlgorithmA::new).forEachOrdered(AlgorithmA::start);
        expertStrings.stream().map((gameString) -> new CandyCrisis(gameString, 3)).map(AlgorithmA::new).forEachOrdered(AlgorithmA::start);
        masterStrings.stream().map((gameString) -> new CandyCrisis(gameString, 4)).map(AlgorithmA::new).forEachOrdered(AlgorithmA::start);
    }
}
