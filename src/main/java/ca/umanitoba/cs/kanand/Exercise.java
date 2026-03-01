
package ca.umanitoba.cs.kanand;

/**
 * Represents a type of exercise (e.g., Running, Cycling).
 * Invariant: name != null, name.length > 0, unit != null
 */
public class Exercise {
    private final String name;
    private final Unit unit;

    public Exercise(String name, Unit unit) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Exercise name cannot be null or empty");
        }
        if (unit == null) {
            throw new IllegalArgumentException("Unit cannot be null");
        }
        this.name = name;
        this.unit = unit;
    }

    public String getName() { return name; }
    public Unit getUnit() { return unit; }

    @Override
    public String toString() {
        return name + " (" + unit.getAbbreviation() + ")";
    }
}
