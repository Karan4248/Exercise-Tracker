package ca.umanitoba.cs.kanand.ui;

import ca.umanitoba.cs.kanand.logic.ExerciseTrackerLogic;
import ca.umanitoba.cs.kanand.model.*;
import ca.umanitoba.cs.kanand.exceptions.*;
import ca.umanitoba.cs.kanand.printers.ExerciseLogPrinter;

import java.util.*;

/**
 * User Interface layer for the Exercise Tracker application.
 * Handles all user interaction flows and input/output.
 * 
 * This class implements the interactive flows specified in Phase 4:
 * - Authentication (login/register)
 * - View activity feed and follow users
 * - Add new activities with route options
 * - Find routes using pathfinding algorithm
 * - Edit user profile
 */
public class ExerciseTrackerUI {
    private final Scanner scanner;
    private final ExerciseTrackerLogic logic;
    private boolean running;

    /**
     * Initializes the UI layer with scanner and logic layer.
     *
     * @param scanner the Scanner for user input
     */
    public ExerciseTrackerUI(Scanner scanner) {
        this.scanner = scanner;
        this.logic = new ExerciseTrackerLogic();
        this.running = true;

        try {
            logic.initializeMap(20, 20);
        } catch (Exception e) {
            System.out.println("ERROR: Could not initialize map: " + e.getMessage());
        }
    }

    /**
     * Starts the main application loop.
     */
    public void start() {
        printWelcome();
        
        if (!authenticate()) {
            printGoodbye();
            return;
        }
        
        while (running && logic.isUserLoggedIn()) {
            displayMainMenu();
        }
        
        printGoodbye();
    }

    /**
     * Handles user authentication (login or register).
     *
     * @return true if authenticated, false if user quit
     */
    private boolean authenticate() {
        while (true) {
            System.out.println("\n=== AUTHENTICATION ===");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            
            String choice = getInput("Select (1-3): ");
            
            switch (choice) {
                case "1" -> {
                    if (attemptLogin()) {
                        return true;
                    }
                }
                case "2" -> {
                    if (attemptRegister()) {
                        return true;
                    }
                }
                case "3" -> {
                    return false;
                }
                default -> System.out.println("Invalid choice. Please enter 1, 2, or 3.");
            }
        }
    }

    /**
     * Attempts to log in a user.
     *
     * @return true if login successful
     */
    private boolean attemptLogin() {
        String username = getInput("\nEnter username: ");
        if (isEmpty(username)) {
            System.out.println("ERROR: Username cannot be empty.");
            return false;
        }
        
        String password = getInput("Enter password: ");
        if (isEmpty(password)) {
            System.out.println("ERROR: Password cannot be empty.");
            return false;
        }
        
        try {
            logic.loginUser(username, password);
            System.out.println("✓ Login successful!");
            return true;
        } catch (InvalidCredentialsException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    /**
     * Attempts to register a new user.
     *
     * @return true if registration and login successful
     */
    private boolean attemptRegister() {
        String username = getInput("\nEnter new username: ");
        if (isEmpty(username)) {
            System.out.println("ERROR: Username cannot be empty.");
            return false;
        }
        
        if (username.length() < 3) {
            System.out.println("ERROR: Username must be at least 3 characters.");
            return false;
        }
        
        String password = getInput("Enter password: ");
        if (isEmpty(password)) {
            System.out.println("ERROR: Password cannot be empty.");
            return false;
        }
        
        if (password.length() < 3) {
            System.out.println("ERROR: Password must be at least 3 characters.");
            return false;
        }
        
        try {
            logic.createUser(username, password);
            logic.loginUser(username, password);
            System.out.println("✓ Account created and logged in!");
            return true;
        } catch (UsernameTakenException e) {
            System.out.println(e.getMessage());
            return false;
        } catch (InvalidCredentialsException e) {
            System.out.println("ERROR: " + e.getMessage());
            return false;
        }
    }

    /**
     * Displays and handles the main menu.
     */
    private void displayMainMenu() {
        User user = logic.getCurrentUser();
        System.out.println("\n=== MAIN MENU ===");
        System.out.println("User: " + user.getUsername());
        System.out.println("1. View feed & follow users");
        System.out.println("2. Add activity");
        System.out.println("3. Find new route");
        System.out.println("4. View my activities");
        System.out.println("5. Edit profile");
        System.out.println("6. Logout");
        
        String choice = getInput("Select (1-6): ");
        
        switch (choice) {
            case "1" -> viewFeedFlow();
            case "2" -> addActivityFlow();
            case "3" -> findRouteFlow();
            case "4" -> viewMyActivitiesFlow();
            case "5" -> editProfileFlow();
            case "6" -> logoutFlow();
            default -> System.out.println("Invalid choice. Please enter a number from 1 to 6.");
        }
    }

    /**
     * Handles viewing activity feed and following users.
     */
    private void viewFeedFlow() {
        System.out.println("\n=== ACTIVITY FEED ===");
        
        List<ExerciseLog> feed = logic.getActivityFeed();
        if (feed.isEmpty()) {
            System.out.println("Your feed is empty. Follow some users!");
        } else {
            for (ExerciseLog log : feed) {
                System.out.println("  " + ExerciseLogPrinter.format(log));
            }
        }
        
        followUsersFlow();
    }

    /**
     * Handles following/unfollowing users.
     */
    private void followUsersFlow() {
        System.out.println("\n=== USERS ===");
        
        List<User> others = logic.getOtherUsers();
        User current = logic.getCurrentUser();
        
        if (others.isEmpty()) {
            System.out.println("No other users.");
            return;
        }
        
        for (int i = 0; i < others.size(); i++) {
            User user = others.get(i);
            String status = current.isFollowing(user) ? "[Following]" : "[ ]";
            System.out.println((i + 1) + ". " + user.getUsername() + " " + status);
        }
        
        String input = getInput("\nEnter user # to toggle follow (or press Enter): ");
        if (isEmpty(input)) return;
        
        try {
            int idx = Integer.parseInt(input) - 1;
            if (idx >= 0 && idx < others.size()) {
                User selected = others.get(idx);
                if (current.isFollowing(selected)) {
                    current.removeFollowing(selected);
                    System.out.println("✓ Unfollowed " + selected.getUsername());
                } else {
                    current.addFollowing(selected);
                    System.out.println("✓ Following " + selected.getUsername());
                }
            } else {
                System.out.println("Invalid user number. Please enter a number between 1 and " + others.size() + ".");
            }
        } catch (NumberFormatException e) {
            System.out.println("ERROR: Please enter a number.");
        }
    }

    /**
     * Handles adding a new activity.
     */
    private void addActivityFlow() {
        System.out.println("\n=== ADD ACTIVITY ===");
        
        try {
            Grid grid = logic.getGrid();
            ExerciseLog.ExerciseLogBuilder logBuilder = ExerciseLog.builder();
            Exercise.ExerciseBuilder exerciseBuilder = Exercise.builder();
            
            // Get activity name using builder validation
            boolean validName = false;
            do {
                String name = getInput("Activity name: ");
                try {
                    logBuilder.name(name);
                    validName = true;
                } catch (InvalidActivityException e) {
                    System.out.println("ERROR: " + e.getMessage());
                }
            } while (!validName);
            
            // Get exercise type using builder validation
            boolean validType = false;
            do {
                String type = getInput("Exercise type: ");
                try {
                    exerciseBuilder.name(type);
                    validType = true;
                } catch (InvalidActivityException e) {
                    System.out.println("ERROR: " + e.getMessage());
                }
            } while (!validType);
            
            // Get unit using builder validation
            boolean validUnit = false;
            do {
                String unitStr = getInput("Unit (KILOMETERS/MILES/METERS/STEPS): ");
                try {
                    Unit unit = Unit.valueOf(unitStr.toUpperCase());
                    exerciseBuilder.unit(unit);
                    validUnit = true;
                } catch (IllegalArgumentException e) {
                    System.out.println("ERROR: Invalid unit. Please enter one of: KILOMETERS, MILES, METERS, STEPS.");
                } catch (InvalidActivityException e) {
                    System.out.println("ERROR: " + e.getMessage());
                }
            } while (!validUnit);
            
            // Get distance using builder validation
            boolean validDistance = false;
            do {
                String distStr = getInput("Distance: ");
                try {
                    double distance = Double.parseDouble(distStr);
                    logBuilder.distance(distance);
                    validDistance = true;
                } catch (NumberFormatException e) {
                    System.out.println("ERROR: Please enter a valid number for distance.");
                } catch (InvalidActivityException e) {
                    System.out.println("ERROR: " + e.getMessage());
                }
            } while (!validDistance);
            
            List<Point> route = selectRouteFlow(grid);
            if (route == null || route.isEmpty()) {
                System.out.println("Activity cancelled.");
                return;
            }
            
            try {
                logBuilder.route(route);
            } catch (InvalidActivityException e) {
                System.out.println("ERROR: " + e.getMessage());
                return;
            }
            
            logBuilder.exercise(exerciseBuilder.build());
            ExerciseLog log = logBuilder.build();
            logic.addExerciseLogToCurrentUser(log);
            
            System.out.println("✓ Activity added!");
            System.out.println(ExerciseLogPrinter.format(log));
            
            if (askYesNo("Add obstacles?")) {
                addObstaclesFlow(grid);
            }
            
        } catch (MapNotInitializedException e) {
            System.out.println("ERROR: Map not initialized.");
        } catch (InvalidActivityException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    /**
     * Handles route selection (copy from previous or enter new).
     *
     * @param grid the game grid
     * @return list of points, or null if cancelled
     */
    private List<Point> selectRouteFlow(Grid grid) {
        System.out.println("\n--- Route ---");
        System.out.println("1. Copy previous route");
        System.out.println("2. Enter new route");
        System.out.println("3. Cancel");
        
        String choice = getInput("Select: ");
        
        return switch (choice) {
            case "1" -> copyPreviousRoute();
            case "2" -> enterNewRoute(grid);
            case "3" -> null;
            default -> {
                System.out.println("Invalid choice. Please enter 1, 2, or 3.");
                yield null;
            }
        };
    }

    /**
     * Allows user to copy a previous route.
     *
     * @return list of points, or null if cancelled
     */
    private List<Point> copyPreviousRoute() {
        List<ExerciseLog> previous = logic.getCurrentUserPreviousRoutes();
        
        if (previous.isEmpty()) {
            System.out.println("No previous routes.");
            return null;
        }
        
        System.out.println("\nPrevious routes:");
        for (int i = 0; i < previous.size(); i++) {
            System.out.println((i + 1) + ". " + previous.get(i).getName());
        }
        
        String input = getInput("Select route # (or press Enter): ");
        if (isEmpty(input)) return null;
        
        try {
            int idx = Integer.parseInt(input) - 1;
            if (idx >= 0 && idx < previous.size()) {
                System.out.println("✓ Route copied.");
                return new ArrayList<>(previous.get(idx).getPoints());
            } else {
                System.out.println("Invalid route number. Please enter a number between 1 and " + previous.size() + ".");
                return null;
            }
        } catch (NumberFormatException e) {
            System.out.println("ERROR: Enter a number.");
            return null;
        }
    }

    /**
     * Allows user to enter a new route manually.
     *
     * @param grid the game grid
     * @return list of points entered
     */
    private List<Point> enterNewRoute(Grid grid) {
        List<Point> route = new ArrayList<>();
        System.out.println("\nEnter route points (grid: " + grid.getWidth() + "x" + grid.getHeight() + ")");
        System.out.println("Type 'done' when finished.");
        
        for (int i = 0; i < 100; i++) {
            String input = getInput("Point " + (i + 1) + " (x y): ");
            
            if (input.equalsIgnoreCase("done")) {
                if (route.isEmpty()) {
                    System.out.println("ERROR: At least 1 point needed.");
                    i--;
                    continue;
                }
                System.out.println("✓ Route entered.");
                return route;
            }
            
            try {
                String[] parts = input.trim().split("\\s+");
                if (parts.length != 2) {
                    System.out.println("ERROR: Enter two numbers.");
                    i--;
                    continue;
                }
                
                int x = Integer.parseInt(parts[0]);
                int y = Integer.parseInt(parts[1]);
                if (x < 0 || y < 0) {
                    System.out.println("ERROR: Coordinates must be non-negative.");
                    i--;
                    continue;
                }
                Point p = new Point(x, y);
                
                if (!grid.isInBounds(p)) {
                    System.out.println("ERROR: Point is out of bounds. Grid is " + grid.getWidth() + "x" + grid.getHeight() + ". Please enter valid coordinates.");
                    i--;
                    continue;
                }
                
                route.add(p);
            } catch (NumberFormatException e) {
                System.out.println("ERROR: Enter valid integers.");
                i--;
            }
        }
        
        return route.isEmpty() ? null : route;
    }

    /**
     * Handles finding new routes using pathfinding.
     */
    private void findRouteFlow() {
        System.out.println("\n=== FIND ROUTE ===");
        
        try {
            Grid grid = logic.getGrid();
            
            System.out.println("Route source:");
            System.out.println("1. My routes only");
            System.out.println("2. All followed routes");
            System.out.println("3. Cancel");
            
            String scopeChoice = getInput("Select: ");
            
            RouteScope scope = switch (scopeChoice) {
                case "1" -> RouteScope.MY_ROUTES_ONLY;
                case "2" -> RouteScope.ALL_ROUTES;
                case "3" -> null;
                default -> {
                    System.out.println("Invalid choice. Please enter 1, 2, or 3.");
                    yield null;
                }
            };
            
            if (scope == null) return;
            
            System.out.println("\nEnter start point:");
            Point start = inputPoint(grid);
            if (start == null) return;
            
            System.out.println("\nEnter end point:");
            Point end = inputPoint(grid);
            if (end == null) return;
            
            if (start.equals(end)) {
                System.out.println("ERROR: Different start and end points are needed for route finding.");
                return;
            }
            
            System.out.println("Searching...");
            List<Point> path = logic.findPath(start, end, scope);
            
            if (path == null) {
                System.out.println("✗ No path found.");
                return;
            }
            
            System.out.println("✓ Path found! (" + path.size() + " points)");
            
            if (askYesNo("Create activity from this route?")) {
                try {
                    ExerciseLog.ExerciseLogBuilder logBuilder = ExerciseLog.builder();
                    Exercise.ExerciseBuilder exerciseBuilder = Exercise.builder();
                    
                    // Get activity name using builder validation
                    boolean validName = false;
                    do {
                        String name = getInput("Activity name: ");
                        try {
                            logBuilder.name(name);
                            validName = true;
                        } catch (InvalidActivityException e) {
                            System.out.println("ERROR: " + e.getMessage());
                        }
                    } while (!validName);
                    
                    // Get exercise type using builder validation
                    boolean validType = false;
                    do {
                        String type = getInput("Exercise type: ");
                        try {
                            exerciseBuilder.name(type);
                            validType = true;
                        } catch (InvalidActivityException e) {
                            System.out.println("ERROR: " + e.getMessage());
                        }
                    } while (!validType);
                    
                    // Get unit using builder validation
                    boolean validUnit = false;
                    do {
                        String unitStr = getInput("Unit (KILOMETERS/MILES/METERS/STEPS): ");
                        try {
                            Unit unit = Unit.valueOf(unitStr.toUpperCase());
                            exerciseBuilder.unit(unit);
                            validUnit = true;
                        } catch (IllegalArgumentException e) {
                            System.out.println("ERROR: Invalid unit. Please enter one of: KILOMETERS, MILES, METERS, STEPS.");
                        } catch (InvalidActivityException e) {
                            System.out.println("ERROR: " + e.getMessage());
                        }
                    } while (!validUnit);
                    
                    // Get distance using builder validation
                    boolean validDistance = false;
                    do {
                        String distStr = getInput("Distance: ");
                        try {
                            double dist = Double.parseDouble(distStr);
                            logBuilder.distance(dist);
                            validDistance = true;
                        } catch (NumberFormatException e) {
                            System.out.println("ERROR: Please enter a valid number for distance.");
                        } catch (InvalidActivityException e) {
                            System.out.println("ERROR: " + e.getMessage());
                        }
                    } while (!validDistance);
                    
                    logBuilder.route(path);
                    logBuilder.exercise(exerciseBuilder.build());
                    
                    ExerciseLog log = logBuilder.build();
                    logic.addExerciseLogToCurrentUser(log);
                    
                    System.out.println("✓ Activity created!");
                } catch (InvalidActivityException e) {
                    System.out.println("ERROR: " + e.getMessage());
                }
            }
            
        } catch (MapNotInitializedException | InvalidPathException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    /**
     * Prompts user for a point on the grid.
     *
     * @param grid the game grid
     * @return the point, or null if invalid
     */
    private Point inputPoint(Grid grid) {
        String input = getInput("Enter (x y): ");
        
        if (isEmpty(input)) return null;
        
        try {
            String[] parts = input.trim().split("\\s+");
            if (parts.length != 2) {
                System.out.println("ERROR: Enter two numbers.");
                return null;
            }
            
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);
            if (x < 0 || y < 0) {
                System.out.println("ERROR: Coordinates must be non-negative.");
                return null;
            }
            Point p = new Point(x, y);
            
            if (!grid.isInBounds(p)) {
                System.out.println("ERROR: Point is out of bounds. Grid is " + grid.getWidth() + "x" + grid.getHeight() + ". Please enter valid coordinates.");
                return null;
            }
            
            if (grid.getObstacleAt(p) != null) {
                System.out.println("ERROR: There is an obstacle at this location. Please choose a different point.");
                return null;
            }
            
            return p;
        } catch (NumberFormatException e) {
            System.out.println("ERROR: Enter valid integers.");
            return null;
        }
    }

    /**
     * Handles viewing user's own activities.
     */
    private void viewMyActivitiesFlow() {
        System.out.println("\n=== MY ACTIVITIES ===");
        
        User user = logic.getCurrentUser();
        List<ExerciseLog> activities = user.getActivity().getAllLogs();
        
        if (activities.isEmpty()) {
            System.out.println("No activities yet.");
        } else {
            for (ExerciseLog log : activities) {
                System.out.println("  " + ExerciseLogPrinter.format(log));
            }
        }
    }

    /**
     * Handles profile editing.
     */
    private void editProfileFlow() {
        System.out.println("\n=== EDIT PROFILE ===");
        System.out.println("1. Change username");
        System.out.println("2. Change password");
        System.out.println("3. Back");
        
        String choice = getInput("Select: ");
        
        switch (choice) {
            case "1" -> changeUsername();
            case "2" -> changePassword();
            case "3" -> { }
            default -> System.out.println("Invalid choice.");
        }
    }

    /**
     * Handles username change.
     */
    private void changeUsername() {
        String newName = getInput("\nNew username: ");
        
        if (isEmpty(newName)) {
            System.out.println("ERROR: Cannot be empty.");
            return;
        }
        
        if (newName.length() < 3) {
            System.out.println("ERROR: Min 3 characters.");
            return;
        }
        
        try {
            logic.changeCurrentUserUsername(newName);
            System.out.println("✓ Username changed!");
        } catch (UsernameTakenException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    /**
     * Handles password change.
     */
    private void changePassword() {
        String current = getInput("\nCurrent password: ");
        
        if (isEmpty(current)) {
            System.out.println("ERROR: Cannot be empty.");
            return;
        }
        
        String newPass = getInput("New password: ");
        
        if (isEmpty(newPass)) {
            System.out.println("ERROR: Cannot be empty.");
            return;
        }
        
        if (newPass.length() < 3) {
            System.out.println("ERROR: Min 3 characters.");
            return;
        }
        
        if (current.equals(newPass)) {
            System.out.println("ERROR: Must be different.");
            return;
        }
        
        if (logic.changeCurrentUserPassword(current, newPass)) {
            System.out.println("✓ Password changed!");
        } else {
            System.out.println("ERROR: Incorrect current password.");
        }
    }

    /**
     * Handles adding obstacles.
     *
     * @param grid the game grid
     */
    private void addObstaclesFlow(Grid grid) {
        boolean adding = true;
        
        while (adding) {
            String input = getInput("\nObstacle location (x y) or Enter to skip: ");
            
            if (isEmpty(input)) {
                adding = false;
                continue;
            }
            
            try {
                String[] parts = input.trim().split("\\s+");
                if (parts.length != 2) {
                    System.out.println("ERROR: Enter two numbers.");
                    continue;
                }
                
                int x = Integer.parseInt(parts[0]);
                int y = Integer.parseInt(parts[1]);
                if (x < 0 || y < 0) {
                    System.out.println("ERROR: Coordinates must be non-negative.");
                    continue;
                }
                Point p = new Point(x, y);
                
                if (!grid.isInBounds(p)) {
                    System.out.println("ERROR: Point is out of bounds. Grid is " + grid.getWidth() + "x" + grid.getHeight() + ". Please enter valid coordinates.");
                    continue;
                }
                
                String typeStr = getInput("Type (TREE/BUILDING/ROCK/WATER): ");
                
                try {
                    Obstacle type = Obstacle.valueOf(typeStr.toUpperCase());
                    ObstaclePlacement obs = new ObstaclePlacement(p, type);
                    grid.addObstacle(obs);
                    System.out.println("✓ Obstacle added.");
                    
                    if (!askYesNo("Add another?")) {
                        adding = false;
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("ERROR: Invalid obstacle type. Please enter one of: TREE, BUILDING, ROCK, WATER.");
                }
            } catch (NumberFormatException e) {
                System.out.println("ERROR: Enter valid integers.");
            }
        }
    }

    /**
     * Handles logout.
     */
    private void logoutFlow() {
        if (askYesNo("Logout?")) {
            logic.logoutUser();
            System.out.println("✓ Logged out.");
        }
    }

    // === UTILITY METHODS ===

    /**
     * Gets user input.
     *
     * @param prompt the prompt to display
     * @return user input (trimmed)
     */
    private String getInput(String prompt) {
        System.out.print(prompt);
        try {
            return scanner.nextLine().trim();
        } catch (NoSuchElementException e) {
            running = false;
            return "";
        }
    }

    /**
     * Checks if string is empty.
     *
     * @param str the string to check
     * @return true if empty
     */
    private boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    /**
     * Asks user a yes/no question.
     *
     * @param question the question
     * @return true for yes
     */
    private boolean askYesNo(String question) {
        while (true) {
            String response = getInput(question + " (y/n): ").toLowerCase();
            
            if (response.startsWith("y")) {
                return true;
            } else if (response.startsWith("n")) {
                return false;
            } else {
                System.out.println("ERROR: Answer 'y' or 'n'.");
            }
        }
    }

    /**
     * Prints welcome message.
     */
    private void printWelcome() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║   EXERCISE TRACKER                     ║");
        System.out.println("║   Track your activities & routes       ║");
        System.out.println("╚════════════════════════════════════════╝");
    }

    /**
     * Prints goodbye message.
     */
    private void printGoodbye() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║   Thank you for using Exercise Tracker ║");
        System.out.println("╚════════════════════════════════════════╝\n");
    }
}
