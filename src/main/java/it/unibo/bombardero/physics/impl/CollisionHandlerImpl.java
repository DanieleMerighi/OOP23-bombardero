package it.unibo.bombardero.physics.impl;

import java.util.Optional;

import java.awt.geom.Line2D;

import it.unibo.bombardero.cell.Cell;
import it.unibo.bombardero.cell.powerup.api.PowerUp;
import it.unibo.bombardero.character.Character;
import it.unibo.bombardero.map.api.Functions;
import it.unibo.bombardero.physics.api.BoundingBox;
import it.unibo.bombardero.physics.api.CollisionHandler;

/**
 * Take character that are colliding with a Cell and apply the effect of the collision.
 */
public final class CollisionHandlerImpl implements CollisionHandler {

    @Override
    public void applyCharacterBoundaryCollision(final Character character, final Optional<Cell> collidingCell) {
        if (collidingCell.isPresent()) {
            character.setCharacterPosition(character.getCharacterPosition().apply(Functions
                .sumFloat(character.getBoundingBox()
                    .computeCollision(collidingCell.get().getBoundingBox().get(), character.getFacingDirection()))));
        }
    }

    @Override
    public void applyCharacterBoundaryCollision(final Character character, final Line2D.Float mapOutline) {
        final BoundingBox bBox = character.getBoundingBox();
        if (bBox.isColliding(mapOutline)) {
            character.setCharacterPosition(
                character.getCharacterPosition().apply(Functions
                .sumFloat(bBox.computeCollision(mapOutline, character.getFacingDirection()))));
        }
    }

    @Override
    public void applyFlameCollision(final Character character) {
        killCharacter(character);
    }

    @Override
    public void applyPowerUpCollision(final Character character, final PowerUp powerUp) {
        powerUp.applyEffect(character);
    }

    @Override
    public void applyCollapseCollision(final Character character) {
        killCharacter(character);
    }

    private void killCharacter(final Character character) {
        character.kill();
    }

}
