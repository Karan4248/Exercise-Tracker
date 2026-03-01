package ca.umanitoba.cs.kanand.model;

import com.google.common.base.Preconditions;

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
        logs.add(Preconditions.checkNotNull(log, "Precondition failed: log must not be null"));
        Preconditions.checkState(logs.contains(log), "Postcondition failed: logs must contain log");
        Preconditions.checkState(logs != null, "Invariant violated: logs must not be null");
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
        Preconditions.checkState(logs != null, "Invariant violated: logs must not be null");
        return new ArrayList<>(logs);
    }

    public double getTotalDistance(LocalDateTime since) {
        Preconditions.checkNotNull(since, "Precondition failed: since must not be null");

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
        Preconditions.checkState(logs != null, "Invariant violated: logs must not be null");
    }
}
