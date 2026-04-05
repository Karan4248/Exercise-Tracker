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
     * Changes the username of the current user.
     * 
     * Preconditions:
     *   - currentUser != null (user must be logged in)
     *   - newUsername != null and !newUsername.isEmpty()
     *   - newUsername is not already taken by another user
     * 
     * Postconditions:
     *   - currentUser's username has been changed to newUsername
     * 
     * @param newUsername the new username to set
     * @throws IllegalStateException if no user is logged in
     * @throws NullPointerException if newUsername is null
     * @throws IllegalArgumentException if newUsername is empty or already taken
     */
    public void changeCurrentUserUsername(String newUsername) throws UsernameTakenException {
        Preconditions.checkState(currentUser != null, "Precondition failed: no user logged in");
        Preconditions.checkNotNull(newUsername, "Precondition failed: newUsername cannot be null");
        Preconditions.checkArgument(!newUsername.isEmpty(), "Precondition failed: newUsername cannot be empty");

        for (User user : users) {
            if (user != currentUser && user.getUsername().equals(newUsername)) {
                throw new UsernameTakenException(
                    "ERROR: Username '" + newUsername + "' is already taken. Please choose a different username.");
            }
        }

        currentUser.changeUsername(newUsername);

        Preconditions.checkState(checkInvariant(), "Invariant violated after changeCurrentUserUsername");
    }

    /**
     * Changes the password of the current user.
     * 
     * Preconditions:
     *   - currentUser != null (user must be logged in)
     *   - currentPassword is the correct current password
     *   - newPassword != null and !newPassword.isEmpty()
     * 
     * Postconditions:
     *   - currentUser's password has been changed to newPassword
     * 
     * @param currentPassword the current password for verification
     * @param newPassword the new password to set
     * @return true if password was changed, false if current password is incorrect
     * @throws IllegalStateException if no user is logged in
     * @throws NullPointerException if either password is null
     * @throws IllegalArgumentException if newPassword is empty
     */
    public boolean changeCurrentUserPassword(String currentPassword, String newPassword) {
        Preconditions.checkState(currentUser != null, "Precondition failed: no user logged in");
        Preconditions.checkNotNull(currentPassword, "Precondition failed: currentPassword cannot be null");
        Preconditions.checkNotNull(newPassword, "Precondition failed: newPassword cannot be null");
        Preconditions.checkArgument(!newPassword.isEmpty(), "Precondition failed: newPassword cannot be empty");

        boolean changed = currentUser.changePassword(currentPassword, newPassword);

        if (changed) {
            Preconditions.checkState(checkInvariant(), "Invariant violated after changeCurrentUserPassword");
        }

        return changed;
    }

    /**
     * Adds an exercise log to the current user's activity.
     * 
     * Preconditions:
     *   - currentUser != null (user must be logged in)
     *   - log != null
     * 
     * Postconditions:
     *   - The exercise log has been added to currentUser's activity
     * 
     * @param log the exercise log to add
     * @throws IllegalStateException if no user is logged in
     * @throws NullPointerException if log is null
     */
    public void addExerciseLogToCurrentUser(ExerciseLog log) {
        Preconditions.checkState(currentUser != null, "Precondition failed: no user logged in");
        Preconditions.checkNotNull(log, "Precondition failed: log cannot be null");

        currentUser.getActivity().addExerciseLog(log);

        Preconditions.checkState(checkInvariant(), "Invariant violated after addExerciseLogToCurrentUser");
    }

    /**
     * Gets all exercise logs from the current user's followed users.
     * This creates the activity feed based on who the current user is following.
     * 
     * Preconditions:
     *   - currentUser != null (user must be logged in)
     * 
     * Postconditions:
     *   - Returns a list containing all activities from followed users
     *   - The list does not include activities from the current user
     * 
     * @return a list of all exercise logs from followed users
     * @throws IllegalStateException if no user is logged in
     */
    public List<ExerciseLog> getActivityFeed() {
        Preconditions.checkState(currentUser != null, "Precondition failed: no user logged in");

        List<ExerciseLog> feed = new ArrayList<>();
        List<User> following = currentUser.getFollowing();

        for (User user : following) {
            feed.addAll(user.getActivity().getAllLogs());
        }

        return feed;
    }

    /**
     * Gets all previous routes of the current user.
     * Used for duplicating routes when adding a new activity.
     * 
     * Preconditions:
     *   - currentUser != null (user must be logged in)
     * 
     * Postconditions:
     *   - Returns a list of all previous exercise logs from the current user
     * 
     * @return a list of the current user's previous exercise logs
     * @throws IllegalStateException if no user is logged in
     */
    public List<ExerciseLog> getCurrentUserPreviousRoutes() {
        Preconditions.checkState(currentUser != null, "Precondition failed: no user logged in");
        return currentUser.getActivity().getAllLogs();
    }

    /**
     * Gets all covered routes for pathfinding from all followed users (and self).
     * Used for pathfinding with "all routes" scope.
     * 
     * Preconditions:
     *   - currentUser != null (user must be logged in)
     * 
     * Postconditions:
     *   - Returns a list of all exercise logs from current user and followed users
     * 
     * @return a list of exercise logs for global pathfinding scope
     * @throws IllegalStateException if no user is logged in
     */
    public List<ExerciseLog> getAllCoveredRoutes() {
        Preconditions.checkState(currentUser != null, "Precondition failed: no user logged in");

        List<ExerciseLog> allRoutes = new ArrayList<>();
        allRoutes.addAll(currentUser.getActivity().getAllLogs());
        allRoutes.addAll(getActivityFeed());

        return allRoutes;
    }

    /**
     * Finds a path between two points using the pathfinding algorithm.
     * 
     * Preconditions:
     *   - grid != null (map must be initialized)
     *   - start != null
     *   - end != null
     *   - scope is one of PERSONAL_ROUTES or ALL_ROUTES
     * 
     * Postconditions:
     *   - If a path exists, returns the list of points from start to end
     *   - If no path exists, returns null
     *   - Grid state is unchanged
     * 
     * @param start the starting point
     * @param end the ending point
     * @param scope the scope for pathfinding (PERSONAL_ROUTES or ALL_ROUTES)
     * @return a list of points representing the path, or null if no path exists
     * @throws MapNotInitializedException if map is not initialized
     * @throws NullPointerException if start, end, or scope is null
     * @throws InvalidPathException if pathfinding fails due to grid state
     */
    public List<Point> findPath(Point start, Point end, RouteScope scope) 
            throws MapNotInitializedException, InvalidPathException {
        if (grid == null) {
            throw new MapNotInitializedException(
                "ERROR: Map has not been initialized. Please initialize the map first.");
        }

        Preconditions.checkNotNull(start, "Precondition failed: start cannot be null");
        Preconditions.checkNotNull(end, "Precondition failed: end cannot be null");
        Preconditions.checkNotNull(scope, "Precondition failed: scope cannot be null");

        PathFinder pathFinder = new PathFinder(grid, scope);
        return pathFinder.findPath(start, end);
    }

    /**
     * Checks the invariant conditions of this class.
     * 
     * @return true if all invariants are satisfied
     */
    private boolean checkInvariant() {
        if (users == null) return false;
        if (currentUser != null && !users.contains(currentUser)) return false;
        return true;
    }
}
