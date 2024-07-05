package it.unibo.bombardero.character.ai.api;

/**
 * This abstract class provides default implementations for equals and hashCode methods for all enemy states.
 */
public abstract class AbstractEnemyState implements EnemyState {
    
    /**
     * Determines whether the specified object is equal to this state.
     * The comparison is based on the class type of the objects.
     * 
     * @param obj the object to compare with
     * @return true if the specified object is of the same class as this state; false otherwise
     */
    @Override
    public boolean equals(final Object obj) {
        return obj != null && this.getClass() == obj.getClass();
    }

    /**
     * Returns the hash code value for this state.
     * The hash code is a constant value, ensuring that all instances of the same state class have the same hash code.
     * 
     * @return the hash code value, which is a constant 31
     */
    @Override
    public int hashCode() {
        return 31; // arbitrary constant value
    }

}
