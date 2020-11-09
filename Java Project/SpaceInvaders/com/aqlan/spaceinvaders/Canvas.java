package com.aqlan.spaceinvaders;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class Canvas extends JComponent implements KeyListener, MouseListener, ActionListener {
    private static final long serialVersionUID = -7269678075128926434L;
    private PixelGrid grid;
    private JFrame frame;
    private int lastKeyPressed;
    private Position lastPositionClicked;

    private boolean gameOver;
    private Timer timer;
    private Clip shootSound;

    private InvadersController invadersController;
    private BulletController bulletController;

    private Barrier[] barriers;
    private Defender defender;
    private boolean defenderWin;

    private GameProperties gp = GameProperties.getInstance();
    private int lives = gp.getLives();

    private int numRows = 225;
    private int numCols = 225;

    private int carryScore = 0;

    // create display with each cell of given dimensions
    public Canvas(int numRows, int numCols) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.grid = new PixelGrid(numRows, numCols);
        lastKeyPressed = -1;
        lastPositionClicked = null;

        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                grid.put(new Position(row, col), Color.BLACK);
            }
        }

        frame = new JFrame();
        frame.setTitle("Space Invaders");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addKeyListener(this);

        // change the constant that gets divided to change the size of the frame
        // that gets displayed. This will also change
        // how much space each cell takes up.
        int cellWidth = 900 / numCols;
        int cellHeight = 900 / numRows;
        int cellSize = Math.min(cellWidth, cellHeight);
        setPreferredSize(new Dimension(numCols * cellSize, numRows * cellSize));
        addMouseListener(this);
        frame.getContentPane().add(this);
        frame.pack();
    }

    private void reInitGame() {
        this.grid = new PixelGrid(225, 225);
        lastKeyPressed = -1;
        lastPositionClicked = null;

        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                grid.put(new Position(row, col), Color.BLACK);
            }
        }

        // change the constant that gets divided to change the size of the frame
        // that gets displayed. This will also change
        // how much space each cell takes up.
        int cellWidth = 900 / numCols;
        int cellHeight = 900 / numRows;
        int cellSize = Math.min(cellWidth, cellHeight);
        setPreferredSize(new Dimension(numCols * cellSize, numRows * cellSize));

        startGame();
    }

    public void startGame() {
        barriers = new Barrier[48];

        barriers[0] = new Barrier(grid, new Position(170, 42));
        barriers[1] = new Barrier(grid, new Position(170, 47));
        barriers[2] = new Barrier(grid, new Position(170, 52));
        barriers[3] = new Barrier(grid, new Position(170, 57));
        barriers[4] = new Barrier(grid, new Position(170, 103));
        barriers[5] = new Barrier(grid, new Position(170, 108));
        barriers[6] = new Barrier(grid, new Position(170, 113));
        barriers[7] = new Barrier(grid, new Position(170, 118));
        barriers[8] = new Barrier(grid, new Position(170, 164));
        barriers[9] = new Barrier(grid, new Position(170, 169));
        barriers[10] = new Barrier(grid, new Position(170, 174));
        barriers[11] = new Barrier(grid, new Position(170, 179));
        barriers[12] = new Barrier(grid, new Position(175, 42));
        barriers[13] = new Barrier(grid, new Position(175, 47));
        barriers[14] = new Barrier(grid, new Position(175, 52));
        barriers[15] = new Barrier(grid, new Position(175, 57));
        barriers[16] = new Barrier(grid, new Position(175, 103));
        barriers[17] = new Barrier(grid, new Position(175, 108));
        barriers[18] = new Barrier(grid, new Position(175, 113));
        barriers[19] = new Barrier(grid, new Position(175, 118));
        barriers[20] = new Barrier(grid, new Position(175, 164));
        barriers[21] = new Barrier(grid, new Position(175, 169));
        barriers[22] = new Barrier(grid, new Position(175, 174));
        barriers[23] = new Barrier(grid, new Position(175, 179));
        barriers[24] = new Barrier(grid, new Position(180, 42));
        barriers[25] = new Barrier(grid, new Position(180, 47));
        barriers[26] = new Barrier(grid, new Position(180, 52));
        barriers[27] = new Barrier(grid, new Position(180, 57));
        barriers[28] = new Barrier(grid, new Position(180, 103));
        barriers[29] = new Barrier(grid, new Position(180, 108));
        barriers[30] = new Barrier(grid, new Position(180, 113));
        barriers[31] = new Barrier(grid, new Position(180, 118));
        barriers[32] = new Barrier(grid, new Position(180, 164));
        barriers[33] = new Barrier(grid, new Position(180, 169));
        barriers[34] = new Barrier(grid, new Position(180, 174));
        barriers[35] = new Barrier(grid, new Position(180, 179));
        barriers[36] = new Barrier(grid, new Position(185, 42));
        barriers[37] = new Barrier(grid, new Position(185, 47));
        barriers[38] = new Barrier(grid, new Position(185, 52));
        barriers[39] = new Barrier(grid, new Position(185, 57));
        barriers[40] = new Barrier(grid, new Position(185, 103));
        barriers[41] = new Barrier(grid, new Position(185, 108));
        barriers[42] = new Barrier(grid, new Position(185, 113));
        barriers[43] = new Barrier(grid, new Position(185, 118));
        barriers[44] = new Barrier(grid, new Position(185, 164));
        barriers[45] = new Barrier(grid, new Position(185, 169));
        barriers[46] = new Barrier(grid, new Position(185, 174));
        barriers[47] = new Barrier(grid, new Position(185, 179));

        bulletController = new BulletController(this);
        invadersController = new InvadersController(this, bulletController, carryScore);
        bulletController.setInvaders(invadersController.getInvaders());

        bulletController.execute();
        invadersController.execute();

        defender = new Defender(grid);
        defender.draw(new Position(210, 100));

        // set up sound for defender shots
        try {
            File file = new File("sound/invaderkilled.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(file);
            shootSound = AudioSystem.getClip();
            shootSound.open(audioIn);
        } catch (Exception e) {
            System.out.println("Sound file not found: invaderkilled.wav");
            System.exit(1);
        }
        gameOver = false;
        timer = new Timer(20, this);
        timer.start();
        frame.setVisible(true);
    }

    @Override
    public void paintComponent(Graphics g) {
        int numRows = grid.getNumRows();
        int numCols = grid.getNumCols();
        int cellSize = getCellSize();

        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                Position pos = new Position(row, col);
                g.setColor(grid.get(pos));
                int x = col * cellSize;
                int y = row * cellSize;
                g.fillRect(x, y, cellSize, cellSize);
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_RIGHT) {
            defender.shift("RIGHT", 2);
        }
        if (key == KeyEvent.VK_LEFT) {
            defender.shift("LEFT", 2);
        }
        if (key == KeyEvent.VK_SPACE) {
            if (!bulletController.isDefenderBulletLive()) {
                Position barrelLoc = defender.getBarrelLoc();
                Bullet temp = new Bullet(barrelLoc, grid, "NORTH", 6, 9, true);
                temp.setDefenderBullet();
                bulletController.addBullet(temp);
                shootSound.setFramePosition(0);
                shootSound.start();
            }
        }
        if (key == KeyEvent.VK_S) {
            if(lives > 0)
                reInitGame();
            else
                endGame();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            repaint();
            timer.restart();
        } else if(gameOver  && lives > 0) {
            restartGame();
        } else {
            endGame();
        }
    }

    public void endGame() {
        this.grid = new PixelGrid(125, 125);
        for (int i = 0; i < grid.getNumRows(); i++) {
            for (int j = 0; j < grid.getNumCols(); j++) {
                grid.put(new Position(i, j), Color.BLACK);
            }
        }
        int cellWidth = 900 / 125;
        int cellHeight = 900 / 125;
        int cellSize = Math.min(cellWidth, cellHeight);
        setPreferredSize(new Dimension(125 * cellSize, 125 * cellSize));
        if (!defenderWin) {
            DrawString.draw(grid, new Position(20, 25), "SORRY GAME OVER");
            DrawString.draw(grid, new Position(35, 25), "SCORE: " + String.valueOf(invadersController.getScore()));
        } else {
            DrawString.draw(grid, new Position(20, 5), "GOOD JOB DEFENDER OF EARTH");
            DrawString.draw(grid, new Position(35, 5), "YOU WIN");
        }
        repaint();

    }

    public void restartGame() {
        gameOver = false;
        lives--;
        carryScore = invadersController.getScore();
        invadersController.updateTitle();
        this.grid = new PixelGrid(125, 125);
        for (int i = 0; i < grid.getNumRows(); i++) {
            for (int j = 0; j < grid.getNumCols(); j++) {
                grid.put(new Position(i, j), Color.BLACK);
            }
        }
        int cellWidth = 900 / 125;
        int cellHeight = 900 / 125;
        int cellSize = Math.min(cellWidth, cellHeight);
        setPreferredSize(new Dimension(125 * cellSize, 125 * cellSize));

        //DrawString.draw(grid, new Position(20, 25), "SORRY GAME OVER");
        DrawString.draw(grid, new Position(25, 25), "SCORE: " + String.valueOf(invadersController.getScore()));
        DrawString.draw(grid, new Position(35, 25), "LIVES LEFT: " + String.valueOf(lives));

        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // ignore
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // ignore
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int cellSize = getCellSize();
        int row = e.getY() / cellSize;
        if (row < 0 || row >= grid.getNumRows()) {
            return;		//ignore
        }
        int col = e.getX() / cellSize;
        if (col < 0 || col >= grid.getNumCols()) {
            return;		//ignore
        }
        lastPositionClicked = new Position(row, col);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // ignore
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // ignore
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // ignore
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // ignore
    }

    public PixelGrid getGrid() {
        return grid;
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public void setTitle(String title) {
        frame.setTitle(title);
    }

    // returns -1 if no key pressed since last call.
    // otherwise returns the code for the last key pressed.
    public int checkLastKeyPressed() {
        int key = lastKeyPressed;
        lastKeyPressed = -1;
        return key;
    }

    // returns null if no position clicked since last call.
    public Position checkLastPositionClicked() {
        Position pos = lastPositionClicked;
        lastPositionClicked = null;
        return pos;
    }

    private int getCellSize() {
        int cellWidth = getWidth() / grid.getNumCols();
        int cellHeight = getHeight() / grid.getNumRows();
        return Math.min(cellWidth, cellHeight);
    }

    public Barrier[] getBarriers() {
        return barriers;
    }

    public void gameIsOver(boolean win) {
        defenderWin = win;
        invadersController.gameIsOver();
        bulletController.gameIsOver();
        gameOver = true;
    }

    public int getLives() {
        return lives;
    }
}
