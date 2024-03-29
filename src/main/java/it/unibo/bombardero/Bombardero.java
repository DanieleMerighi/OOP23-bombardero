package it.unibo.bombardero;

import it.unibo.bombardero.core.BombarderoEngine;

public class Bombardero {
    
    public static void main(String[] args) {
        BombarderoEngine engine = new BombarderoEngine();
        engine.initGame();
        engine.mainLoop();
    }
}
