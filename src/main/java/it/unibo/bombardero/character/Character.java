package it.unibo.bombardero.character;

import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.map.api.Pair;

public abstract class Character {

    protected Pair coord;
    protected int width, height;
    protected boolean isAlive = true;
    protected GameManager manager;

    public Character(GameManager manager, Pair coord, int width, int height) {
        this.manager = manager;
        this.coord = coord;
        this.width = width;
        this.height = height;
    }

    public abstract void update();

    public boolean isAlive() {
        return isAlive;
    }

    public Pair getCoord() {
        return this.coord;
    }

    public void kill() {
        isAlive = false;
    }
    
}
