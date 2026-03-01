package ca.umanitoba.cs.kanand.model;

/**
 * Enumeration of distance units for exercises.
 */
public enum Unit {
    KILOMETERS("km"),
    MILES("mi"),
    METERS("m"),
    STEPS("steps");

    private final String abbreviation;

    Unit(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getAbbreviation() {
        return abbreviation;
    }
}
