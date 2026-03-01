package ca.umanitoba.cs.kanand.printers;

import ca.umanitoba.cs.kanand.model.ExerciseLog;

import java.time.format.DateTimeFormatter;

public final class ExerciseLogPrinter {
    private static final DateTimeFormatter DATE = DateTimeFormatter.ISO_LOCAL_DATE;

    private ExerciseLogPrinter() { }

    public static String format(ExerciseLog log) {
        if (log == null) return "<null activity>";
        return String.format(
                "Activity #%d: \"%s\" - %s, %.2f %s, %d points, %s",
                log.getId(),
                log.getName(),
                log.getExercise().getName(),
                log.getDistance(),
                log.getExercise().getUnit().getAbbreviation(),
                log.getPoints().size(),
                log.getTimestamp().toLocalDate().format(DATE)
        );
    }
}
