package it.unibo.bombardero.utils;

import java.util.List;

import it.unibo.bombardero.map.api.GenPair;

public class Utils {

    public static final int MAP_ROWS = 13;
    public static final int MAP_COLS = 13;
    /*
     * This number control how much walls to generate in relation to the free space:
     */
    public static final double WALL_PRESENCE_RATE = 0.8;
    public static final int MAX_RANGE_BOMB = 13;

    public static final GenPair<Float, Float> PLAYER_SPAWNPOINT = new GenPair<>(0.5f, 0.5f);
    public static final List<GenPair<Float, Float>> ENEMIES_SPAWNPOINT = List.of(
            new GenPair<Float, Float>(12.5f, 12.5f),
            new GenPair<Float, Float>(0.5f, 12.5f), new GenPair<Float, Float>(12.5f, 0.5f));
    // timers value
    public static final long GAME_TIME = 120_000L;
    public static final int MAX_WAITING_TIME = 2000;

    // explosion value
    public static final int ENEMY_DETECTION_RADIUS = 4;
    public static final int ENEMY_STARTING_BOMBS = 1;

    // view value
    public static final int MAP_WIDTH = 524; // the original map image's width
    public static final int MAP_HEIGHT = 512; // the original map image's height
    public static final double GRASS_PADDING_RATIO = 0.2; // the padding that has to be added to the map
    public static final int BG_WIDTH = 1920;
    public static final int BG_HEIGHT = 1088;
    public static final int CELL_SIZE = 32; // A single cell's size in game-pixels
    public static final int PLAYER_WIDTH = 20;
    public static final int PLAYER_HEIGHT = 30;
    public static final int NUM_OF_ENEMIES = 3;

}
