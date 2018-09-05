package byog.Core;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;

public class StartGUI {
    static int w = Game.WIDTH;
    static int h = Game.HEIGHT;
    
    public static void drawStartingScreen() {
        StdDraw.picture(40, 6, "/Users/Eric/Google Drive/File "
                + "Sharing/cs61b/sp18-proj2-axt-ace/proj2/byog/TileEngine/tiles/editeddiglettscave.png");
        Font font = new Font("Pokemon Solid", Font.PLAIN, 50);
        StdDraw.setFont(font);
        StdDraw.text(w / 2.0, h * 8 / 12.0, "DIGLETT'S CAVE");
        Font font1 = new Font("Pokemon Hollow", Font.PLAIN, 30);
        StdDraw.setFont(font1);
        StdDraw.text(w / 2.0, h * 6 / 12.0, "new game (N)");
        StdDraw.text(w / 2.0, h * 5 / 12.0, "load game (L)");
        StdDraw.text(w / 2.0, h * 4 / 12.0, "quit (Q)");
        StdDraw.show();
    }

    public static long getSeedInput() {
        String seed = "";
        drawSeedScreen();
        while (true) {
            StdDraw.text(w / 2.0, h * 6 / 12.0, seed);
            StdDraw.show();
            if (StdDraw.hasNextKeyTyped()) {
                char key = Character.toUpperCase(StdDraw.nextKeyTyped());
                if (key == 'S') {
                    StdDraw.clear(new Color(0, 0, 0));
                    StdDraw.show();
                    break;
                }
                seed += key;
                StdDraw.clear(new Color(0, 0, 0));
                drawSeedScreen();
            }

        }
        return Long.parseLong(seed);
    }

    public static void drawSeedScreen() {
        StdDraw.setPenColor(Color.white);
        StdDraw.clear(new Color(0, 0, 0));
        StdDraw.text(w / 2.0, h * 8 / 12.0, "Please enter a seed:");
        StdDraw.show();
    }

    public static void drawLossScreen(String lossReason) {
        StdDraw.clear(new Color(0, 0, 0));
        if (lossReason == "you have been slain") {
            StdDraw.picture(40, 10, "/Users/Eric/Google Drive/File "
                    + "Sharing/cs61b/sp18-proj2-axt-ace/proj2/byog/TileEngine/tiles/gengarattack2.png");
        } else {
            StdDraw.picture(40, 15, "/Users/Eric/Google Drive/File "
                    + "Sharing/cs61b/sp18-proj2-axt-ace/proj2/byog/TileEngine/tiles/diglettstopped.png");
        }
        Font font = new Font("Pokemon GB", Font.BOLD, 60);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(w / 2.0, h * 8 / 12.0, "YOU LOSE");
        StdDraw.text(w / 2.0, h / 3.0, lossReason);
        StdDraw.show();
    }

    public static void drawWinScreen() {
        StdDraw.clear(new Color(0, 0, 0));
        StdDraw.picture(40, 10, "/Users/Eric/Google Drive/File "
                + "Sharing/cs61b/sp18-proj2-axt-ace/proj2/byog/TileEngine/tiles/gengarcaptured.png");
        Font font = new Font("Pokemon GB", Font.BOLD, 50);
        StdDraw.setFont(font);
        StdDraw.text(w / 2.0, h / 2.0, "YOU HAVE CAPTURED GENGAR");
        StdDraw.show();
    }
    
}
