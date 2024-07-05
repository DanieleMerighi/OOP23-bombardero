package it.unibo.bombardero.physics.api;

import java.util.Optional;
import java.awt.geom.Line2D;

import it.unibo.bombardero.cell.Cell;
import it.unibo.bombardero.cell.powerup.api.PowerUp;
import it.unibo.bombardero.character.Character;

/**
 * This interface take objects that are colliding with somethig and apply the effect of the collision.
 */
public interface CollisionHandler {

    /**
     * Set the Character's position so that his BoundingBox is out from the cell's BoundingBox.
     * @param character
     * @param collidingCell
     */
    void applyCharacterBoundaryCollision(Character character, Optional<Cell> collidingCell);

    /**
     * Set the Character's position so that his BoundingBox is out from line.
     * @param character
     * @param mapOutline
     */
    void applyCharacterBoundaryCollision(Character character, Line2D.Float mapOutline);

    /**
     * If the Character is over a Flame he dies.
     * @param character
     */
    void applyFlameCollision(Character character);

    /**
     * If the Character is over a PowerUp he picks it up.
     * @param character
     * @param powerUp
     */
    void applyPowerUpCollision(Character character, PowerUp powerUp);

}
