package it.unibo.bombardero.cell.powerup.api;

import java.util.Map;

import it.unibo.bombardero.cell.Bomb.BombType;

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
    // Debuff P.U.
    SKULL(25);

    private double weight;
    private static final Map <PowerUpType, BombType> TO_BOMB_TYPE = Map.of (
        PowerUpType.REMOTE_BOMB, BombType.BOMB_REMOTE,
        PowerUpType.PIERCING_BOMB, BombType.BOMB_PIERCING,
        PowerUpType.POWER_BOMB,  BombType.BOMB_POWER
    );


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
