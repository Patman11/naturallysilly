package naturallysilly;

/**
 *
 * @author Patrick Bednarski - 40002239
 * @author Youssef Akallal - 25988322
 * @author Ali Douch - 27578253
 * @author Anthony Dubois - 26647375
 */
public enum Keys {
    A('a', 0, 0),
    B('b', 0, 1),
    C('c', 0, 2),
    D('d', 0, 3),
    E('e', 0, 4),
    F('f', 1, 0),
    G('g', 1, 1),
    H('h', 1, 2),
    I('i', 1, 3),
    J('j', 1, 4),
    K('k', 2, 0),
    L('l', 2, 1),
    M('m', 2, 2),
    N('n', 2, 3),
    O('o', 2, 4);
    public final char VALUE;
    public final int HEIGHT;
    public final int WIDTH;

    Keys(char value, int height, int width) {
        this.VALUE = value;
        this.HEIGHT = height;
        this.WIDTH = width;
    }
}
