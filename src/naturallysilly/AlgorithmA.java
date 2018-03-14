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

    private static final int INITIAL_CAPACITY = 32;
    private final PriorityQueue<CandyCrisis> queue;
    private final CandyCrisisComparator comparator;
    
    /**
     * prepares a new game to be run through A
     * @param game 
     */
    public AlgorithmA(CandyCrisis game) {
        comparator = new CandyCrisisComparator();
        queue = new PriorityQueue<>(INITIAL_CAPACITY, comparator);
        queue.add(game);
    }
    
    /**
     * Starts running Algorithm A
     */
    public void start() {
        CandyCrisis currentState, newState;
        Keys[] nextMoves;
        Keys empty;
        long startTime = System.currentTimeMillis();
        while (true) {
            currentState = queue.poll();
            if (currentState.isFinished()) {
                currentState.setStartTime(startTime);
                currentState.setEndTime(System.currentTimeMillis());
                currentState.WriteOutputFile();
                break;
            }
            nextMoves = currentState.getValidMoves();
            empty = currentState.getEmptyKey();
            for (Keys nextMove : nextMoves) {
                if (nextMove == null) {
                    break;
                }
                if (nextMove == currentState.getLastPosition()) {
                    continue;
                }
                newState = new CandyCrisis(currentState);
                newState.swap(empty, nextMove, true);
                queue.add(newState);
            }
        }
    }
}
