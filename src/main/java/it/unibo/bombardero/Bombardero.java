package it.unibo.bombardero;

import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;

import it.unibo.bombardero.view.BombarderoGraphics;

public class Bombardero {
    
    public static void main(String[] args) {
        /* Baga: debug lines so i can understand the window size when someone runs the game */
        System.out.println(
            "Max bounds: " + GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds()
        );
        System.out.println(
            "toolkit size: " + Toolkit.getDefaultToolkit().getScreenSize()
        );
        new BombarderoGraphics();
    }
}
