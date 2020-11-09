package com.aqlan.spaceinvaders;

import java.lang.reflect.InvocationTargetException;
import javax.swing.SwingUtilities;

public class Game {
    public static void main(String[] args) throws InterruptedException, InvocationTargetException {
        SwingUtilities.invokeAndWait(new Runnable() {
            Canvas display;
            @Override
            public void run() {
                GameProperties prop = GameProperties.getInstance();
                display = new Canvas(225, 225);
                display.startGame();
            }
        });
    }
}