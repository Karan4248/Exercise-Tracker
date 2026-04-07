package ca.umanitoba.cs.kanand.test;

import ca.umanitoba.cs.kanand.model.Activity;
import ca.umanitoba.cs.kanand.model.User;

/**
 * Test suite for User class following COMP 2450 class methodology.
 * 
 * Tests domain behavior:
 * - Valid user creation with different credentials
 * - Authentication (correct/incorrect passwords)
 * - Profile modifications (username, password changes)
 * - Follow/unfollow functionality
 * - Activity container management
 * - Exception handling using try-catch
 * 
 * Uses TestResults to track pass/fail. NO assertion statements.
 */
public class UserTest {
    private TestResults results = new TestResults();

    public static void main(String[] args) {
        UserTest tests = new UserTest();
        tests.runAllTests();
    }

    public void runAllTests() {
        System.out.println("\n╔" + "═".repeat(58) + "╗");
        System.out.println("║" + " ".repeat(24) + "User Test Suite" + " ".repeat(20) + "║");
        System.out.println("╚" + "═".repeat(58) + "╝\n");

        testUserCreation();
        testAuthenticate();
        testAuthenticateIncorrect();
        testChangePassword();
        testChangePasswordIncorrect();
        testChangeUsername();
        testAddFollowing();
        testRemoveFollowing();
        testIsFollowing();
        testCannotFollowSelf();
        testActivityContainer();
        testGetFollowing();

        printSummary();
    }

    private void testUserCreation() {
        try {
            User user = new User("alice", "pass123");
            if (user.getUsername().equals("alice")) {
                results.pass("User creation with valid credentials");
            } else {
                results.fail("User creation with valid credentials");
            }
        } catch (Exception e) {
            results.fail("Unexpected exception in testUserCreation: " + e.getMessage());
        }
    }

    private void testAuthenticate() {
        try {
            User user = new User("bob", "secure_pwd");
            if (user.authenticate("secure_pwd")) {
                results.pass("Authenticate with correct password returns true");
            } else {
                results.fail("Authenticate with correct password returns true");
            }
        } catch (Exception e) {
            results.fail("Unexpected exception in testAuthenticate: " + e.getMessage());
        }
    }

    private void testAuthenticateIncorrect() {
        try {
            User user = new User("charlie", "mypass");
            if (!user.authenticate("wrongpassword")) {
                results.pass("Authenticate with incorrect password returns false");
            } else {
                results.fail("Authenticate with incorrect password returns false");
            }
        } catch (Exception e) {
            results.fail("Unexpected exception in testAuthenticateIncorrect: " + e.getMessage());
        }
    }

    private void testChangePassword() {
        try {
            User user = new User("alice", "pass123");
            String newPassword = "newpass123";
            
            boolean changed = user.changePassword("pass123", newPassword);
            if (changed && user.authenticate(newPassword) && !user.authenticate("pass123")) {
                results.pass("Change password updates credentials");
            } else {
                results.fail("Change password updates credentials");
            }
        } catch (Exception e) {
            results.fail("Unexpected exception in testChangePassword: " + e.getMessage());
        }
    }

    private void testChangePasswordIncorrect() {
        try {
            User user = new User("bob", "secure_pwd");
            
            boolean changed = user.changePassword("wrongoldpass", "newpass");
            if (!changed && user.authenticate("secure_pwd")) {
                results.pass("Change password fails with incorrect old password");
            } else {
                results.fail("Change password fails with incorrect old password");
            }
        } catch (Exception e) {
            results.fail("Unexpected exception in testChangePasswordIncorrect: " + e.getMessage());
        }
    }

    private void testChangeUsername() {
        try {
            User user = new User("charlie", "mypass");
            String newUsername = "newuser";
            
            user.changeUsername(newUsername);
            if (user.getUsername().equals(newUsername)) {
                results.pass("Change username updates username");
            } else {
                results.fail("Change username updates username");
            }
        } catch (Exception e) {
            results.fail("Unexpected exception in testChangeUsername: " + e.getMessage());
        }
    }

    private void testAddFollowing() {
        try {
            User user1 = new User("alice", "pass123");
            User user2 = new User("bob", "secure_pwd");
            
            user1.addFollowing(user2);
            if (user1.isFollowing(user2)) {
                results.pass("Add following adds user to following list");
            } else {
                results.fail("Add following adds user to following list");
            }
        } catch (Exception e) {
            results.fail("Unexpected exception in testAddFollowing: " + e.getMessage());
        }
    }

    private void testRemoveFollowing() {
        try {
            User user1 = new User("alice", "pass123");
            User user2 = new User("bob", "secure_pwd");
            
            user1.addFollowing(user2);
            boolean removed = user1.removeFollowing(user2);
            
            if (removed && !user1.isFollowing(user2)) {
                results.pass("Remove following removes user from following list");
            } else {
                results.fail("Remove following removes user from following list");
            }
        } catch (Exception e) {
            results.fail("Unexpected exception in testRemoveFollowing: " + e.getMessage());
        }
    }

    private void testIsFollowing() {
        try {
            User user1 = new User("alice", "pass123");
            User user2 = new User("bob", "secure_pwd");
            User user3 = new User("charlie", "mypass");
            
            user1.addFollowing(user2);
            
            if (user1.isFollowing(user2) && !user1.isFollowing(user3)) {
                results.pass("isFollowing correctly identifies followed users");
            } else {
                results.fail("isFollowing correctly identifies followed users");
            }
        } catch (Exception e) {
            results.fail("Unexpected exception in testIsFollowing: " + e.getMessage());
        }
    }

    private void testCannotFollowSelf() {
        try {
            User user = new User("alice", "pass123");
            
            try {
                user.addFollowing(user);
                results.fail("Cannot follow self - should throw exception");
            } catch (IllegalArgumentException e) {
                if (!user.getFollowing().contains(user)) {
                    results.pass("Cannot follow self - throws IllegalArgumentException");
                } else {
                    results.fail("Cannot follow self - user in following list");
                }
            }
        } catch (Exception e) {
            results.fail("Unexpected outer exception in testCannotFollowSelf: " + e.getMessage());
        }
    }

    private void testActivityContainer() {
        try {
            User user = new User("alice", "pass123");
            Activity activity = user.getActivity();
            
            if (activity != null && activity.getAllLogs().isEmpty()) {
                results.pass("User has empty activity container on creation");
            } else {
                results.fail("User has empty activity container on creation");
            }
        } catch (Exception e) {
            results.fail("Unexpected exception in testActivityContainer: " + e.getMessage());
        }
    }

    private void testGetFollowing() {
        try {
            User user1 = new User("alice", "pass123");
            User user2 = new User("bob", "secure_pwd");
            User user3 = new User("charlie", "mypass");
            
            user1.addFollowing(user2);
            user1.addFollowing(user3);
            
            java.util.List<User> following = user1.getFollowing();
            if (following.size() == 2 && following.contains(user2) && following.contains(user3)) {
                results.pass("getFollowing returns all followed users");
            } else {
                results.fail("getFollowing returns all followed users");
            }
        } catch (Exception e) {
            results.fail("Unexpected exception in testGetFollowing: " + e.getMessage());
        }
    }

    private void printSummary() {
        System.out.println("\n" + "─".repeat(60));
        System.out.printf("User: %d passed, %d failed out of %d tests%n", 
            results.successes(), results.failures(), results.totalTests());
        System.out.println("─".repeat(60) + "\n");
    }
}
