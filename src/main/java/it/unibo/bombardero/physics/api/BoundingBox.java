package it.unibo.bombardero.physics.api;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import it.unibo.bombardero.character.Direction;
import it.unibo.bombardero.map.api.Coord;

/**
 * this interface rapresents the solid part of cells and characters.
 */
public interface BoundingBox {

    /**
     * move the bounding box in the same position of a character.
     * @param pos position of a character
     */
    void move(Point2D pos);

    /**
     * @param bBox BoundingBox of an adiacent Cell
     * @return if this bounding box is colliding with another bounding box
     */
    boolean isColliding(BoundingBox bBox);

    /**
     * 
     * @param bBox bBox of the object with which it is colliding.
     * @param dir direction of the Character
     * @return the distance of the collision
     */
    Coord computeCollision(BoundingBox bBox, Direction dir);

    /**
     * 
     * @param mapOutline the line that rapresent an Outline of the map
     * @return if the BoundingBox is colliding with this line
     */
    boolean isColliding(Line2D.Float mapOutline);

    /**
     * 
     * @param mapOutline the line that rapresent an Outline of the map
     * @param dir direction of the Character
     * @return the distance of the collision
     */
    Coord computeCollision(Line2D.Float mapOutline, Direction dir);

    /**
     * @return the rectangle that rappresent the bounding box
     */
    Rectangle2D getPhysicsBox();

}
