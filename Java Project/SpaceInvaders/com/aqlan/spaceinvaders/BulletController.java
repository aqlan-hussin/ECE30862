package com.aqlan.spaceinvaders;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BulletController  extends SwingWorker<Void, PixelGrid> {
    private Canvas display;
    private Invaders invaders;
    private PixelGrid grid;
    private Clip killSound;
    private ArrayList<Bullet> bullets;
    private boolean playerBulletLive;
    private boolean gameOver;

    private GameProperties gp = GameProperties.getInstance();

    public BulletController(Canvas dis) {
        playerBulletLive = false;
        bullets = new ArrayList<Bullet>();
        gameOver = false;
        display = dis;

        try {
            File file = new File("sound/explosion.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(file);
            killSound = AudioSystem.getClip();
            killSound.open(audioIn);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }

        grid = display.getGrid();
    }

    @Override
    protected Void doInBackground() {
        try {
            int msElapsed = 0;
            while (!gameOver) {
                // move bullets in their respective directions every time through the loop
                for (int i = 0; i < bullets.size(); i++) {
                    if (bullets.get(i).isLive()) {
                        Position pos = bullets.get(i).shift();
                        if (pos != null) {
                            // check to see if bullet hit a barrier
                            Barrier[] barriers;
                            barriers = display.getBarriers();
                            boolean handled = false;
                            for (int j = 0; j < 48; j++) {
                                if (barriers[j].containsPos(pos)) {
                                    barriers[j].hitBarrier(pos, bullets.get(i).getDirection());
                                    handled = true;
                                }
                            }
                            if (!handled
                                    && bullets.get(i).getDirection()
                                    .equals("SOUTH")
                                    && bullets.get(i).getPosition().getRow() >= 205) {
                                // if the position wasnt a barrier, a bullet hit the player, game is now over
                                killSound.setFramePosition(0);
                                killSound.start();
                                display.gameIsOver(false);
                                gameOver = true;
                            } else {
                                invaders.hitInvader(pos); // if shift actually returned a position, its the pos of a collision
                            }
                        }
                    }
                    if (!bullets.get(i).isLive()) {
                        // check to see if the bullet is no longer live, remove it if so
                        if (bullets.get(i).isDefenderBullet()) {
                            playerBulletLive = false;
                        }
                        bullets.remove(i);
                    }
                }
                if (msElapsed % 1000 == 0) {
                    bullets.addAll(invaders.shoot());
                }
                msElapsed += gp.getRateFire();
                setProgress(100);
                publish(grid);
                Thread.sleep(50);
            }
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
        return null;
    }

    @Override
    protected void process(List<PixelGrid> list) {
        display.repaint();
    }

    public void addBullet(Bullet bullet) {
        if (bullet.isDefenderBullet()) {
            playerBulletLive = true;
        }
        bullets.add(bullet);
    }

    public boolean isDefenderBulletLive() {
        return playerBulletLive;
    }

    public PixelGrid getGrid() {
        return grid;
    }

    public Bullet getBulletContaining(Position pos) {
        for (int i = 0; i < bullets.size(); i++) {
            if (bullets.get(i).contains(pos)) {
                if (bullets.get(i).isDefenderBullet()) {
                    playerBulletLive = false;
                }
                return bullets.get(i);
            }
        }
        return null;
    }

    public void setInvaders(Invaders hr) {
        invaders = hr;
    }
    public void gameIsOver() {
        gameOver = true;
    }
}
