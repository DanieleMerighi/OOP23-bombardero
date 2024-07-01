package it.unibo.bombardero.physics.impl;

import java.util.Optional;
import java.awt.geom.Line2D;

import it.unibo.bombardero.cell.Cell;
import it.unibo.bombardero.cell.powerup.api.PowerUp;
import it.unibo.bombardero.character.Character;
import it.unibo.bombardero.physics.api.BoundingBox;
import it.unibo.bombardero.physics.api.CollisionHandler;

public class CollisionHandlerImpl implements CollisionHandler{

    @Override
    public void applyCharacterBoundaryCollision(final Character character, final Optional<Cell> collidingCell) {
        if(collidingCell.isPresent()) {
            character.setCharacterPosition(character.getCharacterPosition()
                .sum(character.getBoundingBox()
                    .computeCollision(collidingCell.get().getBoundingBox(), character.getFacingDirection())));
        }
    }

    @Override
    public void applyCharacterBoundaryCollision(final Character character, final Line2D.Float mapOutline) {
        BoundingBox bBox = character.getBoundingBox();
        if(bBox.isColliding(mapOutline)) {
            character.setCharacterPosition(
                character.getCharacterPosition()
                .sum(bBox.computeCollision(mapOutline, character.getFacingDirection())));
        }
    }

    @Override
    public void applyFlameCollision(final Character character) {
        character.kill();
    }

    @Override
    public void applyPowerUpCollision(final Character character, final PowerUp powerUp) {
        powerUp.applyEffect(character);
    }

}
