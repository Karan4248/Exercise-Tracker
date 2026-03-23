package ca.umanitoba.cs.kanand.model;

import java.time.format.DateTimeFormatter;

public final class ExerciseLogPrinter {
    private static final DateTimeFormatter DATE = DateTimeFormatter.ISO_LOCAL_DATE;

    /**
     * Prevents instantiation of this utility class.
     */
    private ExerciseLogPrinter() { }

    /**
     * Formats an ExerciseLog into a readable string representation.
     *
     * @param log the ExerciseLog to format
     * @return a formatted string with activity details
     */
    public static String format(ExerciseLog log) {
        if (log == null) return "<null activity>";
        return String.format(
                "Activity #%d: \"%s\" - %s, %.2f %s, %d points, %s",
                log.getId(),
                log.getName(),
                log.getExercise().getName(),
                log.getDistance(),
                log.getExercise().getUnit(),
                log.getPoints().size(),
                log.getTimestamp().toLocalDate().format(DATE)
        );
    }
}
