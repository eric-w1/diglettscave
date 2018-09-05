package byog.Core;

import byog.TileEngine.TETile;

import java.io.Serializable;

public class Gem implements Serializable {
    int xPos;
    int yPos;
    int xPvalue;
    TETile colourVariant;


    public Gem(int x, int y, int xPvalue, TETile tileType) {
        xPos = x;
        yPos = y;
        this.xPvalue = xPvalue;
        colourVariant = tileType;
    }
}
