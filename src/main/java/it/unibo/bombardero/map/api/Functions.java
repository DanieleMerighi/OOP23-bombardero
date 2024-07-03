package it.unibo.bombardero.map.api;

import java.util.function.Function;

public class Functions {
    
    public static Function <GenPair<Integer, Integer>, GenPair<Integer, Integer>> sumInt(final GenPair<Integer, Integer> p) {
        return (pair) -> new GenPair<>(Integer.sum(pair.x(), p.x()), Integer.sum(pair.y(), p.y()));
    }

    public static Function <GenPair<Integer, Integer>, GenPair<Integer, Integer>> multiplyInt(int scale) {
        return (pair) -> new GenPair<>((pair.x() * scale), (pair.y() * scale));
    }

    public static Function <GenPair<Float, Float>, GenPair<Float, Float>> sumFloat(final GenPair<Float, Float> p) {
        return (pair) -> new GenPair<>(Float.sum(pair.x(), p.x()), Float.sum(pair.y(), p.y()));
    }

    public static Function <GenPair<Float, Float>, GenPair<Float, Float>> multiplyFloat(float scale) {
        return (pair) -> new GenPair<>((pair.x() * scale), (pair.y() * scale));
    }

    public static Function <GenPair<Float, Float>, GenPair<Float, Float>> subtractFloat(final GenPair<Float, Float> p) {
        return (pair) -> new GenPair<>((pair.x() - p.x()), (pair.y() - p.y()));
    }

    public static Function <GenPair<Float, Float>, GenPair<Float, Float>> subtractFloatAndInt(final GenPair<Integer, Integer> p) {
        return (pair) -> new GenPair<>((pair.x() - p.x()), (pair.y() - p.y()));
    }

    public static Function <GenPair<Float, Float>, Float> distanceTo(final GenPair<Float, Float> p) {
        return (pair) -> (float)Math.sqrt(Math.pow(pair.x() - p.x(), 2) + Math.pow(pair.y() - p.y(), 2));
    }
    
}
