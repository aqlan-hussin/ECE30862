package com.aqlan.spaceinvaders;

import java.awt.*;
import java.util.ArrayList;

public class Invader {
    private PixelGrid grid; // we need the grid upon instantiation so we can manage pixels in the grid this class controls
    private String type; // the type of invader, ie, could be the player, or any of the possible enemy types
    private int[][] state1;
    private int[][] state2;
    private int curState;
    private Position pos; // top left position
    private boolean alive;
    private Position bulletLoc; // position of the bullet that killed this invader, kind of a kludge way to remove the invader

    public Invader(PixelGrid gr, String t) {
        grid = gr;
        type = t;
        alive = true;
        init();
    }

    /*
     * init sets up the pixels in the grid centered around a specified position
     * and the designated type of the Invader. init will populate the pixels
     * array and have each pixel add itself to the grid.
     */
    private void init() {
        if (type.equals("A")) {
            state1 = new int[][] {
                    { 1, 1, 1, 1, 1, 1, 1, 1 },
                    { 1, 1, 1, 1, 1, 1, 1, 1 },
                    { 1, 1, 1, 1, 1, 1, 1, 1 },
                    { 1, 1, 1, 1, 1, 1, 1, 1 },
                    { 1, 1, 1, 1, 1, 1, 1, 1 },
                    { 1, 1, 1, 1, 1, 1, 1, 1 },
                    { 1, 1, 1, 1, 1, 1, 1, 1 },
                    { 1, 1, 1, 1, 1, 1, 1, 1 } };

            state2 = new int[][] {
                    { 1, 1, 1, 1, 1, 1, 1, 1 },
                    { 1, 1, 1, 1, 1, 1, 1, 1 },
                    { 1, 1, 1, 1, 1, 1, 1, 1 },
                    { 1, 1, 1, 1, 1, 1, 1, 1 },
                    { 1, 1, 1, 1, 1, 1, 1, 1 },
                    { 1, 1, 1, 1, 1, 1, 1, 1 },
                    { 1, 1, 1, 1, 1, 1, 1, 1 },
                    { 1, 1, 1, 1, 1, 1, 1, 1 } };
        } else if (type.equals("B")) {
            state1 = new int[][] {
                    { 1, 1, 1, 1, 1, 1, 1, 1 },
                    { 1, 1, 1, 1, 1, 1, 1, 1 },
                    { 1, 1, 1, 1, 1, 1, 1, 1 },
                    { 1, 1, 1, 1, 1, 1, 1, 1 },
                    { 1, 1, 1, 1, 1, 1, 1, 1 },
                    { 1, 1, 1, 1, 1, 1, 1, 1 },
                    { 1, 1, 1, 1, 1, 1, 1, 1 },
                    { 1, 1, 1, 1, 1, 1, 1, 1 } };

            state2 = new int[][] {
                    { 1, 1, 1, 1, 1, 1, 1, 1 },
                    { 1, 1, 1, 1, 1, 1, 1, 1 },
                    { 1, 1, 1, 1, 1, 1, 1, 1 },
                    { 1, 1, 1, 1, 1, 1, 1, 1 },
                    { 1, 1, 1, 1, 1, 1, 1, 1 },
                    { 1, 1, 1, 1, 1, 1, 1, 1 },
                    { 1, 1, 1, 1, 1, 1, 1, 1 },
                    { 1, 1, 1, 1, 1, 1, 1, 1 } };
        } else if (type.equals("C")) {
            state1 = new int[][] {
                    { 1, 1, 1, 1, 1, 1, 1, 1 },
                    { 1, 1, 1, 1, 1, 1, 1, 1 },
                    { 1, 1, 1, 1, 1, 1, 1, 1 },
                    { 1, 1, 1, 1, 1, 1, 1, 1 },
                    { 1, 1, 1, 1, 1, 1, 1, 1 },
                    { 1, 1, 1, 1, 1, 1, 1, 1 },
                    { 1, 1, 1, 1, 1, 1, 1, 1 },
                    { 1, 1, 1, 1, 1, 1, 1, 1 } };
            state2 = new int[][] {
                    { 1, 1, 1, 1, 1, 1, 1, 1 },
                    { 1, 1, 1, 1, 1, 1, 1, 1 },
                    { 1, 1, 1, 1, 1, 1, 1, 1 },
                    { 1, 1, 1, 1, 1, 1, 1, 1 },
                    { 1, 1, 1, 1, 1, 1, 1, 1 },
                    { 1, 1, 1, 1, 1, 1, 1, 1 },
                    { 1, 1, 1, 1, 1, 1, 1, 1 },
                    { 1, 1, 1, 1, 1, 1, 1, 1 } };
        }
        curState = 1;
    }

    // where position is the position at the top left of the bounding grid of
    // the invader
    public synchronized void draw(Position position) {
        if (pos == null) {
            pos = position;
        }
        boolean destroyed = false;
        for (int i = 0; i < state1.length; i++) {
            for (int j = 0; j < state1[0].length; j++) {
                grid.put(new Position(pos.getRow() + i, pos.getCol() + j), Color.BLACK);
            }
        }
        pos = position;
        if (curState == 1) {
            for (int row = 0; row < state1.length; row++) {
                for (int col = 0; col < state1[row].length; col++) {
                    if (!destroyed && state1[row][col] == 1) {
                        Position temp = new Position(pos.getRow() + row, pos.getCol() + col);
                        if (grid.get(temp).equals(Color.BLACK)) {
                            grid.put(temp, Color.WHITE);
                        } else {
                            bulletLoc = temp;
                            destroy();
                            destroyed = true;
                        }
                    }
                }
            }
        } else {
            for (int row = 0; row < state2.length; row++) {
                for (int col = 0; col < state2[row].length; col++) {
                    if (!destroyed && state2[row][col] == 1) {
                        Position temp = new Position(pos.getRow() + row,pos.getCol() + col);
                        if (grid.get(temp).equals(Color.BLACK)) {
                            grid.put(temp, Color.WHITE);
                        } else {
                            bulletLoc = temp;
                            destroy();
                            destroyed = true;
                        }
                    }
                }
            }
        }
        if (curState == 1) {
            curState = 2;
        } else {
            curState = 1;
        }
    }

    /*
     * shift will move all the pixels that compose an invader in the direction
     * specified by the piv. pixels are moved by inc amount of bposks, ie, if
     * inc=1, each pixel will shift one cell in specified direction. Note that
     * this method controls all the action, as calls to shift will cause pixels
     * to move. This method will be called by a Horde object, which controls the
     * movement of a group of Invaders.
     */
    public synchronized void shift(String dir, int inc) {
        if (dir.equals("LEFT")) {
            draw(new Position(pos.getRow(), pos.getCol() - inc));
        } else if (dir.equals("RIGHT")) {
            draw(new Position(pos.getRow(), pos.getCol() + inc));
        } else if (dir.equals("DOWN")) {
            draw(new Position(pos.getRow() + inc, pos.getCol()));
        }
    }

    /*
     * returns an arraylist containing the positions of the pixels that this
     * Invader manages. Called by horde's hitInvader() to determine which
     * invader was hit by a bullet.
     */
    public synchronized ArrayList<Position> getPixelLocs() {
        ArrayList<Position> poss = new ArrayList<Position>();
        if (curState == 1) {
            for (int i = 0; i < state1.length; i++) {
                for (int j = 0; j < state1[i].length; j++) {
                    if (state1[i][j] == 1) {
                        poss.add(new Position(pos.getRow() + i, pos.getCol() + j));
                    }
                }
            }
        } else {
            for (int i = 0; i < state2.length; i++) {
                for (int j = 0; j < state2[i].length; j++) {
                    if (state2[i][j] == 1) {
                        poss.add(new Position(pos.getRow() + i, pos.getCol() + j));
                    }
                }
            }
        }
        return poss;
    }

    /*
     * destroys this invader. Goes through the pixel array, removing each from
     * the grid, before setting itself to null.
     */
    public synchronized void destroy() {
        for (int i = 0; i < state1.length; i++) {
            for (int j = 0; j < state1[i].length; j++) {
                grid.put(new Position(pos.getRow() + i, pos.getCol() + j), Color.BLACK);
            }
        }
        alive = false;
    }

    // upper left position
    public synchronized Position getPosition() {
        return pos;
    }

    public synchronized Position getRightPosition() {
        return new Position(pos.getRow(), pos.getCol() + state1[0].length);
    }

    // upper right position
    public synchronized int getLeftCol() {
        return pos.getCol();
    }

    public synchronized int getRightCol() {
        return pos.getCol() + state1[0].length;
    }

    public synchronized boolean isAlive() {
        return alive;
    }

    public synchronized int getTopRow() {
        return pos.getRow();
    }

    public synchronized int getBottomRow() {
        return pos.getRow() + state1.length;
    }

    public synchronized Position getBulletLoc() {
        return bulletLoc;
    }

    public String getType() {
        return type;
    }
}
