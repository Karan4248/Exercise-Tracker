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

    /**
     * Initializes an empty Activity container.
     */
    public Activity() {
        this.logs = new ArrayList<>();
        Preconditions.checkState(checkInvariant(), "Invariant violated: logs must not be null");
    }

    /**
     * Adds an exercise log to the activity collection.
     *
     * @param log the ExerciseLog to add
     * @throws NullPointerException if log is null
     */
    public void addExerciseLog(ExerciseLog log) {
        Preconditions.checkNotNull(log, "Precondition failed: log must not be null");
        logs.add(log);
        Preconditions.checkState(logs.contains(log), "Postcondition failed: logs must contain log");
        Preconditions.checkState(checkInvariant(), "Invariant violated: logs must not be null");
    }

    /**
     * Removes an exercise log by its ID.
     *
     * @param id the ID of the ExerciseLog to remove
     * @return true if a log was removed, false otherwise
     */
    public boolean removeExerciseLog(int id) {
        boolean removed = logs.removeIf(log -> log.getId() == id);
        Preconditions.checkState(checkInvariant(), "Invariant violated: logs must not be null");
        return removed;
    }

    /**
     * Retrieves an exercise log by its ID.
     *
     * @param id the ID of the ExerciseLog to retrieve
     * @return the ExerciseLog with the given ID, or null if not found
     */
    public ExerciseLog getExerciseLog(int id) {
        for (ExerciseLog log : logs) {
            if (log.getId() == id) {
                return log;
            }
        }
        return null;
    }

    /**
     * Retrieves all exercise logs in a copy of the list.
     *
     * @return a copy of all ExerciseLogs
     */
    public List<ExerciseLog> getAllLogs() {
        Preconditions.checkState(checkInvariant(), "Invariant violated: logs must not be null");
        return new ArrayList<>(logs);
    }

    /**
     * Calculates the total distance covered by all exercises since a given date/time.
     *
     * @param since the LocalDateTime threshold
     * @return the total distance of all logs after the given time
     * @throws NullPointerException if since is null
     */
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

    /**
     * Calculates the total distance covered across all recorded exercises.
     *
     * @return the lifetime distance total
     */
    public double getLifetimeDistance() {
        double total = 0;
        for (ExerciseLog log : logs) {
            total += log.getDistance();
        }
        return total;
    }

    /**
     * Clears all exercise logs from the activity.
     */
    public void clear() {
        logs.clear();
        Preconditions.checkState(checkInvariant(), "Invariant violated: logs must not be null");
    }

    /**
     * Checks the class invariant.
     *
     * @return true if the invariant is satisfied, false otherwise
     */
    private boolean checkInvariant() {
        return logs != null;
    }
}

