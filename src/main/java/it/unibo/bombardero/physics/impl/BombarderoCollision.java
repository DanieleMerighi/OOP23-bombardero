package it.unibo.bombardero.physics.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import it.unibo.bombardero.cell.Cell;
import it.unibo.bombardero.cell.powerup.api.PowerUp;
import it.unibo.bombardero.character.Direction;
import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.map.api.GameMap;
import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.physics.api.CollisionEngine;
import it.unibo.bombardero.physics.api.CollisionHandler;
import it.unibo.bombardero.character.Character;

public class BombarderoCollision implements CollisionEngine{
    private final CollisionHandler cHandler = new CollisionHandlerImpl();
    private final Map<Direction, Line2D.Float> MAP_OUTLINES = Map.of(
        Direction.UP , new Line2D.Float(new Point2D.Float(0, 0) , new Point2D.Float(13, 0)),
        Direction.DOWN , new Line2D.Float(new Point2D.Float(0, 13) , new Point2D.Float(13, 13)),
        Direction.LEFT , new Line2D.Float(new Point2D.Float(0, 0) , new Point2D.Float(0, 13)),
        Direction.RIGHT , new Line2D.Float(new Point2D.Float(13, 0) , new Point2D.Float(13, 13))
    );

    @Override
    public void checkFlameAndPowerUpCollision(final Character character, final GameManager mgr) {
        final GameMap gMap = mgr.getGameMap();
        if(gMap.isFlame(character.getIntCoordinate())) {
            cHandler.applyFlameCollision(character);
        } else if(gMap.isPowerUp(character.getIntCoordinate())) {
            cHandler.applyPowerUpCollision(character, (PowerUp)gMap.getMap().get(character.getIntCoordinate()));
            mgr.removePowerUp(character.getIntCoordinate());
        }
    }

    @Override
    public void checkCharacterCollision(final Character character, final GameManager mgr) {
        final GameMap gMap = mgr.getGameMap();
        final Map<Pair, Cell> map = gMap.getMap();
        final List<Pair> cells = getDirectionCell(character.getFacingDirection() , character.getIntCoordinate());
        if(!cells.isEmpty()) {
            final Optional<Cell> collidingCell= cells.stream()
                .filter(p -> map.containsKey(p))
                .map( p -> map.get(p))
                .filter(c-> c.getBoundingBox().isColliding(character.getBoundingBox()) && c.getBoundingCollision())
                .findFirst();
            cHandler.applyCharacterBoundaryCollision(character, collidingCell);
        } else {
            cHandler.applyCharacterBoundaryCollision(character, MAP_OUTLINES.get(character.getFacingDirection()));
        }
    }

    private List<Pair> getDirectionCell(final Direction dir , final Pair cPos) {
        final List<Pair> l = dir.getDiagonals(dir,cPos);
        final List<Pair> l2 = new LinkedList <> (l);
        if(cPos.sum(dir.getPair()).x() >= 0 && cPos.sum(dir.getPair()).y() >= 0 && cPos.sum(dir.getPair()).x() < 13 && cPos.sum(dir.getPair()).y() < 13) {
            l2.add(cPos.sum(dir.getPair()));
        } 
        return l2;
    }
}