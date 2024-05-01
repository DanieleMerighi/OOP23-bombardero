package it.unibo.bombardero.cell;

public class PowerUp extends Cell {

    public enum PowerUpType {
        // Bomb type-related P.U.
        REMOTE_BOMB("powerup/bomb_remote"),
        PIERCING_BOMB("powerup/bomb_piercing"),
        HYPER_BOMB("powerup/bomb_hyper"),
        LINE_BOMB("powerup/line"),
        // Bomb range-related P.U.
        PLUSONE_BOMBRANGE("powerup/fire_plusone"),
        MINUSONE_BOMBRANGE("powerup/fire_minusone"),
        MAX_BOMBRANGE("powerup/fire_max"),
        // Speed-related P.U.
        PLUSONE_SKATES("powerup/skates"),
        MINUSONE_SKATES("powerup/skates_minusone"),
        // Other P.U.
        SKULL("powerup/skull"),
        PUNCH("powerup/punch");

        private String typeString;

        private PowerUpType(final String typeString) {
            this.typeString = typeString;
        }

        public String getTypeString() {
            return this.typeString;
        }
    }

    public PowerUp(CellType type) {
        super(type);
    }
    
}
