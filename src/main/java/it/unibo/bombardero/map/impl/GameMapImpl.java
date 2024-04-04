package it.unibo.bombardero.map.impl;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import it.unibo.bombardero.character.Enemy;
import it.unibo.bombardero.character.Player;
import it.unibo.bombardero.cell.Bomb;
import it.unibo.bombardero.cell.BreakableWall;
import it.unibo.bombardero.cell.Cell;
import it.unibo.bombardero.cell.Flame;
import it.unibo.bombardero.cell.UnbreakableWall;
import it.unibo.bombardero.map.api.GameMap;
import it.unibo.bombardero.map.api.Pair;

/* TODO:
 * Fare la mappa statica
 * Matrice delle celle
 * Lista dei enemy
 * Reference to player
 * generazione ostacoli
 * chiusura arena dopo un certo tempo
 * Timer(?)
 */

public class GameMapImpl implements GameMap {

    private final Map<Pair,Cell> map = new HashMap<>(); /* Using an HashMap to hold the information about the map's tiles */
    private final MapManagerImpl manager;
    private final static int mapWidth = 13; /* the maps width and height expressed in tiles */
    private final static int mapHeight = 17;

    public GameMapImpl() {
        this.manager = new MapManagerImpl(this);
        manager.placeUnbreakableWalls();
        manager.generateBreakableWalls();
    }

    @Override
    public void update() {
        this.manager.update();
    }

    @Override
    public void addBomb(final Bomb bomb, final Pair coordinate) {
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
    public void addFlame() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addEnemy'");
    }

    @Override
    public void addUnbreakableWall(Pair coord, Cell wall) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addUnbreakableWall'");
    }

    @Override
    public void addBreakableWall() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addBreakableWall'");
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
    public boolean isBomb(Pair coordinate) {
        return this.map.containsKey(coordinate) && this.map.get(coordinate) instanceof Bomb;
    }

    @Override
    public boolean isBreakableWall(Pair coordinate) {
        return this.map.containsKey(coordinate) && this.map.get(coordinate) instanceof BreakableWall;
    }

    @Override
    public boolean isUnbreakableWall(Pair coordinate) {
        return this.map.containsKey(coordinate) && this.map.get(coordinate) instanceof UnbreakableWall;
    }

    @Override
    public boolean isFlame(Pair coordinate) {
        return this.map.containsKey(coordinate) && this.map.get(coordinate) instanceof Flame;
    }

    @Override
    public boolean isEmpty(Pair coordinate) {
        return this.map.containsKey(coordinate);
    }
    
}
