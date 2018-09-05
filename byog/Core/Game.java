package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.awt.Color;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    public static final int TOPBARHEIGHT = 2;

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    //use for phase2
    public void playWithKeyboard() {
        ter.initialize(WIDTH, HEIGHT + TOPBARHEIGHT, 0, 0);
        StdDraw.setPenColor(Color.white);
        StartGUI.drawStartingScreen();
        char firstKey;
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                firstKey = Character.toUpperCase(StdDraw.nextKeyTyped());
                break;
            }
        }
        if (firstKey == 'N') {
            long seed = StartGUI.getSeedInput();
            LayoutEngine mapGenerator = new LayoutEngine(seed, WIDTH, HEIGHT);
            TETile[][] map = mapGenerator.generateWorld();
            Interactable interactiveMap = new Interactable(map, mapGenerator);
            HUDEngine hudEngine = new HUDEngine(interactiveMap);
            interactivePlayLoop(interactiveMap, hudEngine);
        } else if (firstKey == 'L') {
            Interactable interactiveMap = loadFromState();
            HUDEngine hudEngine = new HUDEngine(interactiveMap);
            interactivePlayLoop(interactiveMap, hudEngine);
        } else if (firstKey == 'Q') {
            System.exit(0);
        }
    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        // Fill out this method to run the game using the input passed in,
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().
        input = input.toUpperCase();
        TETile[][] finalWorldFrame = new TETile[1][1];
        Interactable interactiveMap;
        switch (input.charAt(0)) {
            case 'N':
                long seed = Long.parseLong(input.substring(1, input.indexOf
                        ('S')));
                String remainder = "";
                if (input.indexOf('S') + 1 < input.length()) {
                    remainder = input.substring(input.indexOf('S') + 1);
                }

                LayoutEngine mapGenerator = new LayoutEngine(seed, WIDTH, HEIGHT);
                TETile[][] map = mapGenerator.generateWorld();
                interactiveMap = new Interactable(map, mapGenerator);
                finalWorldFrame = playWithString(remainder, interactiveMap);
                break;
            case 'L':
                interactiveMap = loadFromState();
                finalWorldFrame = playWithString(input.substring(1), interactiveMap);
                break;
            case 'Q':
                return null;
            default:
                return null;
        }
//        ter.initialize(WIDTH, HEIGHT, 0, 0);
//        ter.renderFrame(finalWorldFrame);
        return finalWorldFrame;
    }

    public static void main(String[] args) {
        Game newGame = new Game();
        newGame.playWithKeyboard();
    }


    private static void saveState(Interactable interactiveMap) {
        File file = new File("./savestate.txt");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fs = new FileOutputStream(file);
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(interactiveMap);
            os.close();
        }  catch (FileNotFoundException e) {
            System.out.println("save state not found");
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(0);
        }
    }

    private static Interactable loadFromState() {
        File file = new File("./savestate.txt");
        if (file.exists()) {
            try {
                FileInputStream fs = new FileInputStream(file);
                ObjectInputStream os = new ObjectInputStream(fs);
                Interactable savedMap = (Interactable) os.readObject();
                os.close();
                return savedMap;
            } catch (FileNotFoundException e) {
                System.out.println("save state not found");
                System.exit(-1);
            } catch (IOException e) {
                System.out.println(e);
                System.exit(-2);
            } catch (ClassNotFoundException e) {
                System.out.println("class not found");
                System.exit(-3);
            }
        }
        System.exit(-69);
        return null;
    }

    public TETile[][] playWithString(String input, Interactable interactiveMap) {
        interactiveMap.updateOverlayMap();

        if (input.length() == 0) {
            return interactiveMap.overlayMap;
        }
        for (int i = 0; i < input.length(); i += 1) {
            char keypress = input.charAt(i);
            if (keypress == ':') {
                continue;
            } else if (keypress == 'Q') {
                saveState(interactiveMap);
                if (input.length() == i + 1) {
                    break;
                }
                return playWithInputString(input.substring(i + 1));
            }
            interactiveMap.movePlayer(keypress);
            interactiveMap.updateOverlayMap();
            if (interactiveMap.gameOver || interactiveMap.gameWon) {
                return interactiveMap.overlayMap;
            }
        }
        return interactiveMap.overlayMap;
    }

    public void interactivePlayLoop(Interactable interactiveMap, HUDEngine hudEngine) {
        interactiveMap.updateOverlayMap();
        while (true) {
            ter.renderFrame(interactiveMap.overlayMap);
            hudEngine.displayMouseInfo();
            hudEngine.displayStatus("Diglett");
//                StdDraw.pause(100);

            if (StdDraw.hasNextKeyTyped()) {
                char input = Character.toUpperCase(StdDraw.nextKeyTyped());
                if (input == ':') {
                    while (true) {
                        if (StdDraw.hasNextKeyTyped()) {
                            char waitingForQ = Character.toUpperCase(StdDraw.nextKeyTyped());
                            if (waitingForQ == 'Q') {
                                break;
                            }
                        }
                    }
                    saveState(interactiveMap);
                    System.exit(0);
                    break;
                }
                interactiveMap.movePlayer(input);
                if (interactiveMap.gameOver || interactiveMap.gameWon) {
                    break;
                }
                interactiveMap.updateOverlayMap();
            }
            StdDraw.clear(new Color(0, 0, 0));
        }
    }
}

