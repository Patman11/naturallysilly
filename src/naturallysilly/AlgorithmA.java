package naturallysilly;

import java.util.PriorityQueue;

/**
 *
 * @author Patrick Bednarski - 40002239
 * @author Youssef Akallal - 25988322
 * @author Ali Douch - 27578253
 * @author Anthony Dubois - 26647375
 */
public class AlgorithmA {

    private static final int INITIAL_CAPACITY = 16;
    private final PriorityQueue<CandyCrisis> queue;
    private final CandyCrisisComparator comparator;
    
    public AlgorithmA(CandyCrisis game) {
        comparator = new CandyCrisisComparator();
        queue = new PriorityQueue<>(INITIAL_CAPACITY, comparator);
        
    }
}