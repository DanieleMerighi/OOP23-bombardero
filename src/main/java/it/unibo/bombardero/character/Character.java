package it.unibo.bombardero.character;

import it.unibo.bombardero.map.api.Coord;

public abstract class Character {

    protected Coord coord;
    protected int width, height;
    protected boolean isAlive = true;

    public Character(Coord coord, int width, int height) {
        this.coord = coord;
        this.width = width;
        this.height = height;
    }

    protected abstract void update();

    public boolean isAlive() {
        return isAlive;
    }

    public void kill() {
        isAlive = false;
    }
    
}
