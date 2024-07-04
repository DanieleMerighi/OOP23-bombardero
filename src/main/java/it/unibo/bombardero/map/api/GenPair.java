package it.unibo.bombardero.map.api;

import java.util.function.Function;

public record GenPair <K, V> (K x, V y) {

    public GenPair <K, V> apply(final Function <GenPair<K, V>, GenPair<K, V>> f) {
        return f.apply(this);
    }

    public K reduce(final Function <GenPair<K, V>, K> f) {
        return f.apply(this);
    }
}