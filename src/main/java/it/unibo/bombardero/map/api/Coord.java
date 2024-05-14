package it.unibo.bombardero.map.api;

public record Coord(float row, float col) {
    public Coord sum(Coord p1) {
        return new Coord(p1.row + row, p1.col + col);
    }

}
