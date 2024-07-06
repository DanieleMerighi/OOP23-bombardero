package it.unibo.bombardero.map.api;

import java.util.function.Function;

//utility classs, leave the constructor and the final
/**
 * utility class that return Function for different type of GenPair for example to sum two 
 * Pair<Integer, Integer>.
 */
public final class Functions {

    private Functions() {

    }

    /**
     * 
     * @param p Pair to sum
     * @return return a function that takes a pair of Integer and return a new pair
     *         that is the sum with p
     */
    public static Function<GenPair<Integer, Integer>, GenPair<Integer, Integer>> sumInt(
            final GenPair<Integer, Integer> p) {
        return pair -> new GenPair<>(Integer.sum(pair.x(), p.x()), Integer.sum(pair.y(), p.y()));
    }

    /**
     * 
     * @param scale number that moltiplicates the pair
     * @return a function that takes a pair of Integer and return
     *         a new pair that is the moltiplicaton between the pair and scale
     */
    public static Function<GenPair<Integer, Integer>, GenPair<Integer, Integer>> multiplyInt(final int scale) {
        return pair -> new GenPair<>(pair.x() * scale, pair.y() * scale);
    }

    /**
     * 
     * @param p Pair to sum
     * @return return a function that takes a pair of Float and return a new pair
     *         that is the sum with p
     */
    public static Function<GenPair<Float, Float>, GenPair<Float, Float>> sumFloat(final GenPair<Float, Float> p) {
        return pair -> new GenPair<>(Float.sum(pair.x(), p.x()), Float.sum(pair.y(), p.y()));
    }

    /**
     * 
     * @param scale number that moltiplicates the pair
     * @return a function that takes a pair of Float and return
     *         a new pair that is the moltiplicaton between the pair and scale
     */
    public static Function<GenPair<Float, Float>, GenPair<Float, Float>> multiplyFloat(final float scale) {
        return pair -> new GenPair<>(pair.x() * scale, pair.y() * scale);
    }

    /**
     * 
     * @param p Pair to subtraction
     * @return return a function that takes a pair of Float and return a new pair
     *         that is the subtraction with p
     */
    public static Function<GenPair<Float, Float>, GenPair<Float, Float>> subtractFloat(final GenPair<Float, Float> p) {
        return pair -> new GenPair<>(pair.x() - p.x(), pair.y() - p.y());
    }

    /**
     * 
     * @param p Pair to subtraction
     * @return return a function that takes a pair of Float and return a new pair
     *         that is the subtraction with a Integer pair
     */
    public static Function<GenPair<Float, Float>, GenPair<Float, Float>> subtractFloatAndInt(
            final GenPair<Integer, Integer> p) {
        return pair -> new GenPair<>(pair.x() - p.x(), pair.y() - p.y());
    }

    /**
     * 
     * @param p
     * @return return a function that takes a pair of Float and return the ditance
     *         from the pair p
     */
    public static Function<GenPair<Float, Float>, Float> distanceTo(final GenPair<Float, Float> p) {
        return pair -> (float) Math.sqrt(Math.pow(pair.x() - p.x(), 2) + Math.pow(pair.y() - p.y(), 2));
    }

}
