package it.unibo.bombardero.map.api;

public record Pair(int x, int y) {

    public Pair sum(Pair p1) {
        return new Pair(p1.x + x, p1.y + y);
    }

    public Pair multipy(int m) {
        return new Pair(this.x * m, this.y * m);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + x;
        result = prime * result + y;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Pair other = (Pair) obj;
        if (x != other.x)
            return false;
        if (y != other.y)
            return false;
        return true;
    }
    
}
