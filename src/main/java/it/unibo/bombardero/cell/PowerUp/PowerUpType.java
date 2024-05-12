package it.unibo.bombardero.cell.PowerUp;

public enum PowerUpType {
    // Bomb type-related P.U.
    REMOTE_BOMB("powerup/bomb_remote"),
    PIERCING_BOMB("powerup/bomb_piercing"),
    POWER_BOMB("powerup/bomb_hyper"),
    // Bomb range-related P.U.
    PLUSONE_BOMBRANGE("powerup/fire_plus_one"),
    MINUSONE_BOMBRANGE("powerup/fire_minus_one"),
    MAX_BOMBRANGE("powerup/fire_max"),
    // Speed-related P.U.
    PLUSONE_SKATES("powerup/skates_plus_one"),
    MINUSONE_SKATES("powerup/skates_minus_one"),
    // Botton-related P.U.
    PUNCH("powerup/punch"),
    LINE_BOMB("powerup/line"),
    // Other P.U.
    SKULL("powerup/skull");

    private String typeString;

    private PowerUpType(final String typeString) {
        this.typeString = typeString;
    }

    public String getTypeString() {
        return this.typeString;
    }
}
