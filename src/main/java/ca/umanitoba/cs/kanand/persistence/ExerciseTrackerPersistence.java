package ca.umanitoba.cs.kanand.persistence;

import ca.umanitoba.cs.kanand.model.User;
import java.util.List;

/**
 * Interface for persisting and loading Exercise Tracker data.
 * Abstraction allows for different persistence implementations (JSON, XML, etc.).
 */
public interface ExerciseTrackerPersistence {

    /**
     * Saves all user data to persistent storage.
     *
     * Precondition:
     *   - users != null
     *
     * Postcondition:
     *   - All user data including activities, exercise logs, and following
     *     relationships are written to persistent storage
     *
     * @param users the list of users to persist
     */
    void saveUsers(List<User> users);

    /**
     * Loads all user data from persistent storage.
     *
     * Postcondition:
     *   - Returns a list of users with their activities, exercise logs,
     *     and following relationships restored
     *   - If no data exists, returns an empty list
     *   - If data is corrupted, returns an empty list
     *
     * @return the list of users loaded from storage
     */
    List<User> loadUsers();
}
