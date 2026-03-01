package ca.umanitoba.cs.kanand;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Container for all exercise logs (activities).
 * Invariant: logs != null
 */
public class Activity {
    private final List<ExerciseLog> logs;

    public Activity() {
        this.logs = new ArrayList<>();
    }

    public void addExerciseLog(ExerciseLog log) {
        if (log == null) {
            throw new IllegalArgumentException("Log cannot be null");
        }
        logs.add(log);
    }

    public boolean removeExerciseLog(int id) {
        return logs.removeIf(log -> log.getId() == id);
    }

    public ExerciseLog getExerciseLog(int id) {
        for (ExerciseLog log : logs) {
            if (log.getId() == id) {
                return log;
            }
        }
        return null;
    }

    public List<ExerciseLog> getAllLogs() {
        return new ArrayList<>(logs);
    }

    public double getTotalDistance(LocalDateTime since) {
        double total = 0;
        for (ExerciseLog log : logs) {
            if (log.getTimestamp().isAfter(since)) {
                total += log.getDistance();
            }
        }
        return total;
    }

    public double getLifetimeDistance() {
        double total = 0;
        for (ExerciseLog log : logs) {
            total += log.getDistance();
        }
        return total;
    }

    public void clear() {
        logs.clear();
    }
}
