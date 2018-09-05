package byog.TileEngine;

import java.awt.Color;

/**
 * Contains constant tile objects, to avoid having to remake the same tiles in different parts of
 * the code.
 *
 * You are free to (and encouraged to) create and add your own tiles to this file. This file will
 * be turned in with the rest of your code.
 *
 * Ex:
 *      world[x][y] = Tileset.FLOOR;
 *
 * The style checker may crash when you try to style check this file due to use of unicode
 * characters. This is OK.
 */

public class Tileset {
    public static final TETile PLAYER = new TETile('@', Color.white, Color.black, "DIGLETT",
            "/Users/Eric/Google Drive/File Sharing/"
                    + "cs61b/sp18-proj2-axt-ace/proj2/byog/TileEngine/tiles/diglett.png");
    public static final TETile WALL = new TETile('#', new Color(216, 128, 128), Color.darkGray,
            "wall", "/Users/Eric/Google Drive/File "
            + "Sharing/cs61b/sp18-proj2-axt-ace/proj2/"
            + "byog/TileEngine/tiles/wall.png");
    public static final TETile FLOOR = new TETile('·', new Color(128, 192, 128), Color.black,
            "floor", "/Users/Eric/Google Drive/File "
            + "Sharing/cs61b/sp18-proj2-axt-ace/proj2/byog/TileEngine/tiles/floor.png");
    public static final TETile NOTHING = new TETile(' ', Color.black, Color.black, "nothing");
    public static final TETile DARKNESS = new TETile(' ', Color.black, Color.black, "darkness");
    public static final TETile BOSS = new TETile('▲', Color.blue, Color.black, "FINAL BOSS",
            "/Users/Eric/Google Drive/File "
                    + "Sharing/cs61b/sp18-proj2-axt-ace/proj2/byog/TileEngine/tiles/gengar.png");
//    public static final TETile FLOWER = new TETile('❀', Color.magenta, Color.pink, "flower");
//    public static final TETile LOCKED_DOOR = new TETile('█', Color.orange, Color.black,
//            "locked door");
    public static final TETile DOOR = new TETile('▢', Color.orange, Color.black,
            "pokeball", "/Users/Eric/Google Drive/File "
        + "Sharing/cs61b/sp18-proj2-axt-ace/proj2/byog/TileEngine/tiles/pokeball.png");
    public static final TETile GREENINDICATOR = new TETile('▒', Color.green, Color.black,
            "you are stronger than the boss", "/Users/Eric/Google Drive/File "
            + "Sharing/cs61b/sp18-proj2-axt-ace/proj2/byog/TileEngine/tiles/greenindicator.png");
//    public static final TETile MOUNTAIN = new TETile('▲', Color.gray, Color.black, "mountain");
    public static final TETile REDINDICATOR = new TETile('▒', Color.red, Color.black,
            "THE BOSS IS TOO STRONG!", "/Users/Eric/Google Drive/File "
        + "Sharing/cs61b/sp18-proj2-axt-ace/proj2/byog/TileEngine/tiles/redindicator.png");

    public static final TETile GEM1 = new TETile('❀', Color.magenta, Color.pink, "gem",
            "/Users/Eric/Google Drive/File "
                    + "Sharing/cs61b/sp18-proj2-axt-ace/proj2/byog/TileEngine/tiles/gem1.png");
    public static final TETile GEM2 = new TETile('❀', Color.magenta, Color.pink, "gem",
            "/Users/Eric/Google Drive/File "
                    + "Sharing/cs61b/sp18-proj2-axt-ace/proj2/byog/TileEngine/tiles/gem2.png");
    public static final TETile GEM3 = new TETile('❀', Color.magenta, Color.pink, "gem",
            "/Users/Eric/Google Drive/File "
                    + "Sharing/cs61b/sp18-proj2-axt-ace/proj2/byog/TileEngine/tiles/gem3.png");
}


