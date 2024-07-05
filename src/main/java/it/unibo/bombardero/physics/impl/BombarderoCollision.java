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
import it.unibo.bombardero.map.api.Functions;
import it.unibo.bombardero.map.api.GameMap;
import it.unibo.bombardero.map.api.GenPair;
import it.unibo.bombardero.physics.api.CollisionEngine;
import it.unibo.bombardero.physics.api.CollisionHandler;
import it.unibo.bombardero.character.Character;

/**
 * Detect every type of collision between characters and cells
 */
public final class BombarderoCollision implements CollisionEngine {
    private static final int MIN_NUM_CELL = 0;
    private static final int MAX_NUM_CELL = 12;
    private final CollisionHandler cHandler = new CollisionHandlerImpl();
    private final Map<Direction, Line2D.Float> mapOutlines = Map.of(
            Direction.UP, new Line2D.Float(new Point2D.Float(0, 0), new Point2D.Float(13, 0)),
            Direction.DOWN, new Line2D.Float(new Point2D.Float(0, 13), new Point2D.Float(13, 13)),
            Direction.LEFT, new Line2D.Float(new Point2D.Float(0, 0), new Point2D.Float(0, 13)),
            Direction.RIGHT, new Line2D.Float(new Point2D.Float(13, 0), new Point2D.Float(13, 13)));

    @Override
    public void checkFlameAndPowerUpCollision(final Character character, final GameMap gMap) {
        if (gMap.isFlame(character.getIntCoordinate())) {
            cHandler.applyFlameCollision(character);
        } else if (gMap.isPowerUp(character.getIntCoordinate())) {
            cHandler.applyPowerUpCollision(character, (PowerUp) gMap.getMap().get(character.getIntCoordinate()));
            gMap.removePowerUp(character.getIntCoordinate());
        }
    }

    @Override
    public void checkCharacterCollision(final Character character, final GameMap gMap) {
        final Map<GenPair<Integer, Integer>, Cell> map = gMap.getMap();
        final List<GenPair<Integer, Integer>> cells = getDirectionCell(character);
        if (!cells.isEmpty()) {
            final Optional<Cell> collidingCell = cells.stream()
                    .filter(p -> map.containsKey(p))
                    .map(p -> map.get(p))
                    .filter(c -> c.haveBoundingCollision()
                            && c.getBoundingBox().get().isColliding(character.getBoundingBox()))
                    .findFirst();
            cHandler.applyCharacterBoundaryCollision(character, collidingCell);
        } else {
            cHandler.applyCharacterBoundaryCollision(character, mapOutlines.get(character.getFacingDirection()));
        }
    }

    private List<GenPair<Integer, Integer>> getDirectionCell(final Character character) {
        final Direction dir = character.getFacingDirection();
        final List<GenPair<Integer, Integer>> l = new ArrayList<>();
        l.add(character.getIntCoordinate().apply(Functions.sumInt(dir.getPair())));
        if (dir.equals(Direction.RIGHT) || dir.equals(Direction.LEFT)) {
            l.add(new GenPair<Integer, Integer>(character.getIntCoordinate().x(),
                    getAdjacent(character.getCharacterPosition().y(), character.getIntCoordinate().y()))
                    .apply(Functions.sumInt(dir.getPair())));
        } else {
            l.add(new GenPair<Integer, Integer>(
                    getAdjacent(character.getCharacterPosition().x(), character.getIntCoordinate().x()),
                    character.getIntCoordinate().y()).apply(Functions.sumInt(dir.getPair())));
        }
        l.removeIf(p -> isOutOfBound(p));
        return l;
    }

    private int getAdjacent(final float n1, final int n2) {
        return n1 - n2 > 0.5 ? n2 + 1 : n2 - 1;
    }

    private boolean isOutOfBound(final GenPair<Integer, Integer> p) {
        return p.x() < MIN_NUM_CELL || p.y() < MIN_NUM_CELL || p.x() > MAX_NUM_CELL || p.y() > MAX_NUM_CELL;
    }
}
