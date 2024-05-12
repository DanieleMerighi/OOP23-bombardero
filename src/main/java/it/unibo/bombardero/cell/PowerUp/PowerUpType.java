package it.unibo.bombardero.cell.PowerUp;

public enum PowerUpType {
    // Bomb type-related P.U.
    REMOTE_BOMB("powerup/bomb_remote"),
    PIERCING_BOMB("powerup/bomb_piercing"),
    POWER_BOMB("powerup/bomb_power"),
    // Number of bomb-related P.U.
    PLUS_ONE_BOMB("powerup/bomb_plus_one"),
    MINUS_UNE_BOMB("powerup/bomb_minus_one"),
    // Bomb range-related P.U.
    PLUS_ONE_FLAME_RANGE("powerup/flame_plus_one"),
    MINUS_ONE_FLAME_RANGE("powerup/flame_minus_one"),
    MAX_FLAME_RANGE("powerup/flame_max"),
    // Speed-related P.U.
    PLUS_ONE_SKATES("powerup/skates_plus_one"),
    MINUS_ONE_SKATES("powerup/skates_minus_one"),
    // Botton-related P.U.
    PUNCH("powerup/punch"),
    LINE_BOMB("powerup/line"),
    // Bomb movement-related P.U.
    KICK("powerup/kick"),
    // Debuff P.U.
    SKULL("powerup/skull");

    private String typeString;

    private PowerUpType(final String typeString) {
        this.typeString = typeString;
    }

    public String getTypeString() {
        return this.typeString;
    }
}
