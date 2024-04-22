package it.unibo.bombardero.utils;

public class Utils {

    public static final int MAP_ROWS = 13;
    public static final int MAP_COLS = 13;
    /* This number control how much walls to generate in relation to the free space: */
    public static final double WALL_PRESENCE_RATE = 0.8;


    //valori per mappa
    public static final int GRASS = 1;
    public static final int UNBREAKABLE_WALL = 2;
    public static final int WALL = 3;
    public static final int PLAYER = 4;
    public static final int ENEMY = 5;
    public static final int BOMB = 6;
    public static final int EXPLOSION = 7;

    // Valori per il timer
    public final static long GAME_TIME = 120000l;

    // Valori per l'esplosione
    public static final int EXPLOSION_RADIUS = 4;
    public static final int ENEMY_DETECTION_RADIUS = 4;
    public static final int ENEMY_STARTING_BOMBS = 3;
    public static final int ENEMY_SPEED = 1;

    // PARAMETRI PER LA VIEW
    public static int MAP_WIDTH = 524; // the original map image's width
    public static int MAP_HEIGHT = 512; // the original map image's height
    public static double GRASS_PADDING_RATION = 0.2; // the padding that has to be added to the map
    
}
