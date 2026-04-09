package ca.umanitoba.cs.kanand.logic;

import ca.umanitoba.cs.kanand.exceptions.*;
import ca.umanitoba.cs.kanand.model.*;
import ca.umanitoba.cs.kanand.persistence.ExerciseTrackerPersistence;
import ca.umanitoba.cs.kanand.test.TestResults;
import ca.umanitoba.cs.kanand.test.TestSuite;

import java.util.ArrayList;
import java.util.List;

public class ExerciseTrackerLogicTest implements TestSuite {
    private final TestResults results = new TestResults();

    @Override
    public String name() {
        return "Tests for ExerciseTrackerLogic Class";
    }

    @Override
    public TestResults runTests() {

        testCreateUser();
        testCreateUserDuplicateUsername();
        testLoginUser();
        testLoginUserInvalidCredentials();
        testLogoutUser();
        testChangeUsername();
        testChangeUsernameTaken();
        testChangePassword();
        testChangePasswordWrongCurrent();
        testInitializeMap();
        testRemoveMap();
        testGetGridBeforeInit();
        testAddExerciseLog();
        testGetActivityFeed();
        testGetOtherUsers();

        return results;
    }

    /**
     * Helper: creates a fresh logic instance with the stub persistence.
     */
    private ExerciseTrackerLogic createLogic() {
        return new ExerciseTrackerLogic(new StubPersistence());
    }

    // ── User creation ──────────────────────────────────────────────

    private void testCreateUser() {
        try {
            ExerciseTrackerLogic logic = createLogic();
            User user = logic.createUser("alice", "pass123");

            if (user != null && user.getUsername().equals("alice")) {
                results.pass("createUser returns user with correct username");
            } else {
                results.fail("createUser returns user with correct username");
            }
        } catch (Exception e) {
            results.fail("createUser threw " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    private void testCreateUserDuplicateUsername() {
        try {
            ExerciseTrackerLogic logic = createLogic();
            logic.createUser("alice", "pass123");

            try {
                logic.createUser("alice", "otherpass");
                results.fail("createUser with duplicate username should throw UsernameTakenException");
            } catch (UsernameTakenException e) {
                results.pass("createUser with duplicate username throws UsernameTakenException");
            }
        } catch (Exception e) {
            results.fail("createUser duplicate threw " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    // ── Login / Logout ─────────────────────────────────────────────

    private void testLoginUser() {
        try {
            ExerciseTrackerLogic logic = createLogic();
            logic.createUser("bob", "secure");
            User loggedIn = logic.loginUser("bob", "secure");

            if (loggedIn != null && logic.isUserLoggedIn() && logic.getCurrentUser() == loggedIn) {
                results.pass("loginUser logs in with valid credentials");
            } else {
                results.fail("loginUser logs in with valid credentials");
            }
        } catch (Exception e) {
            results.fail("loginUser threw " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    private void testLoginUserInvalidCredentials() {
        try {
            ExerciseTrackerLogic logic = createLogic();
            logic.createUser("bob", "secure");

            try {
                logic.loginUser("bob", "wrongpassword");
                results.fail("loginUser with wrong password should throw InvalidCredentialsException");
            } catch (InvalidCredentialsException e) {
                results.pass("loginUser with wrong password throws InvalidCredentialsException");
            }
        } catch (Exception e) {
            results.fail("loginUser invalid threw " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    private void testLogoutUser() {
        try {
            ExerciseTrackerLogic logic = createLogic();
            logic.createUser("alice", "pass123");
            logic.loginUser("alice", "pass123");
            logic.logoutUser();

            if (!logic.isUserLoggedIn() && logic.getCurrentUser() == null) {
                results.pass("logoutUser sets currentUser to null");
            } else {
                results.fail("logoutUser sets currentUser to null");
            }
        } catch (Exception e) {
            results.fail("logoutUser threw " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    // ── Profile editing ────────────────────────────────────────────

    private void testChangeUsername() {
        try {
            ExerciseTrackerLogic logic = createLogic();
            logic.createUser("alice", "pass123");
            logic.loginUser("alice", "pass123");
            logic.changeCurrentUserUsername("alice_new");

            if (logic.getCurrentUser().getUsername().equals("alice_new")) {
                results.pass("changeCurrentUserUsername updates username");
            } else {
                results.fail("changeCurrentUserUsername updates username");
            }
        } catch (Exception e) {
            results.fail("changeUsername threw " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    private void testChangeUsernameTaken() {
        try {
            ExerciseTrackerLogic logic = createLogic();
            logic.createUser("alice", "pass123");
            logic.createUser("bob", "pass456");
            logic.loginUser("alice", "pass123");

            try {
                logic.changeCurrentUserUsername("bob");
                results.fail("changeCurrentUserUsername to taken name should throw UsernameTakenException");
            } catch (UsernameTakenException e) {
                results.pass("changeCurrentUserUsername to taken name throws UsernameTakenException");
            }
        } catch (Exception e) {
            results.fail("changeUsername taken threw " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    private void testChangePassword() {
        try {
            ExerciseTrackerLogic logic = createLogic();
            logic.createUser("alice", "pass123");
            logic.loginUser("alice", "pass123");
            boolean changed = logic.changeCurrentUserPassword("pass123", "newpass");

            if (changed && logic.getCurrentUser().authenticate("newpass")) {
                results.pass("changeCurrentUserPassword updates password");
            } else {
                results.fail("changeCurrentUserPassword updates password");
            }
        } catch (Exception e) {
            results.fail("changePassword threw " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    private void testChangePasswordWrongCurrent() {
        try {
            ExerciseTrackerLogic logic = createLogic();
            logic.createUser("alice", "pass123");
            logic.loginUser("alice", "pass123");
            boolean changed = logic.changeCurrentUserPassword("wrongold", "newpass");

            if (!changed && logic.getCurrentUser().authenticate("pass123")) {
                results.pass("changeCurrentUserPassword fails with wrong current password");
            } else {
                results.fail("changeCurrentUserPassword fails with wrong current password");
            }
        } catch (Exception e) {
            results.fail("changePassword wrong threw " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    // ── Map management ─────────────────────────────────────────────

    private void testInitializeMap() {
        try {
            ExerciseTrackerLogic logic = createLogic();
            Grid grid = logic.initializeMap(10, 10);

            if (grid != null && logic.isMapInitialized() && grid.getWidth() == 10 && grid.getHeight() == 10) {
                results.pass("initializeMap creates grid with correct dimensions");
            } else {
                results.fail("initializeMap creates grid with correct dimensions");
            }
        } catch (Exception e) {
            results.fail("initializeMap threw " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    private void testRemoveMap() {
        try {
            ExerciseTrackerLogic logic = createLogic();
            logic.initializeMap(10, 10);
            logic.removeMap();

            if (!logic.isMapInitialized()) {
                results.pass("removeMap sets grid to null");
            } else {
                results.fail("removeMap sets grid to null");
            }
        } catch (Exception e) {
            results.fail("removeMap threw " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    private void testGetGridBeforeInit() {
        try {
            ExerciseTrackerLogic logic = createLogic();

            try {
                logic.getGrid();
                results.fail("getGrid before init should throw MapNotInitializedException");
            } catch (MapNotInitializedException e) {
                results.pass("getGrid before init throws MapNotInitializedException");
            }
        } catch (Exception e) {
            results.fail("getGrid threw " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    // ── Exercise log / feed ────────────────────────────────────────

    private void testAddExerciseLog() {
        try {
            ExerciseTrackerLogic logic = createLogic();
            logic.createUser("alice", "pass123");
            logic.loginUser("alice", "pass123");
            logic.initializeMap(10, 10);

            List<Point> points = new ArrayList<>();
            points.add(new Point(0, 0));
            points.add(new Point(1, 0));
            points.add(new Point(2, 0));

            Exercise exercise = new Exercise("Running", Unit.KILOMETERS);
            ExerciseLog log = new ExerciseLog("Morning run", exercise, points, 3.0);
            logic.addExerciseLogToCurrentUser(log);

            List<ExerciseLog> logs = logic.getCurrentUserPreviousRoutes();
            if (logs.size() == 1 && logs.get(0).getName().equals("Morning run")) {
                results.pass("addExerciseLogToCurrentUser adds log to current user");
            } else {
                results.fail("addExerciseLogToCurrentUser adds log to current user");
            }
        } catch (Exception e) {
            results.fail("addExerciseLog threw " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    private void testGetActivityFeed() {
        try {
            ExerciseTrackerLogic logic = createLogic();
            User alice = logic.createUser("alice", "pass123");
            User bob = logic.createUser("bob", "pass456");

            // Login as bob, add an exercise
            logic.loginUser("bob", "pass456");
            logic.initializeMap(10, 10);

            List<Point> points = new ArrayList<>();
            points.add(new Point(0, 0));
            points.add(new Point(1, 1));

            Exercise exercise = new Exercise("Walking", Unit.KILOMETERS);
            ExerciseLog log = new ExerciseLog("Walk", exercise, points, 1.5);
            logic.addExerciseLogToCurrentUser(log);
            logic.logoutUser();

            // Login as alice, follow bob
            logic.loginUser("alice", "pass123");
            alice.addFollowing(bob);

            List<ExerciseLog> feed = logic.getActivityFeed();
            if (feed.size() == 1 && feed.get(0).getName().equals("Walk")) {
                results.pass("getActivityFeed returns followed user's logs");
            } else {
                results.fail("getActivityFeed returns followed user's logs (got " + feed.size() + " logs)");
            }
        } catch (Exception e) {
            results.fail("getActivityFeed threw " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    private void testGetOtherUsers() {
        try {
            ExerciseTrackerLogic logic = createLogic();
            logic.createUser("alice", "pass123");
            logic.createUser("bob", "pass456");
            logic.createUser("charlie", "pass789");
            logic.loginUser("alice", "pass123");

            List<User> others = logic.getOtherUsers();
            if (others.size() == 2) {
                results.pass("getOtherUsers returns all users except current");
            } else {
                results.fail("getOtherUsers returns all users except current (got " + others.size() + ")");
            }
        } catch (Exception e) {
            results.fail("getOtherUsers threw " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    // ── Stub persistence (in-memory, no file I/O) ─────────────────

    private static class StubPersistence implements ExerciseTrackerPersistence {
        @Override
        public void saveUsers(List<User> users) {
            // no-op for testing
        }

        @Override
        public List<User> loadUsers() {
            return new ArrayList<>();
        }
    }
}
