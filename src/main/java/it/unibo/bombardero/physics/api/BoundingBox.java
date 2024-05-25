package it.unibo.bombardero.physics.api;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import it.unibo.bombardero.character.Direction;

public interface BoundingBox {

    /**
     * move the bounding box in the same position of a character
     * @param pos position of a character
     */
    void move(Point2D pos);

    /**
     * @param bBox
     * @return if this bounding box is colliding with anothe bounding box
     */
    boolean isColliding(BoundingBox bBox);

    /**
     * 
     * @param cellBox
     * @param dir
     * @return the distance of the collision
     */
    float distanceOfCollision(Rectangle2D bBox, Direction dir);

    /**
     * @return the rectangle that rappresent the bounding box
     */
    Rectangle2D getPhysicsBox();

}
