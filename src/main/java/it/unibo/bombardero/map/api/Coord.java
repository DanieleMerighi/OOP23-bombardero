package it.unibo.bombardero.map.api;

public record Coord(float x, float y) {
    public Coord sum(Coord p1) {
        return new Coord(Math.round((p1.x + x) * 1000.0f) / 1000.0f,
                Math.round((p1.y + y) * 1000.0f) / 1000.0f);
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

    public double distanceTo(Coord other) {
        return Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2));
    }
}
