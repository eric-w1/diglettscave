package byog.Core;

import java.io.Serializable;

public class Status implements Serializable {
    int XP;
    int energy;

    private static final long serialVersionUID = 3459934539720L;

    public Status(int xp, int energy) {
        XP = xp;
        this.energy = energy;
    }
}
