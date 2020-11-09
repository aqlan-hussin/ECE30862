package com.aqlan.spaceinvaders;

import java.awt.*;

public class Defender {
    private PixelGrid grid; // we need the grid upon instantiation so we can manage pixels in the grid this class controls
    private Position barrelLoc; // position for the barrel (1 row above it, really) so we know where bullet object should be spawned
    private int[][] state;
    private Position position;

    public Defender(PixelGrid gr) {
        grid = gr;
        state = new int[][] {
                { 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0 },
                { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
                { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
                { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
                { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
                { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 } };
    }

    /*
     * called when game detects a left or right arrow press. First check if
     * defender can move left/right, then shift all the pixels in the Pixel array
     * over if were not on the edge. If we are on the edge and cant move in the
     * desired direction, we ignore the keypress.
     */
    public void draw(Position pos) {
        position = pos;
        barrelLoc = new Position(pos.getRow() - 1, pos.getCol() + 7);
        for (int row = 0; row < state.length; row++) {
            for (int col = 0; col < state[row].length; col++) {
                if (state[row][col] == 1) {
                    Color color = new Color(0, 255, 0);
                    grid.put(new Position(position.getRow() + row, position.getCol() + col), color);
                }
            }
        }
    }

    public void shift(String dir, int inc) {
        for (int row = 0; row < state.length; row++) {
            for (int col = 0; col < state[row].length; col++) {
                grid.put(new Position(position.getRow() + row, position.getCol()+ col), Color.BLACK);
            }
        }
        if (dir.equals("LEFT")) {
            Position newLoc = new Position(position.getRow(), position.getCol() - inc);
            if (newLoc.getCol() >= 3) {
                draw(newLoc);
            } else {
                draw(position);
            }
        } else if (dir.equals("RIGHT")) {
            Position newLoc = new Position(position.getRow(), position.getCol() + inc);
            if (newLoc.getCol() + 18 <= grid.getNumCols()) {
                draw(newLoc);
            } else {
                draw(position);
            }
        }
    }

    public Position getBarrelLoc() {
        return barrelLoc;
    }

    public void destroy() {
        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state[i].length; j++) {
                grid.put(new Position(position.getRow() + i, position.getCol()+ j), Color.BLACK);
            }
        }
    }
}
