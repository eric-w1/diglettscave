package byog.Core;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Random;
import java.util.ArrayList;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class LayoutEngine implements Serializable {
    private long seed;
    final Random random;
    private int width;
    private int length;
    private TETile[][] grid;
    final ArrayList<Room> rooms;
    private HashSet<Room> connectedRooms;

    private static final long serialVersionUID = 723845873945980L;


    //finetuning:
    private int roomGenStopCond = 10;
    private int hallGenStopCond = 15;
    private int minRoomSize = 5;
    private TETile flooring = Tileset.FLOOR;


    public LayoutEngine(long seed, int w, int l) {
        this.seed = seed;
        random = new Random(seed);
        width = w;
        length = l;
        grid = new TETile[w][l];
        rooms = new ArrayList();
        connectedRooms = new HashSet();
    }

    public void placeRoom(Room room, TETile floortile) { //(x,y) is bottom left corner
        for (int i = 0; i < room.getWIDTH(); i += 1) {
            for (int j = 0; j < room.getLENGTH(); j += 1) {
                grid[room.getX() + i][room.getY() + j] = floortile;
            }
        }
        rooms.add(room);
    }

    public void placeHall(Hall hall, TETile floortile) { //(x,y) is starting end
        for (int i = 0; i < hall.getLENGTH(); i += 1) {
            int o = hall.getORIENTATION();
            if (o == 0) {
                grid[hall.getX()][hall.getY() + i] = floortile;
            } else if (o == 1) {
                grid[hall.getX() + i][hall.getY()] = floortile;
            } else if (o == 2) {
                grid[hall.getX()][hall.getY() - i] = floortile;
            } else if (o == 3) {
                grid[hall.getX() - i][hall.getY()] = floortile;
            }
        }
    }

    public HashSet<Room> checkHall(Hall hall) {
        HashSet<Room> seekedRooms = new HashSet<>();
        Room prev = null;
        Room curr = null;
        for (int i = 0; i < hall.getLENGTH() + 1; i += 1) {
            int o = hall.getORIENTATION();
            if (o == 0) {
                curr = getRoom(hall.getX(), hall.getY() + i);
            } else if (o == 1) {
                curr = getRoom(hall.getX() + i, hall.getY());
            } else if (o == 2) {
                curr = getRoom(hall.getX(), hall.getY() - i);
            } else if (o == 3) {
                curr = getRoom(hall.getX() - i, hall.getY());
            }
            if (curr != null && curr != prev) {
                prev = curr;
                seekedRooms.add(curr);
            }
        }
        return seekedRooms;
    }

    //Returns room with position p, else null if no such room exists at the location.
    public Room getRoom(Position p) {
        for (Room r : rooms) {
            if (r.getX() <= p.getX() && p.getX() < r.getX() + r.getWIDTH() && r.getY() <= p.getY
                    () && p.getY() < r.getY() + r.getLENGTH()) {
                return r;
            }
        }
        return null;
    }

    public Room getRoom(int x, int y) {
        return getRoom(new Position(x, y));
    }

    //Generates room with random width, length, xy pos, and exit positions
    public Room genRandomRoom() {
        int w = random.nextInt(5) + minRoomSize;
        int l = random.nextInt(6) + minRoomSize;
        int x = random.nextInt(width) + 1;
        int y = random.nextInt(length) + 1;

        Position exit0 = new Position(x + random.nextInt(w), y + l);
        Position exit1 = new Position(x + w, y + random.nextInt(l));
        Position exit2 = new Position(x + random.nextInt(w), y);
        Position exit3 = new Position(x, y + random.nextInt(l));
        Position[] exits = new Position[] {exit0, exit1, exit2, exit3};

        return new Room(w, l, x, y, exits);
    }

    public void populateRooms() {
        int consecutiveRoomFails = 0;
        while (consecutiveRoomFails < roomGenStopCond) {
            Room newRoom = genRandomRoom();
            if (roomSafe(newRoom)) {
                placeRoom(newRoom, flooring);
                consecutiveRoomFails = 0;
            } else {
                consecutiveRoomFails += 1;
            }
        }
    }

    public boolean edgeSafe(Hall h) {
        switch (h.getORIENTATION()) {
            case 0: return h.getY() + h.getLENGTH() < length - 1;
            case 1: return h.getX() + h.getLENGTH() < width - 1;
            case 2: return h.endpoint().getY() > 0;
            case 3: return h.endpoint().getX() > 0;
            default: return false;
        }
    }

    public boolean edgeSafe(Room r) {
        boolean lCorner = r.getX() > 0 && r.getY() > 0;
        boolean rCorner = r.getX() + 1 + r.getWIDTH() < width - 1
                && r.getY() + 1 + r.getLENGTH() < length - 1;
        return lCorner && rCorner;
    }

    //checks if there are already blocks in a room's location and if it is outside of map boundaries
    public boolean roomSafe(Room newRoom) {
        if (!edgeSafe(newRoom)) {
            return false;
        } else {
            for (int i = 0; i < newRoom.getWIDTH(); i += 1) {
                for (int j = 0; j < newRoom.getLENGTH(); j += 1) {
                    if (grid[newRoom.getX() + i][newRoom.getY() + j] != Tileset.NOTHING) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean checkWall(int x, int y) {
        if (grid[x][y] == flooring) {
            return false;
        }
        for (int i = -1; i < 2; i += 1) {
            for (int j = -1; j < 2; j += 1) {
                if (i == 0 && j == 0) {
                    continue;
                }
                try {
                    if (grid[x + i][y + j] == flooring) {
                        return true;
                    }
                } catch (IndexOutOfBoundsException e) {
                    continue;
                }
            }
        }
        return false;
    }

    public void fillWalls() {
        for (int i = 0; i < width; i += 1) {
            for (int j = 0; j < length; j += 1) {
                if (checkWall(i, j)) {
                    grid[i][j] = Tileset.WALL;
                }
            }
        }
    }

    public boolean connectedSet(HashSet<Room> hs) {
        for (Room r : hs) {
            if (connectedRooms.contains(r)) {
                return true;
            }
        }
        return false;
    }

    //Extends the hallway from first connected Room until a connected room is reached
    public void generateHall(Room room) {
        int numExits = random.nextInt(2) + 1;
        ArrayList<Integer> completed = new ArrayList<>(numExits);
        for (int i = 0; i < numExits; i += 1) {
            int consecutiveAttempts = 0;
            boolean complete = false;
            while (!complete && consecutiveAttempts < hallGenStopCond) {
                int exitNum = random.nextInt(4);
                int len = random.nextInt(7) + 4;
                Position exit = room.getEXITS()[exitNum];
                Hall newHall = new Hall(len, exitNum, exit.getX(), exit.getY());
                if (!completed.contains(exitNum) && edgeSafe(newHall)) {
                    placeHall(newHall, flooring);
                    connectedRooms.addAll(checkHall(newHall));
                    complete = true;
                    completed.add(exitNum);
                    HashSet<Room> seekedRooms =
                            extendHall(newHall.endpoint(), newHall.getORIENTATION());
                    connectedRooms.addAll(seekedRooms);
                }
                consecutiveAttempts += 1;
            }
        }
    }

    public HashSet<Room> extendHall(Position endpoint, int oldOrientation) {
        HashSet<Room> seekedRooms = new HashSet();
        int newOrientation;
        while (true) {
            newOrientation = random.nextInt(4);
            while (newOrientation == oldOrientation) {
                newOrientation = random.nextInt(4);
            }
            int len = random.nextInt(7) + 4;
            Hall newHall = new Hall(len, newOrientation, endpoint.getX(), endpoint.getY());
            if (edgeSafe(newHall)) {
                placeHall(newHall, flooring);
                seekedRooms.addAll(checkHall(newHall));

                if (seekedRooms.size() > rooms.size() / 2) {
                    break;
                }
                endpoint = newHall.endpoint();
                oldOrientation = newHall.getORIENTATION();
            }
        }
        return seekedRooms;
    }

    //seek connected room from unconnected room - returns empty hs if connected, HashSet if not
    // (rooms to be connected)
    public HashSet<Room> seekRoom(Room room) {
        HashSet<Room> seekedRooms = new HashSet();
        seekedRooms.add(room);
        while (true) {
            int exitNum = random.nextInt(4);
            int len = random.nextInt(7) + 4;
            Position exit = room.getEXITS()[exitNum];
            Hall newHall = new Hall(len, exitNum, exit.getX(), exit.getY());
            if (edgeSafe(newHall)) {
                placeHall(newHall, flooring);
                seekedRooms.addAll(checkHall(newHall));
                if (connectedSet(seekedRooms)) {
                    break;
                }
                extendHallSingle(newHall.endpoint(), newHall.getORIENTATION(), seekedRooms);
                break;
            }
        }

        if (connectedSet(seekedRooms)) {
            connectedRooms.addAll(seekedRooms);
            return new HashSet();
        }
        return seekedRooms;
    }

    public void extendHallSingle(Position endpoint, int oldOrientation, HashSet<Room>
            seekedRooms) {
        int newOrientation;
        while (!connectedSet(seekedRooms)) { //previously (true)
            newOrientation = random.nextInt(4);
            while (newOrientation == oldOrientation) {
                newOrientation = random.nextInt(4);
            }
            int len = random.nextInt(7) + 4;
            Hall newHall = new Hall(len, newOrientation, endpoint.getX(), endpoint.getY());
            if (edgeSafe(newHall)) {
                placeHall(newHall, flooring);

                seekedRooms.addAll(checkHall(newHall));
                endpoint = newHall.endpoint();
                oldOrientation = newHall.getORIENTATION();
            }
        }
    }

    public void genSetsNotConnected(HashSet<HashSet<Room>> toBeConnected) {
        for (Room r : rooms) {
            if (!connectedRooms.contains(r)) {
                HashSet<Room> seekedRooms = seekRoom(r);
                if (seekedRooms.size() > 0) {
                    toBeConnected.add(seekedRooms);
                }
            }
        }
    }

    public void populateHalls() {
        HashSet<HashSet<Room>> toBeConnected = new HashSet();
        int roomNum = random.nextInt(rooms.size());
        Room firstRoom = rooms.get(roomNum);
        connectedRooms.add(firstRoom);
        generateHall(firstRoom);
        genSetsNotConnected(toBeConnected);
    }

    public Position generateWalkablePosition(ArrayList<TETile> movableTiles) {
        while (true) {
            int x = random.nextInt(width);
            int y = random.nextInt(length);
            if (movableTiles.contains(grid[x][y])) {
                return new Position(x, y);
            }
        }
    }

    public TETile[][] generateWorld() {
        for (int i = 0; i < width; i += 1) {
            for (int j = 0; j < length; j += 1) {
                grid[i][j] = Tileset.NOTHING;
            }
        }
        populateRooms();
        populateHalls();
        fillWalls();
        return grid;
    }
}
