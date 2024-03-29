package it.unibo.bombardero;

import javax.swing.text.html.parser.Entity;

import it.unibo.bombardero.core.BombarderoEngine;
import it.unibo.bombardero.view.BombarderoWindow;

public class Bombardero {
    
    public static void main(String[] args) {
        BombarderoEngine engine = new BombarderoEngine();
        engine.initGame();
        engine.mainLoop();
    }
}
