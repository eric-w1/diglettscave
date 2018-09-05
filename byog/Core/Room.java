package byog.Core;

import java.io.Serializable;

public class Room implements Serializable {
    private final int WIDTH;
    private final int LENGTH;
    private final int X; //bottom left corner
    private final int Y;
    private final Position[] EXITS; //index 0 is up, 1 is right, 2 is down, 3 is left
    private static final long serialVersionUID = 1231243243534534L;

    public Room(int w, int l, int x, int y, Position[] exits) {
        WIDTH = w;
        LENGTH = l;
        X = x;
        Y = y;
        EXITS = exits;
    }

    public int getWIDTH() {
        return WIDTH;
    }

    public int getLENGTH() {
        return LENGTH;
    }

    public int getX() {
        return X;
    }

    public int getY() {
        return Y;
    }

    public Position[] getEXITS() {
        return EXITS;
    }

}
