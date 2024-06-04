package it.unibo.bombardero.cell.powerup.api;

/**
 * Enumeration representing different types of power-ups in the game.
 */
public enum PowerUpType {
    // Bomb type-related P.U.
    REMOTE_BOMB(5),
    PIERCING_BOMB(15),
    POWER_BOMB(10),
    // Number of bomb-related P.U.
    PLUS_ONE_BOMB(55),
    MINUS_ONE_BOMB(15),
    // Bomb range-related P.U.
    PLUS_ONE_FLAME_RANGE(55),
    MINUS_ONE_FLAME_RANGE(5),
    MAX_FLAME_RANGE(5),
    // Speed-related P.U.
    PLUS_ONE_SKATES(55),
    MINUS_ONE_SKATES(15),
    // Botton-related P.U.
    LINE_BOMB(15),
    // Bomb movement-related P.U.
    KICK(10),
    // Debuff P.U.
    SKULL(25);

    private double weight;


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
