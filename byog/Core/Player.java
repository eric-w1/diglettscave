package byog.Core;

import byog.TileEngine.TETile;

import java.io.Serializable;

public class Player implements Serializable {
    int playerPosX;
    int playerPosY;
    Interactable interactable;
    Status status;

    private static final long serialVersionUID = 34095034959030L;

    public Player(int x, int y, Interactable interactable) {
        playerPosX = x;
        playerPosY = y;
        this.interactable = interactable;
        status = new Status(0, interactable.startingEnergy);
    }

    //takes in only capital WASD
    public void move(char input) {
        int nextX = 0;
        int nextY = 0;

        switch (input) {
            case 'W':
                nextX = playerPosX;
                nextY = playerPosY + 1;
                break;
            case 'A':
                nextX = playerPosX - 1;
                nextY = playerPosY;
                break;
            case 'S':
                nextX = playerPosX;
                nextY = playerPosY - 1;
                break;
            case 'D':
                nextX = playerPosX + 1;
                nextY = playerPosY;
                break;
            default:
                break;
        }

        try {
            TETile nextTile = interactable.originalMap[nextX][nextY];
            if (interactable.movableTiles.contains(nextTile)) {
                playerPosX = nextX;
                playerPosY = nextY;
                interactable.processTile();
                status.energy -= 1;
                if (status.energy <= 0) {
                    interactable.gameOver = true;
                    StartGUI.drawLossScreen("out of energy");
                }
                int x = interactable.bossPosition.getX();
                int y = interactable.bossPosition.getY();
                if (status.XP < interactable.bossRequiredXP && playerPosX <= x + 2
                        && playerPosX >= x - 2 && playerPosY <= y + 2 && playerPosY >= y - 2) {
                    interactable.gameOver = true;
                    StartGUI.drawLossScreen("you have been slain");
                }
                if (playerPosX == x && playerPosY == y) {
                    interactable.gameWon = true;
                    StartGUI.drawWinScreen();
                }
            }
        } catch (IndexOutOfBoundsException e) {
            assert true;
        }
    }

}
