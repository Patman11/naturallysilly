package naturallysilly;

import java.util.Comparator;

/**
 *
 * @author Patrick Bednarski - 40002239
 * @author Youssef Akallal - 25988322
 * @author Ali Douch - 27578253
 * @author Anthony Dubois - 26647375
 */
public class CandyCrisisComparator implements Comparator<CandyCrisis> {

    @Override
    public int compare(CandyCrisis a, CandyCrisis b) {
        int costA, costB;
        costA = a.cost();
        costB = b.cost();
        if (costA < costB) {
            return -1;
        }
        if (costA > costB) {
            return 1;
        }
        return 0;
    }
}
