package com.aqlan.spaceinvaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class GameProperties {
    private static GameProperties instance = new GameProperties();
    private static final String FILENAME = "GameProperties.txt";
    private int invadersRows = 2;
    private int invadersColumns = 16;
    private int rateLeftRight = 10;
    private int rateDown = 5;
    private int rateFire = 2;
    private int lives = 3;

    private GameProperties () {
        readPropertyFile();
    }

    public static GameProperties getInstance( ) {
        return instance;
    }

    private void readPropertyFile() {
        try(BufferedReader br = new BufferedReader(new FileReader(FILENAME))) {
            String sCurrentLine;

            while ((sCurrentLine = br.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(sCurrentLine," ");
                String currStr = st.nextToken();
                if("INVADERS".equals(currStr)) {
                    invadersRows = Integer.parseInt(st.nextToken());
                    invadersColumns = Integer.parseInt(st.nextToken());
                }
                if("RATE_LEFT_RIGHT".equals(currStr)) {
                    rateLeftRight = Integer.parseInt(st.nextToken());
                }
                if("RATE_DOWN".equals(currStr)) {
                    rateDown = Integer.parseInt(st.nextToken());
                }
                if("RATE_FIRE".equals(currStr)) {
                    rateFire = Integer.parseInt(st.nextToken());
                }
                if("LIVES".equals(currStr)) {
                    lives = Integer.parseInt(st.nextToken());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getInvadersRows() {
        return invadersRows;
    }

    public int getInvadersColumns() {
        return invadersColumns;
    }

    public int getRateLeftRight() {
        return rateLeftRight;
    }

    public int getRateDown() {
        return rateDown;
    }

    public int getRateFire() {
        return rateFire;
    }

    public int getLives() {
        return lives;
    }
}
