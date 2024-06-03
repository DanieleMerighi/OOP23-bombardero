package it.unibo.bombardero.guide.impl;

import org.jgrapht.alg.shortestpath.ContractionHierarchyBidirectionalDijkstra;

import it.unibo.bombardero.cell.BombFactory;
import it.unibo.bombardero.character.Character;
import it.unibo.bombardero.core.api.Controller;
import it.unibo.bombardero.core.api.GameManager;
import it.unibo.bombardero.core.impl.BombarderoGameManager;
import it.unibo.bombardero.guide.api.Guide;
import it.unibo.bombardero.map.api.Coord;
import it.unibo.bombardero.map.api.Pair;

public final class BombarderoGuideManager extends BombarderoGameManager implements Guide {

    public final static Coord PLAYER_GUIDE_SPAWNPOINT = new Coord(4.5f, 6.5f);
    public final static Pair CRATE_GUIDE_SPAWNPOINT = new Pair(8, 6);
    public final static Coord DUMMY_GUIDE_SPAWNPOINT = new Coord(7.5f, 5.5f);

    public BombarderoGuideManager(final Controller controller) {
        super(controller, true);
    }

    /* TODO: metodo update che non tiene il tempo e scandisce i vari eventi nella mappa di gioco 
    dopo che il giocatore fa le mosse che deve fare:
    0) mostra WASD
    1) plauer: si muove in tutte le direzioni
    2) mostra barra spazio
    3) player: piazza bomba
    4) mostra powerup e messaggio descrivendoli brevemente
     */

     @Override
    public void updateGame(final long elapsed) {
        this.getGameMap().update();
        this.getPlayer().update(elapsed);
    }

    public void spawnDummy() {
        addEnemy(new Dummy(DUMMY_GUIDE_SPAWNPOINT));
    }

    /**
     * This nested class represents a "Dummy", and NPC capable of 
     * doing nothing. It's sole purporse is to be the target of
     * the player during the guide.
     * @author Federico Bagattoni
     */
    private final class Dummy extends Character {
        
        /**
         * Creates a new dummy at the passed coordinate.
         * @param coord where to spawn the dummy
         */
        public Dummy(Coord coord) {
            super(BombarderoGuideManager.this, coord, BombarderoGuideManager.this.getBombFactory());
        }

        @Override
        public void update(long elapsedTime) {

        }
        
    }
    
}
