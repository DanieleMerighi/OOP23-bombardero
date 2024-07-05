package it.unibo.bombardero.utils;

import java.util.List;

import it.unibo.bombardero.map.api.GenPair;

/**
 * Utility class containing constants and utility methods for the game.
 */
public final class Utils {

        /** Number of rows in the map. */
        public static final int MAP_ROWS = 13;
        /** Number of columns in the map. */
        public static final int MAP_COLS = 13;
        /** This number control how much walls to generate in relation to the free space. */
        public static final double WALL_PRESENCE_RATE = 0.8;
         /** Maximum range of bombs. */
        public static final int MAX_RANGE_BOMB = 13;
        /** Initial spawn point of the player. */
        public static final GenPair<Float, Float> PLAYER_SPAWNPOINT = new GenPair<>(0.5f, 0.5f);
         /** Initial spawn points of enemies. */
        public static final List<GenPair<Float, Float>> ENEMIES_SPAWNPOINT = List.of(
                new GenPair<Float, Float>(12.5f, 12.5f),
                new GenPair<Float, Float>(0.5f, 12.5f), new GenPair<Float, Float>(12.5f, 0.5f));
        /** Game time in milliseconds. */
        public static final long GAME_TIME = 120_000L;
        /** Maximum waiting time in milliseconds. */
        public static final int MAX_WAITING_TIME = 2000;
        /** Detection radius for enemies. */
        public static final int ENEMY_DETECTION_RADIUS = 4;
        /** Initial numbers of bombs for enemies. */
        public static final int ENEMY_STARTING_BOMBS = 1;
        /** Width of the map in pixels. */
        public static final int MAP_WIDTH = 524; 
        /** Height of the map in pixels. */
        public static final int MAP_HEIGHT = 512;
        /** The padding that has to be added to the map. */
        public static final double GRASS_PADDING_RATIO = 0.2;
        /** Background width in pixels. */
        public static final int BG_WIDTH = 1920;
        /** Background height in pixels. */
        public static final int BG_HEIGHT = 1088;
        /** Size of a single cell in game-pixels. */
        public static final int CELL_SIZE = 32; 
        /** Width of the player in pixels. */
        public static final int PLAYER_WIDTH = 20;
        /** Height of the player in pixels. */
        public static final int PLAYER_HEIGHT = 30;
        /** Number of enemies in the game. */
        public static final int NUM_OF_ENEMIES = 3;

    private Utils() {

    }
}
