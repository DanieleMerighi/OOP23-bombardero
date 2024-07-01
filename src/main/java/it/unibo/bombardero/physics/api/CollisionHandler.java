package it.unibo.bombardero.physics.api;

import java.util.Optional;
import java.awt.geom.Line2D;

import it.unibo.bombardero.cell.Cell;
import it.unibo.bombardero.cell.powerup.api.PowerUp;
import it.unibo.bombardero.character.Character;

public interface CollisionHandler {

    void applyCharacterBoundaryCollision(Character character, Optional<Cell> collidingCell);

    void applyCharacterBoundaryCollision(Character character, Line2D.Float mapOutline);

    void applyFlameCollision(Character character);

    void applyPowerUpCollision(Character character, PowerUp powerUp);
    
}