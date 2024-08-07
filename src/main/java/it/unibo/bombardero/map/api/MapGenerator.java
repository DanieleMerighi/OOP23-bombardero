package it.unibo.bombardero.map.api;

import java.util.Set;
import java.util.List;

/** 
 * The inteface describing the dynamic aspects of the Game Map: placement of breakable
 * and unbreakable walls and the logic behind the map's collapse when the timer ends.
 *  @author Federico Bagattoni
 */
public interface MapGenerator {

    /**
     * Computes and returns the wall that have to be generated given the 
     * percentage passed as argument.
     * @param wallSpawnRate the percentage at which walls have to be present in the map.
     * @return the number of breakable walls that have to be spawned. 
     */
    int getTotalWallsToGenerate(double wallSpawnRate);

    /** 
     * Generates the positions in which unbreakable walls have to spawn, 
     * The positions are fixed in a grid, each wall separated from another
     * by an empty cell.
     * @return a {@link Set} containing all the positions of the walls.
     */
    Set<GenPair<Integer, Integer>> generateUnbreakableWalls();

    /** 
     * Generates the requested number of breakable walls, avoiding spawing them 
     * in the map's corners and in occupied spaces in the {@link GameMap} passed
     * as argument. 
     * @param map the {@link GameMap} object on which the walls will be placed
     * @param totalWallsToGenerate the total number of walls that have to be generated
     * @return a {@link Set} containing all the positions of the walls.
     */
    Set<GenPair<Integer, Integer>> generateBreakableWalls(GameMap map, int totalWallsToGenerate);

    /** 
     * Generates the order of cell on which an Unbreakable wall has to be spawned
     * during the map's collpase.
     * @param collapseStrategy the strategy used to compute the collapse order (e.g. spiral 
     * alternating rows and columns, up and down)
     * @return a list of positions, the first position being the first wall to collapse.
     * @see MatrixTraversalStrategy
     */
    List<GenPair<Integer, Integer>> generateCollapseOrder(MatrixTraversalStrategy collapseStrategy);


}
