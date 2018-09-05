package byog.Core;

import byog.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;

public class HUDEngine {
    Interactable interactiveMap;
    static int w = Game.WIDTH;
    static int h = Game.HEIGHT;
    static int bar = Game.TOPBARHEIGHT;

    public HUDEngine(Interactable interactable) {
        interactiveMap = interactable;
    }

    public void displayMouseInfo() {
        Font font = new Font("Pokemon GB", Font.PLAIN, 13);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.white);
        String desc = "";
        try {
            TETile mouseTile = interactiveMap.overlayMap[(int) StdDraw.mouseX()]
                    [(int) StdDraw.mouseY()];
            desc = mouseTile.description();
        } catch (IndexOutOfBoundsException e) {
            assert true;
        }
        StdDraw.textLeft(0, h + bar / 2.0, desc);
        StdDraw.show();
    }

    public void displayStatus(String characterName) {
        StdDraw.setPenColor(Color.white);
        String text = characterName + " ♂ - " + interactiveMap.player.status.XP
                + " XP" + "  " + interactiveMap.player.status.energy + " energy points";
        StdDraw.textRight(w, h + bar / 2.0, text);
        if (interactiveMap.bossSlain) {
            StdDraw.text(w / 2.9, h + bar / 2.0, "Go pick up the Pokeball!");
        }
        StdDraw.show();
    }
}
