package it.unibo.bombardero.map.impl;

import java.util.List;
import java.util.ArrayList;

import it.unibo.bombardero.character.Enemy;
import it.unibo.bombardero.map.api.Map;

/* TODO:
 * Matrice delle celle
 * Lista dei enemy
 * Reference to player
 * generazione ostacoli
 * chiusura arena dopo un certo tempo
 * Timer(?)
 */

public class MapImpl implements Map {

    private final List<Enemy> enemies = new ArrayList<>(); /* Using an arrayList to hold the reference for the four AI enemies */
    // private final Map< ,Cell  = new HashMap<>();> 
    // private PlayerPosition, o qualcosa di simile

    @Override
    public void addBomb() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addBomb'");
    }

    @Override
    public void addEnemy() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addEnemy'");
    }

    @Override
    public void addPlayer() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addPlayer'");
    }

    @Override
    public void removeEnemy() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeEnemy'");
    }

    @Override
    public boolean isPlayer() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isPlayer'");
    }

    @Override
    public boolean isEnemy() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isEnemy'");
    }

    @Override
    public boolean isBomb() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isBomb'");
    }

    @Override
    public boolean isBreakableWall() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isBreakableWall'");
    }

    @Override
    public boolean isUnbreakableWall() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isUnbreakableWall'");
    }

    @Override
    public boolean isExplosion() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isExplosion'");
    }
    
}
