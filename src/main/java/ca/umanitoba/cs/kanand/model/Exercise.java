package ca.umanitoba.cs.kanand.model;

import com.google.common.base.Preconditions;

/**
 * Represents a type of exercise (e.g., Running, Cycling).
 * Invariant: name != null, name.length > 0, unit != null
 */
public class Exercise {
    private final String name;
    private final Unit unit;

    public Exercise(String name, Unit unit) {
        this.name = Preconditions.checkNotNull(name, "Invariant failed: name must not be null");
        Preconditions.checkState(!this.name.isEmpty(), "Invariant failed: name length must be > 0");
        this.unit = Preconditions.checkNotNull(unit, "Invariant failed: unit must not be null");

        Preconditions.checkState(this.name != null && !this.name.isEmpty(), "Invariant violated: name");
        Preconditions.checkState(this.unit != null, "Invariant violated: unit");
    }

    public String getName() { return name; }
    public Unit getUnit() { return unit; }

    // ... existing code ...
}
