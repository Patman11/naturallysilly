package naturallysilly;

/**
 *
 * @author Patrick Bednarski - 40002239
 * @author Youssef Akallal - 25988322
 * @author Ali Douch - 27578253
 * @author Anthony Dubois - 26647375
 * 
 * This enum maps inputs to array grid position
 */

public enum Keys {
    A('A', 0, 0),
    B('B', 0, 1),
    C('C', 0, 2),
    D('D', 0, 3),
    E('E', 0, 4),
    F('F', 1, 0),
    G('G', 1, 1),
    H('H', 1, 2),
    I('I', 1, 3),
    J('J', 1, 4),
    K('K', 2, 0),
    L('L', 2, 1),
    M('M', 2, 2),
    N('N', 2, 3),
    O('O', 2, 4);
    public final char VALUE;
    public final int HEIGHT;
    public final int WIDTH;

    Keys(char value, int height, int width) {
        this.VALUE = value;
        this.HEIGHT = height;
        this.WIDTH = width;
    }
}
