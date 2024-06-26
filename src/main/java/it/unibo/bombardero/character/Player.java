package it.unibo.bombardero.character;

import it.unibo.bombardero.cell.BombFactory;
import it.unibo.bombardero.cell.powerup.impl.PowerUpImpl;
import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.map.api.Coord;
import it.unibo.bombardero.physics.api.BoundingBox;

/**
 * This class represents a Player in the game.
 * This class extends the Character class and is responsible for handling
 * player-specific actions and behaviors.
 */
public class Player extends Character {

    /**
     * Constructs a new Player instance with the specified parameters.
     * 
     * @param manager     the game manager that controls the game state
     * @param coord       the initial coordinates where the player is spawned
     * @param bombFactory the factory to create bombs
     */
    public Player(final Coord coord, final BombFactory bombFactory, final BoundingBox bBox) {
        super(coord, bombFactory, bBox);
    }

    /**
     * Updates the Player's interactions with the map.
     * This method should define how the player moves and interacts based on user
     * input.
     */
    @Override
    public void update(final long elapsedTime, final GameManager manager) {
        // Skeleton effect:
        if (getResetEffect().isPresent()) { // If there's a Task to reset
            updateSkeleton(elapsedTime, manager, CharacterType.PLAYER);
        }
        // Player movement:
        if (!isStationary()) { // If he's not stationary, computes the new position
            setCharacterPosition(computeNewPlayerPosition());
        }
        // Place bomb:
        if (getHasToPlaceBomb()) {
            placeBomb(manager, CharacterType.PLAYER);
            setHasToPlaceBomb(false);
        }
        // Place line bomb:
        if (getHasToPlaceLineBomb()) {
            PowerUpImpl.placeLineBomb(this, manager.getGameMap().getMap(), getFacingDirection(), manager);
            setHasToPlaceLineBomb(false);
        }
        // Explode remote bomb:
        if (getHasToExplodeRemoteBomb()) {
            explodeRemoteBomb();
            setHasToExplodeRemoteBomb(false);
        }
    }

    /**
     * Sums the current Player position with the direction recived in imput
     * Uses getSpeed to determine how much the Player needs to move in the facing
     * direction.
     * 
     * @return the new updated Player position
     */
    private Coord computeNewPlayerPosition() {
        return getCharacterPosition()
                .sum(new Coord((getFacingDirection().x()) * getSpeed(),
                        (getFacingDirection().y()) * getSpeed()));
    }
}
