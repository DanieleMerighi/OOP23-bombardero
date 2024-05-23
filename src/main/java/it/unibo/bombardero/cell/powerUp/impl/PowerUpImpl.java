package it.unibo.bombardero.cell.powerUp.impl;

import java.util.function.Consumer;
import java.util.stream.IntStream;
import java.util.Map;

import it.unibo.bombardero.map.api.GameMap;
import it.unibo.bombardero.map.api.Pair;
import it.unibo.bombardero.character.Character;
import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.cell.Cell;
import it.unibo.bombardero.cell.powerUp.api.PowerUp;
import it.unibo.bombardero.cell.powerUp.api.PowerUpEffectStrategy;
import it.unibo.bombardero.cell.powerUp.api.PowerUpType;

public class PowerUpImpl extends Cell implements PowerUp {
    private PowerUpType type;
    private Consumer<Character> effect;
    // private float kickBombSpeed;
    // private List<Pair<Direction, Bomb>> kickBombList = new ArrayList<>();

    public PowerUpImpl(final PowerUpType type, final PowerUpEffectStrategy strategy) {
        super(CellType.POWERUP);
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
    }

    //how to stop the forEach from placing bomb after placebomb returns flase?
    public static void placeLineBomb(final Character character, final Map<Pair,Cell> map) {
        if (character.hasLineBomb()) {
            // lo stream continua finché non ha piazzato tutte le bombe o finché incontra un ostacolo
            IntStream
                .range(0, character.getNumBomb())
                // Lo stream continua finché trova celle vuote (celle che non contengono keys)
                .takeWhile(offset -> !map
                        .containsKey(character
                        .getIntCoordinate()
                        .sum(new Pair(
                            character.getFacingDirection().getDy() * offset,
                            character.getFacingDirection().getDx() * offset)
                        )
                    )
                ) // Piazzo le bombe nelle celle vuoto
                .forEach(offset -> character
                    .placeBomb(character
                        .getIntCoordinate()
                        .sum(new Pair(
                            character.getFacingDirection().getDy() * offset,
                            character.getFacingDirection().getDx() * offset)
                        )
                    )
                );
        }
    }

    /*
     * public void kick(Character character, Bomb bomb){
     * if (character.hasKick()) {
     * kickBombList.add(new Pair<>(character.getFacingDirection(), bomb));
     * /*
     * aggiungo alla lista la bomba con la direction. All'update scorro la lista
     * se non è empty. muovo di kickBombSpeed (da mettere a costante)
     * continua finché non ha collition. con player se ha il kick la ricalcia nella
     * direzione che guarda ?
     * lo rimuovo dall'array dalla collition a meno che non stia guardando nella
     * direzione opposta il player
     * se hitta altro rimuovo. Oppure a tempo, dopo 3 secondi tanto esplode
     *
     * }
     * }
     */

    /*
     * public void update(){
     * kickBombList.forEach(bomb -> {
     * Coord coord = bomb.getSecond().getPos();
     * coord = new it.unibo.bombardero.map.api.Pair((int) (bomb.getFirst().getDy() *
     * kickBombSpeed), (int) (bomb.getFirst().getDx() * kickBombSpeed));
     * bomb.getSecond().setPos(bomb.getSecond().getPos().sum(coord));
     * bomb.getSecond().getPos();
     * });
     * }
     */

}
