package it.unibo.bombardero.utils;

import it.unibo.bombardero.map.api.Coord;

import java.util.List;

public class Utils {

    public static final int MAP_ROWS = 13;
    public static final int MAP_COLS = 13;
    /* This number control how much walls to generate in relation to the free space: */
    public static final double WALL_PRESENCE_RATE = 0.8;
    public static final int MAX_RANGE_BOMB = 13;

    public static final Coord PLAYER_SPAWNPOINT = new Coord(0.5f, 0.5f);
    public static final List<Coord> ENEMIES_SPAWNPOINT = List.of(
        new Coord(12.5f, 12.5f),
        new Coord(0.5f, 12.5f)
        ,new Coord(12.5f, 0.5f)
        );
    // Valori per il timer
    public final static long GAME_TIME = 120000l;
    public final static int MAX_WAITING_TIME = 4000;

    // Valori per l'esplosione
    public static final int ENEMY_DETECTION_RADIUS = 4;
    public static final int ENEMY_STARTING_BOMBS = 1;

    // PARAMETRI PER LA VIEW
    public static int MAP_WIDTH = 524; // the original map image's width
    public static int MAP_HEIGHT = 512; // the original map image's height
    public static double GRASS_PADDING_RATIO = 0.2; // the padding that has to be added to the map
    public static int BG_WIDTH = 1920;
    public static int BG_HEIGHT = 1088;
    public final static int CELL_SIZE = 32; // A single cell's size in game-pixels
    public final static int PLAYER_WIDTH = 20; 
    public final static int PLAYER_HEIGHT = 30; 
    public final static int NUM_OF_ENEMIES = 3;
    
}
