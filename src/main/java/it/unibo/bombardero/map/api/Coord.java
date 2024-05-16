package it.unibo.bombardero.map.api;

public record Coord(float row, float col) {
    public Coord sum(Coord p1) {
        return new Coord(p1.row + row, p1.col + col);
    }

    public Coord subtract(Pair p1) {
        return new Coord(p1.row() - row, p1.col() - col);
    }

    public Coord multiply(float scale) {
        return new Coord(row * scale, col * scale);
    }
}
