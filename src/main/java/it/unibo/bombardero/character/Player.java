package it.unibo.bombardero.character;

import it.unibo.bombardero.cell.BombFactory;
import it.unibo.bombardero.cell.powerup.impl.PowerUpImpl;
import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.map.api.Functions;
import it.unibo.bombardero.map.api.GenPair;
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
     * @param coord         the initial coordinates where the player is spawned
     * @param bombFactory   the factory to create bombs
     * @param bBox          the hit box of the player
     */
    public Player(final GenPair<Float, Float> coord, final BombFactory bombFactory, final BoundingBox bBox) {
        super(coord, bombFactory, bBox);
    }

    /**
     * Updates the Player's interactions with the map.
     * This method should define how the player moves and interacts based on user
     * input.
     * 
     * @param manager   the game manager
     * @param elapsedTime   the time elapsed since the last update
     */
    @Override
    public void update(final GameManager manager, final long elapsedTime) {
        // Skeleton effect:
        if (getResetEffect().isPresent()) { // If there's a Task to reset
            updateSkeleton(manager, elapsedTime, CharacterType.PLAYER);
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
    private GenPair<Float, Float> computeNewPlayerPosition() {
        return getCharacterPosition().apply(Functions
                .sumFloat(new GenPair<Float, Float>((getFacingDirection().x()) * getSpeed(),
                        (getFacingDirection().y()) * getSpeed())));
    }
}
