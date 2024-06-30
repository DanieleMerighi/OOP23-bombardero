package it.unibo.bombardero.guide.impl;

import java.util.Stack;
import it.unibo.bombardero.character.Character;
import it.unibo.bombardero.core.api.Controller;
import it.unibo.bombardero.core.impl.BasicBombarderoGameManager;
import it.unibo.bombardero.guide.api.GuideManager;
import it.unibo.bombardero.guide.api.GuideStep;
import it.unibo.bombardero.map.api.Coord;
import it.unibo.bombardero.view.BombarderoViewMessages;

/**
 * This class represents a single instance of the game's guide
 * it is a slightly modified {@link BasicBombarderoGameManager} that 
 * doesn't spawn neither enemies nor crates regularly.
 * <p>
 * It spawns just a crate and the player and the game proceeds at 
 * stages, dictated by the Controller. 
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
     * @param controller the reference to the game's {@link Controller}.
     */
    public BombarderoGuideManager(final Controller controller) {
        super(controller, true);
        this.getGameMap().addBreakableWall(CRATE_GUIDE_SPAWNPOINT);
        initialiseProcedures();
    }

    @Override
    public void updateGame(final long elapsed) {
        super.updateGame(elapsed);
        if(!guideProcedures.isEmpty() && guideProcedures.peek().condition().test(getGameMap(), this)) {
            guideProcedures.pop().action().accept(this, getController());
        }
    }

    @Override
    public void spawnDummy() {
        addEnemy(new Dummy(DUMMY_GUIDE_SPAWNPOINT));
    }

    private void initialiseProcedures() {
        guideProcedures.add(new GuideStep(
            (map, manager) -> true /* manager.getEnemies().stream().allMatch(enemy -> !enemy.isAlive())*/,
            (manager, controller) -> {
                controller.toggleMessage(BombarderoViewMessages.END_GUIDE);
                controller.displayEndGuide();
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
            (map, manager) -> !manager.getPlayer().isStationary(),
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
    private final class Dummy extends Character {
        
        /**
         * Creates a new dummy at the passed coordinate.
         * @param coord where to spawn the dummy
         */
        public Dummy(final Coord coord) {
            super(BombarderoGuideManager.this, coord, BombarderoGuideManager.this.getBombFactory());
        }

        @Override
        public void update(final long elapsedTime) {

        }
        
    }
    
}
