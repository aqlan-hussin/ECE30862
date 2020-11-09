package com.aqlan.spaceinvaders;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.util.ArrayList;

public class Invaders {
    private Invader[][] invaders; // array of Invader objects that are in the Invaders
    private PixelGrid grid; // grid that contains the Invaders
    private int direction; // direction that the invaders is currently shifting. (180 = left, 0 = right)
    private int score;
    private Clip killSound;

    private boolean gameOver;
    private InvadersController invadersController;
    private BulletController bulletController;
    private GameProperties gp = GameProperties.getInstance();

    public Invaders(InvadersController controller, BulletController bullets) {
        invadersController = controller;
        bulletController = bullets;
        invaders = new Invader[gp.getInvadersRows()][gp.getInvadersColumns()]; // assume size is 11 for now, standard
        grid = controller.getGrid();
        direction = 180;
        score = 0;
        gameOver = false;

        try {
            File file = new File("sound/shoot.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(file);
            killSound = AudioSystem.getClip();
            killSound.open(audioIn);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }

        init(); // other constructor operations, moved so we dont bloat the code here

    }

    /*
     * fills the grid with invader objects.
     */
    private void init() {
        for (int row = 0; row < invaders.length; row++) {
            for (int col = 0; col < invaders[row].length; col++) {
                if (row == 0) {
                    int colSpace = grid.getNumCols() - 121;
                    int invaderGap = 5;
                    int wallGap = (colSpace - (invaderGap * 10)) / 2;
                    invaders[row][col] = new Invader(grid, "A");
                    invaders[row][col].draw(new Position(3, wallGap + (invaderGap + 11) * col));
                } else if (row == 1) {
                    int colSpace = grid.getNumCols() - 121;
                    int invaderGap = 5;
                    int wallGap = (colSpace - (invaderGap * 10)) / 2;
                    invaders[row][col] = new Invader(grid, "B");
                    invaders[row][col].draw(new Position(16, wallGap + (invaderGap + 11) * col));
                } else if (row == 2) {
                    int colSpace = grid.getNumCols() - 121;
                    int invaderGap = 5;
                    int wallGap = (colSpace - (invaderGap * 10)) / 2;
                    invaders[row][col] = new Invader(grid, "B");
                    invaders[row][col].draw(new Position(29, wallGap + (invaderGap + 11) * col));
                } else if (row == invaders.length-1) {
                    int colSpace = grid.getNumCols() - 121;
                    int invaderGap = 5;
                    int wallGap = (colSpace - (invaderGap * 10)) / 2;
                    invaders[row][col] = new Invader(grid, "C");
                    invaders[row][col].draw(new Position(row*13+3, wallGap + (invaderGap + 11) * col));
                } else {
                    int colSpace = grid.getNumCols() - 121;
                    int invaderGap = 5;
                    int wallGap = (colSpace - (invaderGap * 10)) / 2;
                    invaders[row][col] = new Invader(grid, "B");
                    invaders[row][col].draw(new Position(row*13+3, wallGap + (invaderGap + 11) * col));
                }

            }
        }
    }

    /*
     * Called when a bullet shot by the player detects a collision in the cell
     * it is moving into. The bullet will call this method with the position in
     * which it detected the collision. hitInvader will take this position, and
     * loop through the invaders array calling pixelLocs() on each to check
     * which invader contains a pixel that occupies the position the collision
     * was detected in. This invader is now "destroyed", so it will be removed
     * from both the array and the grid.
     */
    public void hitInvader(Position pos) {
        for (int row = 0; row < invaders.length; row++) {
            for (int col = 0; col < invaders[row].length; col++) {
                if (invaders[row][col] != null) {
                    Invader temp = invaders[row][col];
                    if (pos.getRow() >= temp.getTopRow() && pos.getRow() <= temp.getBottomRow()) {
                        if (pos.getCol() >= temp.getLeftCol() && pos.getCol() <= temp.getRightCol()) {
                            if (temp.getType().equals("A")) {
                                addToScore(1);
                            } else if (temp.getType().equals("B")) {
                                addToScore(1);
                            } else if (temp.getType().equals("C")) {
                                addToScore(1);
                            }
                            killSound.setFramePosition(0);
                            killSound.start();
                            temp.destroy(); // tell invader to remove from the grid all the pixels it manages
                            invaders[row][col] = null; // remove this invader from the list
                            return;
                        }
                    }
                }
            }
        }

    }

    /*
     * Move will loop through invaders list and call shift on each invader with
     * the current direction to move all the invaders. after everyone moves, we
     * call updateDir to check if the invaders is now on an edge and as such, our
     * direction should be changed
     *
     * Right now move works by removing each invader from the grid, and then
     * redrawing it in it's new position. I'm unsure as to whether there is a
     * better way to implement this process.
     */
    public int move() {
        int invadersLeft = 0; // what this method returns, 1 if there are still invaders left, 0 otherwise. So invaderscontroller knows when to instantiate a new one
        if (direction == 180) {
            Position edge = getLeftEdge();
            // reached the left edge, should move down now
            if (edge.getCol() < 5) {
                for (int i = gp.getInvadersRows()-1; i >= 0; i--) { // move the invaders the farthest down first to make room for others
                    for (int j = gp.getInvadersColumns()-1; j >= 0; j--) {
                        if (invaders[i][j] != null) {
                            Invader temp = invaders[i][j];
                            temp.draw(new Position(temp.getPosition().getRow() + 16, temp.getPosition().getCol()));
                            if (!temp.isAlive()) {
                                bulletController.getBulletContaining(temp.getBulletLoc()).destroy();
                                if (temp.getType().equals("A")) {
                                    addToScore(1);
                                } else if (temp.getType().equals("B")) {
                                    addToScore(1);
                                } else if (temp.getType().equals("C")) {
                                    addToScore(1);
                                }
                                temp = null;
                                invaders[i][j] = null;
                            } else {
                                invadersLeft = 1;
                            }
                        }
                    }
                }
                direction = 0;
            } else {
                for (int i = 0; i < invaders.length; i++) {
                    for (int j = 0; j < invaders[i].length; j++) {
                        if (invaders[i][j] != null) {
                            Invader temp = invaders[i][j];
                            temp.draw(new Position(temp.getPosition().getRow(), temp.getPosition().getCol() - 1));
                            if (!temp.isAlive()) {
                                bulletController.getBulletContaining(temp.getBulletLoc()).destroy();
                                if (temp.getType().equals("A")) {
                                    addToScore(1);
                                } else if (temp.getType().equals("B")) {
                                    addToScore(1);
                                } else if (temp.getType().equals("C")) {
                                    addToScore(1);
                                }
                                temp = null;
                                invaders[i][j] = null;
                            } else {
                                invadersLeft = 1;
                            }
                        }
                    }
                }
            }
        } else if (direction == 0) {
            /* check to make sure we are not on an edge */
            Position edge = getRightEdge();
            if (edge.getCol() >= grid.getNumCols() - 5) {
                for (int i = gp.getInvadersRows()-1; i >= 0; i--) {
                    for (int j = gp.getInvadersColumns()-1; j >= 0; j--) {
                        if (invaders[i][j] != null) {
                            Invader temp = invaders[i][j];
                            temp.draw(new Position(temp.getPosition().getRow() + 16, temp.getPosition().getCol()));
                            if (!temp.isAlive()) {
                                bulletController.getBulletContaining(temp.getBulletLoc()).destroy();
                                if (temp.getType().equals("A")) {
                                    addToScore(1);
                                } else if (temp.getType().equals("B")) {
                                    addToScore(1);
                                } else if (temp.getType().equals("C")) {
                                    addToScore(1);
                                }
                                invaders[i][j] = null;
                                temp = null;
                            } else {
                                invadersLeft = 1;
                            }
                        }
                    }
                }
                direction = 180;
            } else {
                for (int i = 0; i < invaders.length; i++) {
                    for (int j = 0; j < invaders[i].length; j++) {
                        if (invaders[i][j] != null) {
                            Invader temp = invaders[i][j];
                            temp.draw(new Position(temp.getPosition().getRow(), temp.getPosition().getCol() + 1));
                            if (!temp.isAlive()) {
                                bulletController.getBulletContaining(temp.getBulletLoc()).destroy();
                                if (temp.getType().equals("A")) {
                                    addToScore(1);
                                } else if (temp.getType().equals("B")) {
                                    addToScore(1);
                                } else if (temp.getType().equals("C")) {
                                    addToScore(1);
                                }
                                invaders[i][j] = null;
                                temp = null;
                            } else
                                invadersLeft = 1;
                        }
                    }
                }
            }
        }
        return invadersLeft;
    }

    public ArrayList<Bullet> shoot() {
        int[] columnHead = new int[gp.getInvadersColumns()]; // the row number of the invader at the head of the column, -1 if doesn't exist
        for (int i = 0; i < gp.getInvadersColumns(); i++) {
            columnHead[i] = 4;
        }
        for (int col = 0; col < gp.getInvadersColumns(); col++) {
            for (int row = gp.getInvadersRows()-1; row >= 0; row--) {
                if (invaders[row][col] != null) {
                    columnHead[col] = row;
                    break;
                }
            }
            if (columnHead[col] == 4) {
                columnHead[col] = -1;
            }
        }
        ArrayList<Bullet> bullets = new ArrayList<Bullet>();
        for (int i = 0; i < gp.getInvadersColumns(); i++) {
            double chance = columnHead[i] / 30.0;
            double roll = Math.random();
            if (roll <= chance) {
                Invader head = invaders[columnHead[i]][i];
                Position bulletLoc = new Position(0, 0);
                if (head.getType().equals("A")) {
                    bulletLoc = new Position(head.getPosition().getRow() + 8, head.getPosition().getCol() + 5);
                } else if (head.getType().equals("B")) {
                    bulletLoc = new Position(head.getPosition().getRow() + 8, head.getPosition().getCol() + 5);
                } else if (head.getType().equals("C")) {
                    bulletLoc = new Position(head.getPosition().getRow() + 8, head.getPosition().getCol() + 5);
                }
                Bullet bullet = new Bullet(bulletLoc, grid, "SOUTH", 6, 6, false);
                bullets.add(bullet);
            }
        }
        return bullets;

    }

    /*
     * loops through the invaders array, looking for the first non null invader
     * reference that is the farthest left. This is used to determine when the
     * invaders should move down instead of left for the next call to move.
     */
    public Position getLeftEdge() {
        for (int col = 0; col < gp.getInvadersColumns(); col++) {
            for (int row = 0; row < gp.getInvadersRows(); row++) {
                if (invaders[row][col] != null) {
                    return invaders[row][col].getPosition();
                }
            }
        }
        //if we get down here, the game is over, player has won
        invadersController.invadersReportsGameOver();
        return null;
    }

    /*
     * loops through the invaders array, looking for the first non null invader
     * reference that is the right most column. This is used for determining
     * when the invaders has reached the right edge of the screen and should move
     * down instead of right for the next call to move.
     */
    public Position getRightEdge() {
        for (int col = gp.getInvadersColumns()-1; col >= 0; col--) {
            for (int row = 0; row < gp.getInvadersRows(); row++) {
                if (invaders[row][col] != null) {
                    return invaders[row][col].getRightPosition();
                }
            }
        }
        //if we get down here, the game is over, player has won
        invadersController.invadersReportsGameOver();
        return null;
    }

    public Invader[][] getInvaders() {
        return invaders;
    }

    public void destroy() {
        for (int i = 0; i < invaders.length; i++) {
            for (int j = 0; j < invaders[0].length; j++) {
                if (invaders[i][j] != null) {
                    invaders[i][j].destroy();
                }
            }
        }
    }

    /*
     * since invaders is the only object that can tell which invaders have been
     * destroyed, we keep score here and return the value for the invaderscontroller
     * thread to keep track of it. returns the points earned since method was
     * last called.
     */
    public int getScore() {
        int temp = score;
        score = 0;
        return temp;
    }

    public void addToScore(int add) {
        score += add;
    }

    public void gameOver() {
        gameOver = true;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public PixelGrid getGrid() {
        return grid;
    }
}
