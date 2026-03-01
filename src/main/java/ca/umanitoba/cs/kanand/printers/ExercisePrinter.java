package ca.umanitoba.cs.kanand.printers;

import ca.umanitoba.cs.kanand.model.Exercise;

public final class ExercisePrinter {
    private ExercisePrinter() { }

    public static String format(Exercise exercise) {
        if (exercise == null) return "<null exercise>";
        return exercise.getName() + " (" + exercise.getUnit().getAbbreviation() + ")";
    }
}
