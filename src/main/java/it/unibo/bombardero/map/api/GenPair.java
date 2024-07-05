package it.unibo.bombardero.map.api;

import java.util.function.Function;

import org.apache.commons.math3.util.Pair;

/**
 * This is a generic Pair whith method that require Function to apply at the
 * Pair
 */
public class GenPair<K, V> extends Pair<K, V> {

    /**
     * 
     * @param k First element
     * @param v Second element
     */
    public GenPair(final K k, final V v) {
        super(k, v);
    }

    /**
     * 
     * @return The first element
     */
    public K x() {
        return super.getFirst();
    }

    /**
     * 
     * @return the second element
     */
    public V y() {
        return super.getSecond();
    }

    /**
     * 
     * @param f funtion tha will be applied to th Pair
     * @return another GenPair
     */
    public GenPair<K, V> apply(final Function<GenPair<K, V>, GenPair<K, V>> f) {
        return f.apply(this);
    }

    /**
     * 
     * @param f funtion tha will be applied to the Pair
     * @return the result of the function
     */
    public K reduce(final Function<GenPair<K, V>, K> f) {
        return f.apply(this);
    }
}