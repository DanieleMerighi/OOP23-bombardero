package it.unibo.bombardero.map.api;

public record Pair(int row, int col) {

    public Pair sum(Pair p1) {
        return new Pair(p1.row + row, p1.col + col);
    }

    public Pair multipy(int m) {
        return new Pair(this.row * m, this.col * m);
    }
}
