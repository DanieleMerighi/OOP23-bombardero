package it.unibo.bombardero.cell.powerup.impl;

import java.util.function.Consumer;
import java.util.stream.IntStream;
import java.util.Map;

import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.character.Character;
import it.unibo.bombardero.character.Direction;
import it.unibo.bombardero.cell.AbstractCell;
import it.unibo.bombardero.cell.Cell;
import it.unibo.bombardero.cell.powerup.api.PowerUp;
import it.unibo.bombardero.cell.powerup.api.PowerUpEffectStrategy;
import it.unibo.bombardero.cell.powerup.api.PowerUpType;

public class PowerUpImpl extends AbstractCell implements PowerUp {
    final private PowerUpType type;
    final private Consumer<Character> effect;

    public PowerUpImpl(final PowerUpType type, Pair pos, final PowerUpEffectStrategy strategy) {
        super(CellType.POWERUP, pos, true);
        this.type = type;
        this.effect = strategy.getEffect();
    }

    @Override
    public PowerUpType getType() {
        return type;
    }

    @Override
    public void applyEffect(final Character character) {
        effect.accept(character);
        // System.out.println(this.type);
    }

    //how to stop the forEach from placing bomb after placebomb returns flase?
    public static void placeLineBomb(final Character character, final Map<Pair, Cell> map, final Direction facingDirection) {
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
                        )
                    )
                );
        }
    }

}
