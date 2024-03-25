package it.unibo.bombardero.map.api;

/** 
 * The inteface describing the dynamic aspects of the Game Map: placement of breakable
 * and unbreakable walls and the logic behind the map's collapse when the timer ends
 *  @author Federico Bagattoni
 */
public interface MapManager {

    void generateBreakableWalls();

	void placeUnbreakableWalls();

    void placeNextWall();

    void triggerArenaCollpse();

}
