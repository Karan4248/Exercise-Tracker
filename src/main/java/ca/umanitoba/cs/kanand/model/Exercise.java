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
        this.name = Preconditions.checkNotNull(name, "Precondition failed: name cannot be null");
        this.unit = Preconditions.checkNotNull(unit, "Precondition failed: unit cannot be null");
        Preconditions.checkState(!this.name.isEmpty(), "Precondition failed: name cannot be empty");
        Preconditions.checkState(checkInvariant(), "Invariant violated");
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

    /**
     * Checks the class invariant.
     *
     * @return true if the invariant is satisfied, false otherwise
     */
    private boolean checkInvariant() {
        return name != null && !name.isEmpty() && unit != null;
    }

    /**
     * Creates a new builder for Exercise objects.
     *
     * @return a new ExerciseBuilder instance
     */
    public static ExerciseBuilder builder() {
        return new ExerciseBuilder();
    }

    /**
     * Builder class for constructing Exercise objects.
     * Follows the Builder pattern to separate construction logic from domain logic.
     */
    public static class ExerciseBuilder {
        private String name;
        private Unit unit;

        /**
         * Sets the name of the exercise.
         *
         * @param name the exercise name (must be non-empty)
         * @return this builder instance for method chaining
         */
        public ExerciseBuilder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * Sets the unit of measurement for the exercise.
         *
         * @param unit the unit of measurement
         * @return this builder instance for method chaining
         */
        public ExerciseBuilder unit(Unit unit) {
            this.unit = unit;
            return this;
        }

        /**
         * Builds and returns a new Exercise instance with the configured properties.
         *
         * @return a new Exercise object
         * @throws NullPointerException if name or unit is null
         * @throws IllegalStateException if name is empty
         */
        public Exercise build() {
            return new Exercise(this.name, this.unit);
        }
    }
}
