package it.unibo.bombardero.map.impl;

import java.util.ArrayList;
import java.util.List;

import it.unibo.bombardero.map.api.GenPair;
import it.unibo.bombardero.map.api.MatrixTraversalStrategy;

/** 
 * An algorithm that traverses a matrix in a spiral form.
 */
public final class SpiralTraversalStrategy implements MatrixTraversalStrategy {

    @Override
    public List<GenPair<Integer, Integer>> compute(int rows, int cols) {
        final List<GenPair<Integer, Integer>> order = new ArrayList<>();
        int top = 0, bottom = rows - 1, left = 0, right = cols - 1;
        while (top <= bottom && left <= right) {

            for (int i = left; i <= right; i++) {
                order.add(new GenPair<>(top, i));
            }
            top++;
            for (int i = top; i <= bottom; i++) {
                order.add(new GenPair<>(i, right));
            }
            right--;
            if (top <= bottom) {
                for (int i = right; i >= left; i--) {
                    order.add(new GenPair<>(bottom, i));
                } 
                bottom--;
            }
            if (left <= right) {
                for (int i = bottom; i >= top; i--) {
                    order.add(new GenPair<>(i, left));
                }
                left++;
            }
        }
        return order;
    }
    
}
