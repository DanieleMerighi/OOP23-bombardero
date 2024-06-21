package it.unibo.bombardero.cell.powerup.api;

import it.unibo.bombardero.map.api.Pair;

public interface PowerUpFactory {
    
    PowerUp createPowerUp(Pair pos);
}
