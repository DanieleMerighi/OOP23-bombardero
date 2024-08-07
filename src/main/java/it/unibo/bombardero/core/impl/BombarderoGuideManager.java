package it.unibo.bombardero.core.impl;

import java.util.Stack;
import java.util.List;

import it.unibo.bombardero.bomb.api.BombFactory;
import it.unibo.bombardero.character.Character;
import it.unibo.bombardero.core.api.Controller;
import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.core.api.GuideManager;
import it.unibo.bombardero.core.api.GuideStep;
import it.unibo.bombardero.map.api.GenPair;
import it.unibo.bombardero.physics.api.CollisionEngine;
import it.unibo.bombardero.view.BombarderoViewMessages;
import it.unibo.bombardero.view.api.GraphicsEngine.EndGameState;

/**
 * This class represents a single instance of the game's guide
 * it is a slightly modified {@link BasicBombarderoGameManager} that 
 * doesn't spawn neither enemies nor crates regularly.
 * <p>
 * <p>
 * It spawns just a crate and the player and the game proceeds at 
 * stages, dictated by the state of the game.
 * @author Federico Bagattoni
 */
public final class BombarderoGuideManager extends BasicBombarderoGameManager implements GuideManager {

    private final Stack<GuideStep> guideProcedures = new Stack<>();

    /**
     * Creates a new {@link BombarderoGuideManager}, creating a map with no
     * crates, spawning the player in the middle of the map and a single
     * crate to the side of the player.
     * <p>
     * The spawnpoints are represented by the fields {@link #PLAYER_GUIDE_SPAWNPOINT} 
     * and {@link #CRATE_GUIDE_SPAWNPOINT}
     * @param cEngine the collision engine related to this instance of the game.
     */
    public BombarderoGuideManager(final CollisionEngine cEngine) {
        super(GuideManager.PLAYER_GUIDE_SPAWNPOINT, List.of(), false, cEngine);
        addBreakableWall(CRATE_GUIDE_SPAWNPOINT);
        initialiseProcedures();
    }

    @Override
    public void updateGame(final long elapsed, final Controller controller) {
        super.updateGame(elapsed, controller);
        if (!guideProcedures.isEmpty() && guideProcedures.peek().condition().test(getGameMap(), this)) {
            guideProcedures.pop().action().accept(this, controller);
        }
    }

    @Override
    public void spawnDummy() {
        addEnemy(new BombarderoGuideManager.Dummy(DUMMY_GUIDE_SPAWNPOINT, getBombFactory()));
    }

    private void initialiseProcedures() {
        guideProcedures.add(new GuideStep(
            (map, manager) -> manager.getEnemies().stream().allMatch(enemy -> !enemy.isAlive()),
            (manager, controller) -> {
                controller.toggleMessage(BombarderoViewMessages.END_GUIDE);
                controller.displayEndScreen(EndGameState.LOSE);
            }
        ));
        guideProcedures.add(new GuideStep(
            (map, manager) -> map.isEmpty(CRATE_GUIDE_SPAWNPOINT),
            (manager, controller) -> {
                manager.spawnDummy();
                controller.toggleMessage(BombarderoViewMessages.KILL_ENEMY);
            }
        ));
        guideProcedures.add(new GuideStep(
            (map, manager) -> map.isPowerUp(CRATE_GUIDE_SPAWNPOINT),
            (manager, controller) -> controller.toggleMessage(BombarderoViewMessages.EXPLAIN_POWERUP)
        ));
        guideProcedures.add(new GuideStep(
            (map, manager) -> !manager.getPlayers().stream().allMatch(Character::isStationary),
            (manager, controller) -> controller.toggleMessage(BombarderoViewMessages.PLACE_BOMB)
        ));
    } 

    /**
     * This nested class represents a "Dummy", a NPC capable of 
     * doing nothing. 
     * <p>
     * It's sole purporse is to be the target of the player during the guide.
     * @author Federico Bagattoni
     */
    private static class Dummy extends Character {

        /**
         * Creates a new dummy at the passed coordinate.
         * @param coord where to spawn the dummy
         * @param bombFactory the {@link BombFactory} that the character will use to produce bombs
         */
        Dummy(final GenPair<Float, Float> coord, final BombFactory bombFactory) {
            super(coord, bombFactory);
        }

        @Override
        protected void performCharacterActions(final GameManager manager, final long elapsedTime) {

        }
    }

}
