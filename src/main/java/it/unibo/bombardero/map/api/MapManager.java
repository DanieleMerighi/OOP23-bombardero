package it.unibo.bombardero.map.api;

/** 
 * The inteface describing the dynamic aspects of the Game Map: placement of breakable
 * and unbreakable walls and the logic behind the map's collapse when the timer ends
 *  @author Federico Bagattoni
 */
public interface MapManager {

    void generateBreakableWalls();

    /** 
     * This method is assumed to be called once and places the unbreakable obstacles
     * in the map, the placement for the obstacles is always the same
     */
	void placeUnbreakableWalls();

    void triggerArenaCollapse();

}
