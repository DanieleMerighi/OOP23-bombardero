package it.unibo.bombardero.map.api;

import org.apache.commons.math3.util.Pair;

public class Coordinates extends Pair<Float, Float> {

    public Coordinates(float n1, float n2) {
        super(n1, n2);
    }

    public float x() {
        return super.getFirst();
    }

    public float y() {
        return super.getSecond();
    }


    public Coordinates sum(Coordinates p1) {
        return new Coordinates((p1.x() + this.x()), (p1.y() + this.y()));
    }

    public Coordinates subtract(IntPair p1) {
        return new Coordinates((this.x() - p1.x()), (this.y() - p1.y()));
    }

    public Coordinates subtract(Coordinates p1) {
        return new Coordinates((this.x() - p1.x()), (this.y() - p1.y()));
    }

    public Coordinates multiply(float scale) {
        return new Coordinates((this.x() * scale), (this.y() * scale));
    }

    public float distanceTo(Coordinates other) {
        return (float)Math.sqrt(Math.pow(this.x() - other.x(), 2) + Math.pow(this.y() - other.y(), 2));
    }
}
