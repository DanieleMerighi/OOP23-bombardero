package it.unibo.bombardero.cell.powerup.api;

import it.unibo.bombardero.cell.Cell;
import it.unibo.bombardero.character.Character;

/**
 * Represents a power-up in the game. A power-up is a special item that can be collected
 * by characters to gain various effects.
 */
public interface PowerUp extends Cell {

    /**
     * Enumeration representing the different types of power-ups in the game.
     */
    enum PowerUpType {
        // Bomb type-related P.U.
        /**
         * The remote bomb power-up.
         * <p>
         * Allows the character that picks it up to explode the placed bombs when
         * wanted.
         * </p>
         * The player needs to press the 'P' button to explode the bomb.
         */
        REMOTE_BOMB(5),
        /**
         * The piercing bomb power-up.
         * <p>
         * Allows the character that picks it up to place piercing bombs that allow
         * flames to pass through breakable walls and leave nothing behind.
         * </p>
         */
        PIERCING_BOMB(10),
        /**
         * The power bomb power-up.
         * <p>
         * Allows the character that picks it up to place bombs with the max flame range
         * possible.
         * </p>
         */
        POWER_BOMB(10),
        // Number of bomb-related P.U.
        /**
         * The plus one bomb power-up.
         * <p>
         * The character that picks it up gets an extra bomb that he can place.
         * </p>
         */
        PLUS_ONE_BOMB(55),
        /**
         * The minus one bomb power-up.
         * <p>
         * The character who picks it up can place one less bomb.
         * </p>
         */
        MINUS_ONE_BOMB(15),
        // Bomb range-related P.U.
        /**
         * The plus one flame range power-up.
         * <p>
         * The character that picks it up gains some flame range.
         * </p>
         */
        PLUS_ONE_FLAME_RANGE(55),
        /**
         * The minus one flame range power-up.
         * <p>
         * The character that picks it up loses a bit of his flame range.
         * </p>
         */
        MINUS_ONE_FLAME_RANGE(10),
        /**
         * The max flame range power-up.
         * <p>
         * The character that picks it up gains the max flame range possible.
         * </p>
         */
        MAX_FLAME_RANGE(5),
        // Speed-related P.U.
        /**
         * The plus one skate power-up.
         * <p>
         * The character that picks it up gains some speed.
         * </p>
         */
        PLUS_ONE_SKATES(55),
        /**
         * The minus one skate power-up.
         * <p>
         * The character that picks it up loses some speed.
         * </p>
         */
        MINUS_ONE_SKATES(15),
        // Botton-related P.U.
        /**
         * The line bomb power-up.
         * <p>
         * Allows the character that picks it up to place a line of bombs.
         * </p>
         * The player needs to press the 'L' button to place the line of bombs.
         */
        LINE_BOMB(15),
        // Debuff P.U.
        /**
         * The skull power-up.
         * <p>
         * The character that picks it up recives a random effect.
         * Be carefull, the skull is not friendly...
         * </p>
         */
        SKULL(20);

        private final double weight;

        /**
         * Constructor for PowerUpType.
         * 
         * @param weight
         */
        PowerUpType(final int weight) {
            this.weight = weight;
        }

        /**
         * Get the weight of the power-up type.
         * 
         * @return The weight of the power-up type.
         */
        public double getWeight() {
            return this.weight;
        }
    }

    /**
     * Gets the type of the PowerUp.
     * 
     * @return the type of the PowerUp
     */
    PowerUpType getType();

     /**
     * Applies the effect of the PowerUp to the specified character.
     * 
     * @param character the character to apply the effect to
     */
    void applyEffect(Character character);
}
