package it.unibo.bombardero.map.api;

public record Coord(float row, float col) {
    public Coord sum(Coord p1) {
        return new Coord(Math.round((p1.row + row) * 1000.0f) / 1000.0f,
                Math.round((p1.col + col) * 1000.0f) / 1000.0f);
    }

    public Coord subtract(Pair p1) {
        return new Coord(p1.row() - row, p1.col() - col);
    }

    public Coord multiply(float scale) {
        return new Coord(row * scale, col * scale);
    }
}
