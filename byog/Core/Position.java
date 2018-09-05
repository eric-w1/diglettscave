package byog.Core;

import java.io.Serializable;

public class Position implements Serializable {
    private final int X;
    private final int Y;

    private static final long serialVersionUID = 8234092349020L;

    public Position(int x, int y) {
        X = x;
        Y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null) {
            return true;
        }

        if (getClass() != o.getClass()) {
            return false;
        }

        Position p = (Position) o;
        return X == p.X && Y == p.Y;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hashCode(this);
    }

    public int getX() {
        return X;
    }

    public int getY() {
        return Y;
    }
}
