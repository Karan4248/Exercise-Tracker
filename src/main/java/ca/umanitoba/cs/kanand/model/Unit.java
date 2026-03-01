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

    /**
     * Creates a Unit with the specified abbreviation.
     *
     * @param abbreviation the abbreviation for this unit
     */
    Unit(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    /**
     * Gets the abbreviation for this unit.
     *
     * @return the abbreviation
     */
    public String getAbbreviation() {
        return abbreviation;
    }
}
