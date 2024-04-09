package it.unibo.bombardero.character;

import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.map.api.Pair;

public abstract class Character {

    protected float x;
    protected float y;
    protected int width, height;
    protected boolean isAlive = true;
    protected GameManager manager;

    public Character(GameManager manager, float x, float y, int width, int height) {
        this.manager = manager;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public abstract void update();

    public boolean isAlive() {
        return isAlive;
    }

    public Pair getCoord() {
        return new Pair(Math.round(this.x), Math.round(this.y));
    }

    public void kill() {
        isAlive = false;
    }
    
}
