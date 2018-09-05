package byog.Core;

import java.io.Serializable;
import java.util.ArrayList;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class Interactable implements Serializable {
    TETile[][] originalMap;
    TETile[][] overlayMap;
    Player player;
    LayoutEngine mapGenerator;
    ArrayList<TETile> movableTiles;
    ArrayList<TETile> wallTiles;
    ArrayList<TETile> gemTiles;
    Position bossPosition;
    int bossRequiredXP;
    boolean bossSlain;
    ArrayList<Gem> gems;
    ArrayList<Position> usedPositions;
    boolean gameOver = false;
    boolean gameWon = false;

    int numGems = 15;
    int maxGemXP = 1500 + 500;
    int startingEnergy = (Game.HEIGHT + Game.WIDTH) * 3;
    int circleRadius = 7;

    private static final long serialVersionUID = 87924350923485720L;

    public Interactable(TETile[][] map, LayoutEngine mapGenerator) {
        this.mapGenerator = mapGenerator;
        originalMap = map;
        movableTiles = new ArrayList();
        gemTiles = new ArrayList();
        wallTiles = new ArrayList();
        movableTiles.add(Tileset.FLOOR);
        wallTiles.add(Tileset.WALL);
        gemTiles.add(Tileset.GEM1);
        gemTiles.add(Tileset.GEM2);
        gemTiles.add(Tileset.GEM3);

        usedPositions = new ArrayList();
        generateBossLocation();

        while (true) {
            Position initplayerPos = mapGenerator.generateWalkablePosition(movableTiles);
            if (!used(initplayerPos)) {
                player = new Player(initplayerPos.getX(), initplayerPos.getY(), this);
                break;
            }
        }

        gems = generateGems(numGems);

        bossRequiredXP = gemSumValue() * 3 / 4;
        bossSlain = false;
    }

    public TETile[][] getMapCopy() {
        TETile[][] copy = new TETile[originalMap.length][originalMap[0].length];
        for (int i = 0; i < copy.length; i += 1) {
            for (int j = 0; j < copy[0].length; j += 1) {
                copy[i][j] = originalMap[i][j];

            }
        }
        return copy;
    }

    public int gemSumValue() {
        int sum = 0;
        for (Gem g : gems) {
            sum += g.xPvalue;
        }
        return sum;
    }

    //Generate n random gems
    public ArrayList<Gem> generateGems(int n) {
        ArrayList<Gem> gemList = new ArrayList();
        for (int i = 0; i < n; i += 1) {
            while (true) {
                Position newPos = mapGenerator.generateWalkablePosition(movableTiles);
                if (!used(newPos)) {
                    int xp = mapGenerator.random.nextInt(maxGemXP / 10) * 10 + 500;
                    TETile colour = gemTiles.get(mapGenerator.random.nextInt(gemTiles.size()));
                    gemList.add(new Gem(newPos.getX(), newPos.getY(), xp, colour));
                    usedPositions.add(newPos);
                    break;
                }
            }
        }
        return gemList;
    }

    private boolean used(Position pos) {
        for (Position p : usedPositions) {
            if (p.equals(pos)) {
                return true;
            }
        }
        return false;
    }

    private void generateBossLocation() {
        while (true) {
            int index = mapGenerator.random.nextInt(mapGenerator.rooms.size());
            Room randRoom = mapGenerator.rooms.get(index);
            if (randRoom.getWIDTH() > 5 && randRoom.getLENGTH() > 5) {
                Position centerPos = new Position(randRoom.getX() + randRoom.getWIDTH() / 2,
                        randRoom.getY() + randRoom.getLENGTH() / 2);
                if (!used(centerPos)) {
                    bossPosition = centerPos;
                    for (int i = -2; i < 3; i += 1) {
                        for (int j = -2; j < 3; j += 1) {
                            usedPositions.add(new Position(centerPos.getX() + i,
                                    centerPos.getY() + j));
                        }
                    }
                    break;
                }
            }
        }
    }

    public void movePlayer(char input) {
        player.move(input);
    }

    public void placePlayer(TETile[][] map) {
        map[player.playerPosX][player.playerPosY] = Tileset.PLAYER;
    }

    public void placeBoss(TETile[][] map) {
        map[bossPosition.getX()][bossPosition.getY()] = Tileset.BOSS;
    }

    public void placeDoor(TETile[][] map) {
        map[bossPosition.getX()][bossPosition.getY()] = Tileset.DOOR;
    }

    public void placeBossIndicator(TETile[][] map) {
        if (bossSlain) {
            return;
        }
        TETile tile;
        if (player.status.XP >= bossRequiredXP) {
            tile = Tileset.GREENINDICATOR;
        } else {
            tile = Tileset.REDINDICATOR;
        }
        int x = bossPosition.getX();
        int y = bossPosition.getY();
        for (int i = -2; i < 3; i += 1) {
            for (int j = -2; j < 3; j += 1) {
                if (movableTiles.contains(map[x + i][y + j])) {
                    map[x + i][y + j] = tile;
                }
            }
        }
    }


    public TETile[][] getCircleMapCopy(TETile[][] map) {
        TETile[][] copy = new TETile[originalMap.length][originalMap[0].length];

        for (int i = 0; i < originalMap.length; i += 1) {
            for (int j = 0; j < originalMap[0].length; j += 1) {
                copy[i][j] = Tileset.DARKNESS;
            }
        }

        int startingI = (player.playerPosX - circleRadius) > 0 ? (player.playerPosX - circleRadius)
                : 0;
        int endingI = (player.playerPosX + circleRadius) <= originalMap.length ? (player.playerPosX
                + circleRadius) : originalMap.length;

        int startingJ = (player.playerPosY - circleRadius) > 0 ? (player.playerPosY - circleRadius)
                : 0;
        int endingJ = (player.playerPosY + circleRadius) <= originalMap[0].length ? (player
                .playerPosY
                + circleRadius) : originalMap[0].length;

        try {
            for (int i = startingI; i < endingI; i += 1) {
                for (int j = startingJ; j < endingJ; j += 1) {
                    if ((Math.pow((i - player.playerPosX), 2)
                            + Math.pow((j - player.playerPosY), 2))
                            <= circleRadius * circleRadius) {
                        copy[i][j] = map[i][j];
                    }
                }
            }
        } catch (IndexOutOfBoundsException e) {
            assert true;
        }

        return copy;
    }

    //returns null if no gem at location, gem instance otherwise
    public Gem getGem(int x, int y) {
        for (Gem g : gems) {
            if (g.xPos == x && g.yPos == y) {
                return g;
            }
        }
        return null;
    }

    public void placeGems(TETile[][] map) {
        for (Gem g : gems) {
            map[g.xPos][g.yPos] = g.colourVariant;
        }
    }

    public void processGemTile(Gem gem) {
        player.status.XP += gem.xPvalue;
        gems.remove(gem);
    }

    public void processTile() {
        Gem currGem = getGem(player.playerPosX, player.playerPosY);
        if (currGem != null) {
            processGemTile(currGem);
        }
        if (player.playerPosX <= bossPosition.getX() + 1
                && player.playerPosX >= bossPosition.getX() - 1
                && player.playerPosY <= bossPosition.getY() + 1
                && player.playerPosY >= bossPosition.getY() - 1) {
            bossSlain = true;
        }
    }

    //use for every step
    public void updateOverlayMap() {
        TETile[][] map = getMapCopy();
        placePlayer(map);
        placeGems(map);
        placeBossIndicator(map);
        if (bossSlain) {
            placeDoor(map);
        } else {
            placeBoss(map);
        }
        map = getCircleMapCopy(map);
        overlayMap = map;
    }

}
