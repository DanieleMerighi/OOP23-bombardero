package it.unibo.bombardero.map.api;

import java.util.List;

/** 
 * A generic strategy that returns a way to traverse a matrix. This
 * serves as mean to generate the order at which the walls of the 
 * arena will collapse.
 * <p> 
 * Partial traversal are accepted. 
 */
public interface MatrixTraversalStrategy {

    /**
     * Compute the order to traverse a matrix with the passed
     * size. 
     * @param rows the number of rows of the matrix
     * @param cols the number of columns of the matrix
     * @return a list of {@link Cell}, the first one being the first cell visited 
     * in the traversal
     */
    List<GenPair<Integer, Integer>> compute(int rows, int cols);

}
