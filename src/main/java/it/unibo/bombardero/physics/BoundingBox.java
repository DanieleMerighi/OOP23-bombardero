package it.unibo.bombardero.physics;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import it.unibo.bombardero.character.Direction;

public interface BoundingBox {

    void move(Point2D pos);

    boolean isColliding(Rectangle2D cellBox);

    float distanceOfCollision(Rectangle2D cellBox, Direction dir);

}
