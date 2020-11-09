package com.aqlan.spaceinvaders;

public class DrawString {
    public static void draw(PixelGrid grid, Position position, String string) {
        int buffer = 0;
        for (int i = 0; i < string.length(); i++) {
            String substring = string.substring(i, i + 1);
            AlphaNumeric alphaNumeric = new AlphaNumeric(substring, grid);
            if (alphaNumeric.getHeight() == 8) {
                alphaNumeric.draw(new Position(position.getRow() - 3, position.getCol() + (i + 1) + buffer));
            } else {
                alphaNumeric.draw(new Position(position.getRow(), position.getCol() + (i + 1) + buffer));
            }
            //	alphaNumerics[i] = alphaNumeric;
            buffer += alphaNumeric.getWidth();
        }
    }
}
