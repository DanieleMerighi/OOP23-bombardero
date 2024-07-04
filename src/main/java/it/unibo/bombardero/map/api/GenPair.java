package it.unibo.bombardero.map.api;

import java.util.function.Function;

import org.apache.commons.math3.util.Pair;

public class GenPair <K, V> extends Pair <K, V> {

    public GenPair(final K k, final V v) {
        super(k, v);
    }

    public K x() {
        return super.getFirst();
    }

    public V y() {
        return super.getSecond();
    }

    public GenPair <K, V> apply(final Function <GenPair<K, V>, GenPair<K, V>> f) {
        return f.apply(this);
    }

    public K reduce(final Function <GenPair<K, V>, K> f) {
        return f.apply(this);
    }
}