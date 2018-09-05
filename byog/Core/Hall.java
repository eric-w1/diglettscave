package byog.Core;

public class Hall {
    private final int LENGTH;
    private final int ORIENTATION; //0 is up, 1 is right, 2 is down, 3 is left
    private final int X; //starting tip
    private final int Y;

    public Hall(int length, int orientation, int x, int y) {
        LENGTH = length;
        ORIENTATION = orientation;
        X = x;
        Y = y;
    }

    public Position endpoint() {
        switch (ORIENTATION) {
            case 0: return new Position(X, Y + LENGTH - 1);
            case 1: return new Position(X + LENGTH - 1, Y);
            case 2: return new Position(X, Y - LENGTH + 1);
            case 3: return new Position(X - LENGTH + 1, Y);
            default: return new Position(X, Y + LENGTH - 1);
        }
    }

    public int getORIENTATION() {
        return ORIENTATION;
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
}
