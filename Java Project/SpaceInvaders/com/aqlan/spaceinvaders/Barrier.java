package com.aqlan.spaceinvaders;

import java.awt.*;

public class Barrier {
    /* Handles the drawing and managing of the three barriers that sit right above the player on the screen. Most of
     * the calls to Barrier objects are going to be through the bulletController when a bullet detects it has hit a barrier.
     */

    private PixelGrid grid;
    private int[][] state;
    private Position position;

    public Barrier(PixelGrid gr, Position pos) {
        grid = gr;
        position = pos;
        init();
    }

    private void init() {
        state = new int[][] {
                { 1, 1, 1, 1 },
                { 1, 1, 1, 1 },
                { 1, 1, 1, 1 },
                { 1, 1, 1, 1 } };
        draw();
    }

    private void draw() {
        for (int row = 0; row < state.length; row++) {
            for (int col = 0; col < state[row].length; col++) {
                if (state[row][col] == 1) {
                    Position temp = new Position(position.getRow() + row, position.getCol() + col);
                    if (grid.get(temp).equals(Color.BLACK)) {
                        grid.put(temp, Color.GREEN);
                    }
                }
            }
        }
    }

    public boolean containsPos(Position pos) {
        int row = pos.getRow();
        int col = pos.getCol();
        if ((position.getRow() <= row && position.getCol() <= col)) {
            if ((position.getRow() + state.length >= row) && (position.getCol() + state[0].length >= col)) {
                return true;
            }
        }
        return false;
    }

    //a bullet has hit this barrier, detected at Position pos, traveling direction
    public void hitBarrier(Position pos, String direction) {
        if (direction.equals("SOUTH")) {
            // make sure we are hitting the uppermost pos in the column that we can
            int col = pos.getCol();
            int row = position.getRow();
            while (grid.get(new Position(row, col)).equals(Color.BLACK)) {
                row++;
            }
            pos = new Position(row, col);
            grid.put(pos, Color.BLACK);
            grid.put(new Position(pos.getRow(), pos.getCol() - 1), Color.BLACK);
            grid.put(new Position(pos.getRow(), pos.getCol() + 1), Color.BLACK);
            grid.put(new Position(pos.getRow() + 1, pos.getCol()), Color.BLACK);
            grid.put(new Position(pos.getRow(), pos.getCol() - 2), Color.BLACK);
            grid.put(new Position(pos.getRow(), pos.getCol() + 2), Color.BLACK);
            grid.put(new Position(pos.getRow() + 1, pos.getCol() - 1), Color.BLACK);
            grid.put(new Position(pos.getRow() + 1, pos.getCol() + 1), Color.BLACK);
        } /*else {
            // make sure we are hitting the uppermost pos in the column that we can
            int col = pos.getCol();
            int row = position.getRow() + state.length;
            while (grid.get(new Position(row, col)).equals(Color.BLACK)) {
                row--;
            }
            pos = new Position(row, col);
            grid.put(pos, Color.BLACK);
            grid.put(new Position(pos.getRow(), pos.getCol() - 1), Color.BLACK);
            grid.put(new Position(pos.getRow(), pos.getCol() + 1), Color.BLACK);
            grid.put(new Position(pos.getRow() - 1, pos.getCol()), Color.BLACK);
            grid.put(new Position(pos.getRow(), pos.getCol() - 2), Color.BLACK);
            grid.put(new Position(pos.getRow(), pos.getCol() + 2), Color.BLACK);
            grid.put(new Position(pos.getRow() - 1, pos.getCol() - 1), Color.BLACK);
            grid.put(new Position(pos.getRow() - 1, pos.getCol() + 1), Color.BLACK);
        }*/
    }
}
