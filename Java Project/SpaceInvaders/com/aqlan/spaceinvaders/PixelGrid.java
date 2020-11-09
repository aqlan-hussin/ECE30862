package com.aqlan.spaceinvaders;

import java.awt.*;

public class PixelGrid {
    private Color[][] pixels; // the array storing the grid elements

    public PixelGrid(int rows, int cols) {
        if (rows <= 0)
            throw new IllegalArgumentException("rows <= 0");
        if (cols <= 0)
            throw new IllegalArgumentException("cols <= 0");
        pixels = new Color[rows][cols];
    }


    public int getNumRows() {
        return pixels.length;
    }

    public int getNumCols() {
        // Note: according to the constructor precondition, numRows() > 0, so
        // theGrid[0] is non-null.
        return pixels[0].length;
    }

    public boolean isValid(Position pos) {
        return 0 <= pos.getRow() && pos.getRow() < getNumRows()
                && 0 <= pos.getCol() && pos.getCol() < getNumCols();
    }

    public Color get(Position pos) {
        if (!isValid(pos)) {
            throw new IllegalArgumentException("Position " + pos + " is not valid");
        }
        return pixels[pos.getRow()][pos.getCol()];
    }

    public void put(Position pos, Color col) {
        pixels[pos.getRow()][pos.getCol()] = col;
    }

    public void remove(Position pos) {
        if (!isValid(pos)) {
            throw new IllegalArgumentException("Position " + pos + " is not valid");
        }
        pixels[pos.getRow()][pos.getCol()] = null;
    }
}
