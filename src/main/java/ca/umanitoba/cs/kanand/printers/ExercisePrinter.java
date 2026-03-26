package ca.umanitoba.cs.kanand.printers;

import ca.umanitoba.cs.kanand.model.Exercise;

public final class ExercisePrinter {
    /**
     * Prevents instantiation of this utility class.
     */
    private ExercisePrinter() { }

    /**
     * Formats an Exercise into a readable string representation.
     *
     * @param exercise the Exercise to format
     * @return a formatted string with exercise name and unit
     */
    public static String format(Exercise exercise) {
        if (exercise == null) return "<null exercise>";
        return exercise.getName() + " (" + exercise.getUnit() + ")";
    }
}
