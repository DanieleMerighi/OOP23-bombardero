package it.unibo.bombardero.core.api;

import java.util.function.BiPredicate;
import java.util.function.BiConsumer;

import it.unibo.bombardero.map.api.GameMap;

/** 
 * This record represents a guide's step. It is formed by a {@link #condition} 
 * that has to be verified in order to execute the relative {@link #action}.
 * @param condition the condition that has to be satisified in order to execute the {@link #action}
 * @param action the action that is executed once the {@link #condition} is satisfied
 * @author Federico Bagattoni
 */
public record GuideStep(BiPredicate<GameMap, GuideManager> condition, BiConsumer<GuideManager, Controller> action) {
 
}
