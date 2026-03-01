package ca.umanitoba.cs.kanand.model;

import com.google.common.base.Preconditions;

/**
 * Represents a type of exercise (e.g., Running, Cycling).
 * Invariant: name != null, name.length > 0, unit != null
 */
public class Exercise {
    private final String name;
    private final Unit unit;

    /**
     * Creates an Exercise with the specified name and unit of measurement.
     *
     * @param name the name of the exercise (non-empty)
     * @param unit the unit of measurement for this exercise
     * @throws NullPointerException if name or unit is null
     * @throws IllegalStateException if name is empty
     */
    public Exercise(String name, Unit unit) {
        this.name = Preconditions.checkNotNull(name, "Invariant failed: name must not be null");
        Preconditions.checkState(!this.name.isEmpty(), "Invariant failed: name length must be > 0");
        this.unit = Preconditions.checkNotNull(unit, "Invariant failed: unit must not be null");

        Preconditions.checkState(this.name != null && !this.name.isEmpty(), "Invariant violated: name");
        Preconditions.checkState(this.unit != null, "Invariant violated: unit");
    }

    /**
     * Gets the name of the exercise.
     *
     * @return the exercise name
     */
    public String getName() { return name; }

    /**
     * Gets the unit of measurement for this exercise.
     *
     * @return the Unit
     */
    public Unit getUnit() { return unit; }

    // ... existing code ...
}
