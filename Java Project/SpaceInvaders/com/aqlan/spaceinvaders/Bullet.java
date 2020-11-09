package com.aqlan.spaceinvaders;

import java.awt.*;

public class Bullet {
    private PixelGrid grid;
    private Position position; // position that the head of the bullet currently occupies
    private String direction;  // direction of bullet, either NORTH or SOUTH, (90 = north, 270 = south)
    private Position[] poss;   // index 3 is leading pixel
    private int length;
    private int speed;
    private boolean live;
    private boolean defenderBullet;
    private boolean isGreen;

    public Bullet(Position pos, PixelGrid gr, String dir, int ln, int sp, boolean green) {
        grid = gr;
        poss = new Position[ln];
        length = ln;
        position = pos;
        direction = dir;
        speed = sp;
        isGreen = green;
        if (dir.equals("NORTH")) {
            for (int i = 0; i < length; i++) {
                poss[i] = new Position(position.getRow() - i, position.getCol());
                if (!isGreen) {
                    grid.put(poss[i], Color.WHITE);
                } else {
                    grid.put(poss[i], Color.GREEN);
                }
            }
        }
        if (dir.equals("SOUTH")) {
            for (int i = 0; i < length; i++) {
                poss[i] = new Position(position.getRow() + i, position.getCol());
                if (!isGreen) {
                    grid.put(poss[i], Color.WHITE);
                } else {
                    grid.put(poss[i], Color.GREEN);
                }
            }
        }
        position = poss[poss.length - 1];
        live = true;
        defenderBullet = false;
    }

    /*
     * Moves the bullet if possible. If bullet hits an invader, we return the
     * position of the collision to the bullet manager so it can call the
     * hitInvader() method. This avoids having to pass a horde reference to each
     * bullet instance. If we could shift without issue or if the bullet shifts
     * off the screen, return a null position. This is probably a shit
     * programming practice but it's a viable solution at this point.
     */
    public synchronized Position shift() {
        if (!grid.isValid(poss[poss.length - 1]))
            live = false;

        for (int i = 0; i < length; i++) {
            grid.put(poss[i], Color.BLACK);
        }
        Position[] newPositions = new Position[length];
        boolean canMove = true;
        Position collisionLoc = null;
        if (direction.equals("NORTH")) {
            for (int i = 0; canMove && i < length; i++) {
                newPositions[i] = new Position(poss[i].getRow() - speed, poss[i].getCol());
                if (!grid.isValid(newPositions[i])) {
                    canMove = false;
                } else if (!grid.get(newPositions[i]).equals(Color.BLACK)) {
                    canMove = false;
                    collisionLoc = newPositions[i];
                }
            }
        } else if (direction.equals("SOUTH")) {
            for (int i = 0; canMove && i < length; i++) {
                newPositions[i] = new Position(poss[i].getRow() + speed, poss[i].getCol());
                if (!grid.isValid(newPositions[i])) {
                    canMove = false;
                } else if (!grid.get(newPositions[i]).equals(Color.BLACK)) {
                    canMove = false;
                    collisionLoc = newPositions[i];
                }
            }
        }
        if (canMove) {
            for (int i = 0; i < length; i++) {
                if (isGreen) {
                    grid.put(newPositions[i], Color.GREEN);
                } else {
                    grid.put(newPositions[i], Color.WHITE);
                }
            }
            poss = newPositions;
            position = poss[poss.length - 1];
            return null;
        } else {
            live = false;
            return collisionLoc;
        }
    }

    public boolean isLive() {
        return live;
    }

    public Position getPosition() {
        return position;
    }

    public void destroy() {
        live = false;
        for (int i = 0; i < length; i++) {
            grid.put(poss[i], Color.BLACK);
        }
    }

    public String getDirection() {
        return direction;
    }

    public Position[] getLocs() {
        return poss;
    }

    public void setDefenderBullet() {
        defenderBullet = true;
    }

    public boolean isDefenderBullet() {
        return defenderBullet;
    }

    public boolean contains(Position pos) {
        for (int i = 0; i < length; i++) {
            if (poss[i].getRow() == pos.getRow() && poss[i].getCol() == pos.getCol()) {
                return true;
            }
        }
        return false;
    }

    public void setGreen() {
        isGreen = true;
    }
}
