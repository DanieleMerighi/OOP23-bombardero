package it.unibo.bombardero.guide.api;

import java.util.function.BiPredicate;
import java.util.function.BiConsumer;

import it.unibo.bombardero.core.api.Controller;
import it.unibo.bombardero.map.api.GameMap;

/** 
 * This record represents a guide's step. It is formed by a {@link #condition} 
 * that has to be verified in order to execute the relative {@link #action}.
 * @author Federico Bagattoni
 */
public record GuideStep(BiPredicate<GameMap, GuideManager> condition, BiConsumer<GuideManager, Controller> action) {
    
}
