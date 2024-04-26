package it.unibo.bombardero;

import it.unibo.bombardero.view.BombarderoGraphics;

import java.awt.GraphicsEnvironment;

public class Bombardero {
    
    public static void main(String[] args) {
        System.out.println(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds());
        new BombarderoGraphics();
    }
}
