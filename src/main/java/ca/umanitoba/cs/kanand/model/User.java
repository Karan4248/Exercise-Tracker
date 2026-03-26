package ca.umanitoba.cs.kanand.model;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a user profile for the Exercise Tracker application.
 * Supports multiple people using the application with separate activity logs and social feeds.
 * 
 * Class Invariants:
 *   - username != null and !username.isEmpty()
 *   - password != null and !password.isEmpty()
 *   - activity != null
 *   - following != null
 *   - Each user in 'following' list is a distinct, valid User object
 *   - A user cannot follow themselves
 */
public class User {
    private String username;
    private String password;
    private final Activity activity;
    private final List<User> following;

    /**
     * Creates a User with the specified username and password.
     * 
     * Preconditions:
     *   - username != null and !username.isEmpty()
     *   - password != null and !password.isEmpty()
     * 
     * Postconditions:
     *   - The new user has the specified username and password
     *   - The activity container is initialized and empty
     *   - The following list is initialized and empty
     *   - All invariants are satisfied
     * 
     * @param username the username for this account (must be non-empty)
     * @param password the password for this account (must be non-empty)
     * @throws NullPointerException if username or password is null
     * @throws IllegalStateException if username or password is empty
     */
    public User(String username, String password) {
        this.username = Preconditions.checkNotNull(username, "Precondition failed: username cannot be null");
        Preconditions.checkState(!this.username.isEmpty(), "Precondition failed: username cannot be empty");
        
        this.password = Preconditions.checkNotNull(password, "Precondition failed: password cannot be null");
        Preconditions.checkState(!this.password.isEmpty(), "Precondition failed: password cannot be empty");
        
        this.activity = new Activity();
        this.following = new ArrayList<>();

        Preconditions.checkState(checkInvariant(), "Invariant violated: user state is invalid");
    }

    /**
     * Gets the username of this user.
     * 
     * Postcondition:
     *   - The returned username is non-null and non-empty
     * 
     * @return the username
     */
    public String getUsername() {
        Preconditions.checkState(checkInvariant(), "Invariant violated before getUsername()");
        return username;
    }

    /**
     * Checks if the provided password matches this user's password.
     * 
     * Precondition:
     *   - password != null
     * 
     * Postcondition:
     *   - Returns true if and only if the provided password matches this user's password
     *   - The user's password is unchanged
     * 
     * @param password the password to check
     * @return true if the password is correct, false otherwise
     * @throws NullPointerException if password is null
     */
    public boolean authenticate(String password) {
        Preconditions.checkNotNull(password, "Precondition failed: password cannot be null");
        Preconditions.checkState(checkInvariant(), "Invariant violated before authenticate()");
        
        return this.password.equals(password);
    }

    /**
     * Changes the user's password to a new value.
     * 
     * Precondition:
     *   - currentPassword != null and authenticate(currentPassword) == true
     *   - newPassword != null and !newPassword.isEmpty()
     * 
     * Postcondition:
     *   - The user's password has been changed to newPassword
     *   - Subsequent calls to authenticate(newPassword) return true
     * 
     * @param currentPassword the user's current password for verification
     * @param newPassword the new password to set
     * @return true if the password was changed successfully, false if current password is incorrect
     * @throws NullPointerException if either parameter is null
     * @throws IllegalStateException if newPassword is empty
     */
    public boolean changePassword(String currentPassword, String newPassword) {
        Preconditions.checkNotNull(currentPassword, "Precondition failed: currentPassword cannot be null");
        Preconditions.checkNotNull(newPassword, "Precondition failed: newPassword cannot be null");
        Preconditions.checkState(!newPassword.isEmpty(), "Precondition failed: newPassword cannot be empty");
        
        if (!authenticate(currentPassword)) {
            return false;
        }

        this.password = newPassword;

        Preconditions.checkState(checkInvariant(), "Invariant violated after changePassword()");
        Preconditions.checkState(authenticate(newPassword), "Postcondition failed: new password not set correctly");

        return true;
    }

    /**
     * Changes the user's username to a new value.
     * This method does not validate uniqueness - the caller must ensure the new username is not taken.
     * 
     * Precondition:
     *   - newUsername != null and !newUsername.isEmpty()
     * 
     * Postcondition:
     *   - The user's username has been changed to newUsername
     * 
     * @param newUsername the new username to set
     * @throws NullPointerException if newUsername is null
     * @throws IllegalStateException if newUsername is empty
     */
    public void changeUsername(String newUsername) {
        Preconditions.checkNotNull(newUsername, "Precondition failed: newUsername cannot be null");
        Preconditions.checkState(!newUsername.isEmpty(), "Precondition failed: newUsername cannot be empty");

        this.username = newUsername;

        Preconditions.checkState(checkInvariant(), "Invariant violated after changeUsername()");
    }

    /**
     * Gets the user's activity container with all their exercise logs.
     * 
     * Postcondition:
     *   - The returned Activity is non-null
     *   - The returned Activity contains all of this user's exercise logs
     * 
     * @return the user's Activity
     */
    public Activity getActivity() {
        Preconditions.checkState(checkInvariant(), "Invariant violated before getActivity()");
        return activity;
    }

    /**
     * Adds a user to this user's following list.
     * A user cannot follow themselves.
     * 
     * Precondition:
     *   - user != null
     *   - user != this (cannot follow self)
     * 
     * Postcondition:
     *   - The user is added to the following list
     *   - getFollowing().contains(user) is true
     *   - Existing followers remain in the list
     * 
     * @param user the user to follow
     * @throws NullPointerException if user is null
     * @throws IllegalArgumentException if trying to follow self
     */
    public void addFollowing(User user) {
        Preconditions.checkNotNull(user, "Precondition failed: user cannot be null");
        Preconditions.checkArgument(user != this, "Precondition failed: cannot follow yourself");

        if (!following.contains(user)) {
            following.add(user);
        }

        Preconditions.checkState(checkInvariant(), "Invariant violated after addFollowing()");
        Preconditions.checkState(following.contains(user), "Postcondition failed: user not added to following list");
    }

    /**
     * Removes a user from this user's following list.
     * 
     * Precondition:
     *   - user != null
     * 
     * Postcondition:
     *   - The user is removed from the following list
     *   - getFollowing().contains(user) is false
     *   - Other followers remain in the list
     * 
     * @param user the user to unfollow
     * @return true if the user was removed, false if they were not in the list
     * @throws NullPointerException if user is null
     */
    public boolean removeFollowing(User user) {
        Preconditions.checkNotNull(user, "Precondition failed: user cannot be null");

        boolean removed = following.remove(user);

        Preconditions.checkState(checkInvariant(), "Invariant violated after removeFollowing()");
        if (removed) {
            Preconditions.checkState(!following.contains(user), "Postcondition failed: user still in following list");
        }

        return removed;
    }

    /**
     * Checks if this user is following another user.
     * 
     * Precondition:
     *   - user != null
     * 
     * Postcondition:
     *   - Returns true if following this user, false otherwise
     *   - The following list is unchanged
     * 
     * @param user the user to check
     * @return true if this user follows the given user, false otherwise
     * @throws NullPointerException if user is null
     */
    public boolean isFollowing(User user) {
        Preconditions.checkNotNull(user, "Precondition failed: user cannot be null");
        Preconditions.checkState(checkInvariant(), "Invariant violated before isFollowing()");
        
        return following.contains(user);
    }

    /**
     * Gets a copy of the list of users this user is following.
     * 
     * Postcondition:
     *   - The returned list is a copy (modifications don't affect the user's following list)
     *   - The returned list contains all users currently being followed
     * 
     * @return a copy of the following list
     */
    public List<User> getFollowing() {
        Preconditions.checkState(checkInvariant(), "Invariant violated before getFollowing()");
        return new ArrayList<>(following);
    }

    /**
     * Checks the class invariant.
     * 
     * @return true if all invariant conditions are satisfied, false otherwise
     */
    private boolean checkInvariant() {
        if (username == null || username.isEmpty()) {
            return false;
        }
        if (password == null || password.isEmpty()) {
            return false;
        }
        if (activity == null) {
            return false;
        }
        if (following == null) {
            return false;
        }
        // Check that no user is following themselves
        for (User u : following) {
            if (u == this) {
                return false;
            }
        }
        return true;
    }

    /**
     * Compares this user with another object for equality.
     * Users are equal if they have the same username.
     * 
     * @param obj the object to compare with
     * @return true if the objects represent the same user, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return username.equals(user.username);
    }

    /**
     * Generates a hash code for this user based on username.
     * 
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return username.hashCode();
    }
}
