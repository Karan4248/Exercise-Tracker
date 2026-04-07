package ca.umanitoba.cs.kanand.model;

import com.google.common.base.Preconditions;

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
     * Retrieves all exercise logs in a copy of the list.
     *
     * @return a copy of all ExerciseLogs
     */
    public List<ExerciseLog> getAllLogs() {
        Preconditions.checkState(checkInvariant(), "Invariant violated: logs must not be null");
        return new ArrayList<>(logs);
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

