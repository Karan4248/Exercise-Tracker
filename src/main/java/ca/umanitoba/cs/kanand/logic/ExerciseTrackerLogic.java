package ca.umanitoba.cs.kanand.logic;

import com.google.common.base.Preconditions;
import ca.umanitoba.cs.kanand.model.*;
import ca.umanitoba.cs.kanand.exceptions.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Application logic layer for the Exercise Tracker.
 * 
 * This class handles business logic, user management, and state coordination
 * between the UI layer and domain model. It is responsible for:
 * - Managing multiple user profiles
 * - Validating user input at the logic layer
 * - Coordinating activities between current user and the grid
 * - Managing the application's global state
 * 
 * Class Invariants:
 *   - users != null
 *   - currentUser ca be null (no logged in user) or a valid User object
 *   - grid can be null (map not initialized) or a valid Grid object
 *   - If currentUser != null, currentUser must be in the users list
 */
public class ExerciseTrackerLogic {
    private final List<User> users;
    private User currentUser;
    private Grid grid;

    /**
     * Initializes the application logic with empty user list and no grid.
     * 
     * Postcondition:
     *   - users list is initialized and empty
     *   - currentUser is null (no user logged in)
     *   - grid is null (map not initialized)
     *   - All invariants are satisfied
     */
    public ExerciseTrackerLogic() {
        this.users = new ArrayList<>();
        this.currentUser = null;
        this.grid = null;

        Preconditions.checkState(checkInvariant(), "Invariant violated at construction");
    }

    /**
     * Creates a new user account with the specified username and password.
     * 
     * Preconditions:
     *   - username != null and !username.isEmpty()
     *   - password != null and !password.isEmpty()
     *   - No user with the given username already exists
     * 
     * Postconditions:
     *   - A new User is created and added to the users list
     *   - currentUser is still the previously logged in user (or null)
     *   - The new user will have an empty Activity
     * 
     * @param username the username for the new account
     * @param password the password for the new account
     * @return the newly created User
     * @throws NullPointerException if username or password is null
     * @throws IllegalStateException if username or password is empty
     * @throws UsernameTakenException if username already exists
     */
    public User createUser(String username, String password) throws UsernameTakenException {
        Preconditions.checkNotNull(username, "Precondition failed: username cannot be null");
        Preconditions.checkState(!username.isEmpty(), "Precondition failed: username cannot be empty");
        Preconditions.checkNotNull(password, "Precondition failed: password cannot be null");
        Preconditions.checkState(!password.isEmpty(), "Precondition failed: password cannot be empty");

        // Check if username already exists
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                throw new UsernameTakenException(
                    "ERROR: Username '" + username + "' is already taken. Please choose a different username.");
            }
        }

        User newUser = new User(username, password);
        users.add(newUser);

        Preconditions.checkState(checkInvariant(), "Invariant violated after createUser");
        Preconditions.checkState(users.contains(newUser), "Postcondition failed: new user not added");

        return newUser;
    }

    /**
     * Authenticates a user and logs them in.
     * 
     * Preconditions:
     *   - username != null
     *   - password != null
     * 
     * Postconditions:
     *   - If credentials are valid, currentUser is set to the authenticated user
     *   - If credentials are invalid, currentUser is unchanged and exception is thrown
     * 
     * @param username the username to authenticate
     * @param password the password to authenticate
     * @return the authenticated User
     * @throws NullPointerException if username or password is null
     * @throws InvalidCredentialsException if username not found or password is incorrect
     */
    public User loginUser(String username, String password) throws InvalidCredentialsException {
        Preconditions.checkNotNull(username, "Precondition failed: username cannot be null");
        Preconditions.checkNotNull(password, "Precondition failed: password cannot be null");

        User foundUser = null;
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                foundUser = user;
                break;
            }
        }

        if (foundUser == null) {
            throw new InvalidCredentialsException(
                "ERROR: Username '" + username + "' not found. Please check your username or create a new account.");
        }

        if (!foundUser.authenticate(password)) {
            throw new InvalidCredentialsException(
                "ERROR: Incorrect password for user '" + username + "'. Please try again.");
        }

        this.currentUser = foundUser;

        Preconditions.checkState(checkInvariant(), "Invariant violated after loginUser");
        Preconditions.checkState(currentUser == foundUser, "Postcondition failed: user not logged in");

        return currentUser;
    }

    /**
     * Logs out the currently logged in user.
     * 
     * Postcondition:
     *   - currentUser is set to null
     *   - All other application state remains unchanged
     */
    public void logoutUser() {
        this.currentUser = null;

        Preconditions.checkState(checkInvariant(), "Invariant violated after logoutUser");
        Preconditions.checkState(currentUser == null, "Postcondition failed: user not logged out");
    }

    /**
     * Gets the currently logged in user.
     * 
     * Postcondition:
     *   - Returns the current user or null if no user is logged in
     * 
     * @return the current User or null
     */
    public User getCurrentUser() {
        Preconditions.checkState(checkInvariant(), "Invariant violated before getCurrentUser");
        return currentUser;
    }

    /**
     * Checks if a user is currently logged in.
     * 
     * Postcondition:
     *   - Returns true if and only if currentUser != null
     * 
     * @return true if a user is logged in, false otherwise
     */
    public boolean isUserLoggedIn() {
        Preconditions.checkState(checkInvariant(), "Invariant violated before isUserLoggedIn");
        return currentUser != null;
    }

    /**
     * Gets all users except the current user.
     * Used for follow/feed functionality.
     * 
     * Postcondition:
     *   - Returns a list of all other users
     *   - If no user is logged in, returns all users
     * 
     * @return a copy list of users that can be followed
     */
    public List<User> getOtherUsers() {
        Preconditions.checkState(checkInvariant(), "Invariant violated before getOtherUsers");
        
        List<User> others = new ArrayList<>();
        for (User user : users) {
            if (currentUser == null || user != currentUser) {
                others.add(user);
            }
        }
        return others;
    }

    /**
     * Initializes or retrieves the grid/map.
     * Creates a new map if one doesn't  exist.
     * 
     * Preconditions:
     *   - width > 0
     *   - height > 0
     * 
     * Postcondition:
     *   - grid is initialized with the specified dimensions
     *   - All previous obstacles and covered points are cleared
     * 
     * @param width the width of the map
     * @param height the height of the map
     * @return the Grid
     * @throws IllegalArgumentException if width or height is not positive
     */
    public Grid initializeMap(int width, int height) {
        Preconditions.checkArgument(width > 0, "Precondition failed: width must be positive");
        Preconditions.checkArgument(height > 0, "Precondition failed: height must be positive");

        this.grid = new Grid(width, height);
        // Reset activity ID counters when creating a new map
        ObstaclePlacement.resetIdCounter();
        ExerciseLog.resetIdCounter();

        Preconditions.checkState(checkInvariant(), "Invariant violated after initializeMap");
        Preconditions.checkState(grid != null, "Postcondition failed: grid not initialized");

        return grid;
    }

    /**
     * Gets the current grid/map.
     * 
     * Precondition:
     *   - Grid must be initialized (call initializeMap first)
     * 
     * @return the Grid
     * @throws MapNotInitializedException if grid has not been initialized
     */
    public Grid getGrid() throws MapNotInitializedException {
        Preconditions.checkState(checkInvariant(), "Invariant violated before getGrid");
        
        if (grid == null) {
            throw new MapNotInitializedException(
                "ERROR: Map has not been initialized. Use 'ADD MAP' to create the map first.");
        }

        return grid;
    }

    /**
     * Removes the current grid/map and all associated data.
     * 
     * Postcondition:
     *   - grid is set to null
     *   - Current user's activities are not cleared (activities are per-user)
     */
    public void removeMap() {
        this.grid = null;

        Preconditions.checkState(checkInvariant(), "Invariant violated after removeMap");
        Preconditions.checkState(grid == null, "Postcondition failed: grid not removed");
    }

    /**
     * Checks if the grid has been initialized.
     * 
     * Postcondition:
     *   - Returns true if grid != null
     * 
     * @return true if the map is initialized, false otherwise
     */
    public boolean isMapInitialized() {
        Preconditions.checkState(checkInvariant(), "Invariant violated before isMapInitialized");
        return grid != null;
    }

    /**
     * Checks the invariant conditions of this class.
     * 
     * @return true if all invariants are satisfied
     */
    private boolean checkInvariant() {
        if (users == null) {
            return false;
        }
        // If currentUser is not null, it must be in the users list
        if (currentUser != null && !users.contains(currentUser)) {
            return false;
        }
        return true;
    }
}
