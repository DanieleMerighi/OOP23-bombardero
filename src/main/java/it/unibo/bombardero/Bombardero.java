package it.unibo.bombardero;

import it.unibo.bombardero.core.BombarderoEngine;
/**
 * The main class of the Bombardero game.
 */
public final class Bombardero {

    private Bombardero() {

    }

    /** 
     * The main method of the main class of the game.
     * @param args the command line arguments
     */
    public static void main(final String[] args) {
        new BombarderoEngine();
    }
}
