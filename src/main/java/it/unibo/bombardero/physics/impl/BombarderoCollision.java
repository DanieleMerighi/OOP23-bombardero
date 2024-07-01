package it.unibo.bombardero.physics.impl;

import java.util.ArrayList;
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
    private static final int MIN_NUM_CELL = 0;
    private static final int MAX_NUM_CELL = 12;
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
        final List<Pair> cells = getDirectionCell(character);
        if(!cells.isEmpty()) {
            final Optional<Cell> collidingCell= cells.stream()
                .filter(p -> map.containsKey(p))
                .map(p -> map.get(p))
                .filter(c -> c.haveBoundingCollision() && c.getBoundingBox().get().isColliding(character.getBoundingBox()))
                .findFirst();
            cHandler.applyCharacterBoundaryCollision(character, collidingCell);
        } else {
            cHandler.applyCharacterBoundaryCollision(character, MAP_OUTLINES.get(character.getFacingDirection()));
        }
    }

    private List<Pair> getDirectionCell(final Character character) {
        final Direction dir = character.getFacingDirection();
        final List<Pair> l = new ArrayList<>();
        l.add(character.getIntCoordinate().sum(dir.getPair()));
        if (dir.equals(Direction.RIGHT) || dir.equals(Direction.LEFT)) {
            l.add(new Pair(character.getIntCoordinate().x(),
                getAdjacent(character.getCharacterPosition().y(), character.getIntCoordinate().y())).sum(dir.getPair()));
        } else {
            l.add(new Pair(getAdjacent(character.getCharacterPosition().x(), character.getIntCoordinate().x()),
                character.getIntCoordinate().y()).sum(dir.getPair()));
        }
        l.removeIf(p -> isOutOfBound(p));
        return l;
    }

    private int getAdjacent(final float n1, final int n2) {
        return n1 - n2 > 0.5 ? n2 + 1 : n2 - 1;
    }

    private boolean isOutOfBound(final Pair p) {
        return p.x() < MIN_NUM_CELL || p.y() < MIN_NUM_CELL || p.x() > MAX_NUM_CELL || p.y() > MAX_NUM_CELL;
    }
}