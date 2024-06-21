package it.unibo.bombardero.map.api;

public record Coord(float x, float y) {

    public Coord sum(Coord p1) {
        return new Coord((p1.x + x),(p1.y + y));
    }

    public Coord subtract(Pair p1) {
        return new Coord(x - p1.x(), y - p1.y());
    }

    public Coord subtract(Coord p1) {
        return new Coord(x - p1.x, y - p1.y);
    }

    public Coord multiply(float scale) {
        return new Coord(x * scale, y * scale);
    }
}
