package com.aqlan.spaceinvaders;

public class Position {
    private int row; // row location in grid
    private int col; // column location in grid

    public Position(int r, int c) {
        row = r;
        col = c;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Position))
            return false;

        Position otherLoc = (Position) other;
        return getRow() == otherLoc.getRow() && getCol() == otherLoc.getCol();
    }
}
