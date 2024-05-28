package it.unibo.bombardero.cell.powerup.api;

import java.util.Map;

import it.unibo.bombardero.cell.Bomb.BombType;

/**
 * Enumeration representing different types of power-ups in the game.
 */
public enum PowerUpType {
    // Bomb type-related P.U.
    REMOTE_BOMB("powerup/bomb_remote", 5),
    PIERCING_BOMB("powerup/bomb_piercing", 15),
    POWER_BOMB("powerup/bomb_power", 10),
    // Number of bomb-related P.U.
    PLUS_ONE_BOMB("powerup/bomb_plus_one", 55),
    MINUS_ONE_BOMB("powerup/bomb_minus_one", 15),
    // Bomb range-related P.U.
    PLUS_ONE_FLAME_RANGE("powerup/flame_plus_one", 55),
    MINUS_ONE_FLAME_RANGE("powerup/flame_minus_one", 15),
    MAX_FLAME_RANGE("powerup/flame_max", 5),
    // Speed-related P.U.
    PLUS_ONE_SKATES("powerup/skates_plus_one", 55),
    MINUS_ONE_SKATES("powerup/skates_minus_one", 15),
    // Botton-related P.U.
    LINE_BOMB("powerup/line", 15),
    // Bomb movement-related P.U.
    KICK("powerup/kick", 10),
    // Debuff P.U.
    SKULL("powerup/skull", 25);

    private String typeString;
    private double weight;
    private static final Map <PowerUpType, BombType> TO_BOMB_TYPE = Map.of (
        PowerUpType.REMOTE_BOMB, BombType.BOMB_REMOTE,
        PowerUpType.PIERCING_BOMB, BombType.BOMB_PIERCING,
        PowerUpType.POWER_BOMB,  BombType.BOMB_POWER
    );


    /**
     * Constructor for PowerUpType.
     * 
     * @param typeString The string representation of the power-up type.
     * @param weight
     */
    PowerUpType(final String typeString, final int weight) {
        this.typeString = typeString;
        this.weight = weight;
    }

    /**
     * Get the string representation of the power-up type.
     * 
     * @return The string representation of the power-up type.
     */
    public String getTypeString() {
        return this.typeString;
    }

    /**
     * Get the weight of the power-up type.
     * 
     * @return The weight of the power-up type.
     */
    public double getWeight() {
        return this.weight;
    }

    /**
     * 
     * @return the corresponding BombType ,if no rapresent any type of bomb return BOMB_BASIC
     */
    public BombType toBombType() {
        if(TO_BOMB_TYPE.containsKey(this)) {
            return TO_BOMB_TYPE.get(this);
        } else {
            return BombType.BOMB_BASIC;
        }

    }
}
