package it.unibo.bombardero.cell.powerup.impl;

import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import it.unibo.bombardero.cell.AbstractCell;
import it.unibo.bombardero.cell.Cell;
import it.unibo.bombardero.cell.powerup.api.PowerUp;
import it.unibo.bombardero.cell.powerup.api.PowerUpEffect;
import it.unibo.bombardero.cell.powerup.api.PowerUpType;
import it.unibo.bombardero.character.Character;
import it.unibo.bombardero.character.Direction;
import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.map.api.Pair;

/**
 * Implementation of the PowerUp interface representing a power-up in the game.
 */
public final class PowerUpImpl extends AbstractCell implements PowerUp {

    private final PowerUpType type;

    private final Consumer<Character> effect;
    
    /**
     * Places a line of bombs in the direction the character is facing.
     * 
     * @param character         the character placing the bombs
     * @param map               the map of cells
     * @param facingDirection   the direction the character is facing
     */
    public static void placeLineBomb(final Character character, final Map<Pair, Cell> map, final Direction facingDirection, final GameManager manager) {
        if (character.hasLineBomb()) {
            // Lo stream continua finché non ha piazzato tutte le bombe o finché incontra un ostacolo
            IntStream
                .range(0, character.getNumBomb())
                // Lo stream continua finché trova celle vuote (celle non salvate in keys)
                .takeWhile(offset -> !map
                        .containsKey(character
                        .getIntCoordinate()
                        .sum(new Pair(
                            facingDirection.x() * offset,
                            facingDirection.y() * offset)
                        )
                    )
                )
                // Piazzo le bombe nelle celle vuote
                .forEach(offset -> character
                    .placeBomb(character
                        .getIntCoordinate()
                        .sum(new Pair(
                            facingDirection.x() * offset,
                            facingDirection.y() * offset)
                        ), manager
                    )
                );
        }
    }

    /**
     * Constructs a new implementation of PowerUp with the specified parameters.
     * 
     * @param type      the type of PowerUp
     * @param position  the position of the PowerUp in the map
     * @param effect    the effect of the PowerUp
     */
    public PowerUpImpl(final PowerUpType type, final Pair position, final PowerUpEffect effect) {
        super(CellType.POWERUP, position, false, null);
        this.type = type;
        this.effect = effect.getEffect();
    }

    @Override
    public PowerUpType getType() {
        return type;
    }

    @Override
    public void applyEffect(final Character character) {
        effect.accept(character);
    }

}
