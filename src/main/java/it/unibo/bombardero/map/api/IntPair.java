package it.unibo.bombardero.map.api;

import org.apache.commons.math3.util.Pair;

public class IntPair extends Pair <Integer, Integer> {

    public IntPair(int n1, int n2) {
        super(n1, n2);
    }

    public int x() {
        return super.getFirst();
    }

    public int y() {
        return super.getSecond();
    }

    public IntPair sum(IntPair p1) {
        return new IntPair((p1.x() + x()), (p1.y() + y()));
    }

    public IntPair multipy(int m) {
        return new IntPair((x() * m), (y() * m));
    }
    
}
