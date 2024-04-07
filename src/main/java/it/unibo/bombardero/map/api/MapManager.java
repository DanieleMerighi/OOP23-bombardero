package it.unibo.bombardero.map.api;

/** 
 * The inteface describing the dynamic aspects of the Game Map: placement of breakable
 * and unbreakable walls and the logic behind the map's collapse when the timer ends
 *  @author Federico Bagattoni
 */
public interface MapManager {

    /**
     * Generates and places the breakables obstacles in the game, this method is assumed to be called
     * once during the game's loading. This method generates a variable quantity of walls, according to 
     * how much walls are desired to be present (e.g. 70% or 80% of free space is occupied by such 
     * obstacles)
     */
    void placeBreakableWalls();

    /** 
     * This method is assumed to be called once and places the unbreakable obstacles
     * in the map, the placement for the obstacles is always the same
     */
	void placeUnbreakableWalls();

    /**
     * Sets the manager in collapse mode and computes the order in which the walls will collapse
     * (e.g. collapse in spiral form, or from left to right), it has been estabilished that the 
     * walls may collapse in a spiral form
     */
    void triggerArenaCollapse();

    /** 
     * Updates the dynamic aspect of the mapManager, if the manager is in collapse mode then it
     * proceeds with the collapse
     */ 
    void update(); 
}
