package ca.umanitoba.cs.kanand.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import ca.umanitoba.cs.kanand.printers.ExerciseLogPrinter;
import ca.umanitoba.cs.kanand.printers.ObstaclePlacementPrinter;

/**
 * REPL (Read-Eval-Print-Loop) for the Exercise Tracker application.
 */
public class ExerciseTrackerREPL {
    private final Scanner scanner;
    private Grid grid;
    private final Activity activity;
    private boolean running;

    /**
     * Initializes the REPL with a scanner for user input.
     *
     * @param scanner the Scanner for reading user commands
     */
    public ExerciseTrackerREPL(Scanner scanner) {
        this.scanner = scanner;
        this.grid = null;
        this.activity = new Activity();
        this.running = true;
    }

    /**
     * Starts the main REPL loop, displaying the welcome message and processing commands.
     */
    public void start() {
        System.out.println("╔════════════════════════════════════════════╗");
        System.out.println("║     Welcome to the Exercise Tracker!       ║");
        System.out.println("║     Type HELP for available commands       ║");
        System.out.println("╚════════════════════════════════════════════╝");
        System.out.println();

        while (running) {
            System.out.print("\n> ");
            String input = scanner.nextLine().trim().toUpperCase();
            processCommand(input);
        }
    }

    /**
     * Processes a user command and executes the corresponding action.
     *
     * @param command the command string in uppercase
     */
    private void processCommand(String command) {
        switch (command) {
            case "HELP" -> showHelp();
            case "ADD MAP" -> addMap();
            case "ADD OBSTACLE" -> addObstacle();
            case "ADD ACTIVITY" -> addActivity();
            case "SHOW MAP" -> showMap();
            case "SHOW OBSTACLES" -> showObstacles();
            case "SHOW ACTIVITIES" -> showActivities();
            case "SHOW ACTIVITY" -> showActivity();
            case "REMOVE ACTIVITY" -> removeActivity();
            case "REMOVE OBSTACLE" -> removeObstacle();
            case "REMOVE MAP" -> removeMap();
            case "QUIT", "EXIT" -> quit();
            default -> System.out.println("Unknown command. Type HELP for available commands.");
        }
    }

    /**
     * Displays the help menu with all available commands and input formats.
     */
    private void showHelp() {
        System.out.println("""
            ╔══════════════════════════════════════════════════════════════════╗
            ║                     AVAILABLE COMMANDS                           ║
            ╠══════════════════════════════════════════════════════════════════╣
            ║ HELP           - Show this help message                          ║
            ║ ADD MAP        - Initialize the map with width and height        ║
            ║ ADD OBSTACLE   - Add a rectangular obstacle to the map           ║
            ║ ADD ACTIVITY   - Track a new activity with a route               ║
            ║ SHOW MAP       - Display the map with all routes and obstacles   ║
            ║ SHOW OBSTACLES - List all obstacles on the map                   ║
            ║ SHOW ACTIVITIES- List all tracked activities                     ║
            ║ SHOW ACTIVITY  - Display map with a single activity's route      ║
            ║ REMOVE ACTIVITY- Remove an activity by ID                        ║
            ║ REMOVE OBSTACLE- Remove an obstacle by ID                        ║
            ║ REMOVE MAP     - Remove the entire map                           ║
            ║ QUIT / EXIT    - Exit the application                            ║
            ╠══════════════════════════════════════════════════════════════════╣
            ║ INPUT FORMATS:                                                   ║
            ║   - Coordinates: space-separated x y (e.g., "3 5")               ║
            ║   - Activities: identified by numeric ID                         ║
            ║   - Obstacles: identified by numeric ID                          ║
            ╚══════════════════════════════════════════════════════════════════╝
            """);
    }

    /**
     * Prompts the user to create a new map with specified width and height.
     * Resets ID counters for obstacles and activities.
     */
    private void addMap() {
        if (grid != null) {
            System.out.println("A map already exists. Remove it first with REMOVE MAP.");
            return;
        }
        System.out.print("Enter map width: ");
        int width = Integer.parseInt(scanner.nextLine().trim());

        System.out.print("Enter map height: ");
        int height = Integer.parseInt(scanner.nextLine().trim());

        grid = new Grid(width, height);
        ObstaclePlacement.resetIdCounter();
        ExerciseLog.resetIdCounter();
        activity.clear();

        System.out.println("Map created! Size: " + width + " x " + height);
    }

    /**
     * Prompts the user to add a rectangular obstacle to the map.
     */
    private void addObstacle() {
        if (grid == null) {
            System.out.println("No map exists. Create one first with ADD MAP.");
            return;
        }
        System.out.print("Enter obstacle position (x y): ");
        String[] coords = scanner.nextLine().trim().split("\\s+");
        int x = Integer.parseInt(coords[0]);
        int y = Integer.parseInt(coords[1]);

        System.out.println("Obstacle types: TREE, BUILDING, ROCK, WATER");
        System.out.print("Enter obstacle type: ");
        String typeStr = scanner.nextLine().trim().toUpperCase();
        Obstacle type = Obstacle.valueOf(typeStr);

        Point location = new Point(x, y);
        
        if (!grid.isInBounds(location)) {
            System.out.println("Error: Obstacle location (" + location.getX() + "," + location.getY() + ") is out of bounds. "
                    + "Grid size: " + grid.getWidth() + "x" + grid.getHeight());
            return;
        }
        ObstaclePlacement obstacle = new ObstaclePlacement(location, type);
        grid.addObstacle(obstacle);
        System.out.println("Obstacle added: " + ObstaclePlacementPrinter.format(obstacle));
    }

    /**
     * Prompts the user to add a new activity with a route traced on the map.
     */
    private void addActivity() {
        if (grid == null) {
            System.out.println("No map exists. Create one first with ADD MAP.");
            return;
        }
        System.out.print("Enter activity name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Enter exercise type (e.g., Running, Cycling): ");
        String exerciseType = scanner.nextLine().trim();

        System.out.println("Available units: KILOMETERS, MILES, METERS, STEPS");
        System.out.print("Enter unit: ");
        String unitStr = scanner.nextLine().trim().toUpperCase();
        Unit unit = Unit.valueOf(unitStr);

        System.out.print("Enter distance covered: ");
        double distance = Double.parseDouble(scanner.nextLine().trim());

        System.out.print("Enter number of route points: ");
        int numPoints = Integer.parseInt(scanner.nextLine().trim());

        if (numPoints < 1) {
            System.out.println("Activity must have at least one route point.");
            return;
        }
        List<Point> route = new ArrayList<>();
        
        for (int i = 0; i < numPoints; i++) {
            System.out.print("Enter point " + (i + 1) + " (x y): ");
            String[] coordsArr = scanner.nextLine().trim().split("\\s+");
            int px = Integer.parseInt(coordsArr[0]);
            int py = Integer.parseInt(coordsArr[1]);
            Point point = new Point(px, py);

            if (!grid.isInBounds(point)) {
                System.out.println("Point is outside map bounds. Try again.");
                i--;
            } else {
                route.add(point);
            }
        }

        if (!route.isEmpty()) {
            Exercise exercise = new Exercise(exerciseType, unit);
            ExerciseLog log = new ExerciseLog(name, exercise, route, distance);
            activity.addExerciseLog(log);
            System.out.println("Activity added: " + ExerciseLogPrinter.format(log));
        }
    }

    /**
     * Displays the entire map with all routes and obstacles, along with legend and summary.
     */
    private void showMap() {
        if (grid == null) {
            System.out.println("No map exists. Create one first with ADD MAP.");
            return;
        }
        printMapGrid(null);
        printLegend();
        printActivitySummary();
    }

    /**
     * Displays the map with a single activity's route highlighted.
     */
    private void showActivity() {
        if (grid == null) {
            System.out.println("No map exists. Create one first with ADD MAP.");
            return;
        }
        if (activity.getAllLogs().isEmpty()) {
            System.out.println("No activities recorded yet.");
            return;
        }
        System.out.println("Available activities:");
        for (ExerciseLog log : activity.getAllLogs()) {
            System.out.println("  " + log.getId() + ": " + log.getName());
        }

        System.out.print("Enter activity ID to display: ");
        int id = Integer.parseInt(scanner.nextLine().trim());

        ExerciseLog selectedLog = activity.getExerciseLog(id);
        if (selectedLog == null) {
            System.out.println("Activity not found with ID: " + id);
            return;
        }
        printMapGrid(selectedLog);
        System.out.println("\nShowing activity: " + ExerciseLogPrinter.format(selectedLog));
        printLegend();
    }

    /**
     * Renders the grid map with activity routes and obstacles.
     * If selectedActivity is null, displays all activities.
     *
     * @param selectedActivity a specific activity to highlight, or null for all activities
     */
    private void printMapGrid(ExerciseLog selectedActivity) {
        int width = grid.getWidth();
        int height = grid.getHeight();

        boolean[][] routeGrid = new boolean[width + 1][height + 1];

        List<ExerciseLog> logsToShow = (selectedActivity != null)
                ? List.of(selectedActivity)
                : activity.getAllLogs();

        for (ExerciseLog log : logsToShow) {
            for (Point p : log.getPoints()) {
                if (p.getX() <= width && p.getY() <= height) {
                    routeGrid[p.getX()][p.getY()] = true;
                }
            }
        }

        System.out.println("\n+" + "-".repeat((width + 1) * 2 - 1) + "+");
        for (int y = height; y >= 0; y--) {
            System.out.print("|");
            for (int px = 0; px <= width; px++) {
                Point p = new Point(px, y);
                ObstaclePlacement obs = grid.getObstacleAt(p);
                if (obs != null) {
                    System.out.print("* ");
                } else if (routeGrid[px][y]) {
                    System.out.print("> ");
                } else {
                    System.out.print(". ");
                }
            }
            System.out.println("|");
        }
        System.out.println("+" + "-".repeat(width * 2 - 1) + "+");
    }

    /**
     * Displays the map legend explaining the symbols used in the grid.
     */
    private void printLegend() {
        System.out.println("""
            ╔═══════════════════════════════════════════════════════════════╗
            ║                         MAP LEGEND                            ║
            ╠═══════════════════════════════════════════════════════════════╣
            ║   .  = Empty location                                         ║
            ║   >  = Activity route point                                   ║
            ║   *  = Obstacle                                               ║
            ╚═══════════════════════════════════════════════════════════════╝
            """);
    }

    /**
     * Displays a summary of all activities including distance statistics.
     */
    private void printActivitySummary() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneWeekAgo = now.minusWeeks(1);
        LocalDateTime oneYearAgo = now.minusYears(1);

        double weeklyDistance = activity.getTotalDistance(oneWeekAgo);
        double yearlyDistance = activity.getTotalDistance(oneYearAgo);
        double lifetimeDistance = activity.getLifetimeDistance();

        System.out.println("""
            ╔═══════════════════════════════════════════════════════════════╗
            ║                    ACTIVITY SUMMARY                           ║
            ╠═══════════════════════════════════════════════════════════════╣""");
        System.out.printf("║   Total Activities: %-41d ║%n", activity.getAllLogs().size());
        System.out.printf("║   Distance (Last Week):  %-36.2f ║%n", weeklyDistance);
        System.out.printf("║   Distance (Last Year):  %-36.2f ║%n", yearlyDistance);
        System.out.printf("║   Distance (Lifetime):   %-36.2f ║%n", lifetimeDistance);
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");
    }

    /**
     * Lists all obstacles currently on the map.
     */
    private void showObstacles() {
        if (grid == null) {
            System.out.println("No map exists. Create one first with ADD MAP.");
            return;
        }
        List<ObstaclePlacement> obstacles = grid.getObstacles();
        if (obstacles.isEmpty()) {
            System.out.println("No obstacles on the map.");
            return;
        }
        System.out.println("\n═══════════════════════════════════════");
        System.out.println("              OBSTACLES                 ");
        System.out.println("═══════════════════════════════════════");
        for (ObstaclePlacement obs : obstacles) {
            System.out.println("  " + ObstaclePlacementPrinter.format(obs));
        }
        System.out.println("═══════════════════════════════════════\n");
    }

    /**
     * Lists all recorded activities by ID and name.
     */
    private void showActivities() {
        List<ExerciseLog> logs = activity.getAllLogs();
        if (logs.isEmpty()) {
            System.out.println("No activities recorded yet.");
            return;
        }
        System.out.println("\n═══════════════════════════════════════════════════════════════");
        System.out.println("                        ACTIVITIES                              ");
        System.out.println("═══════════════════════════════════════════════════════════════");
        for (ExerciseLog log : logs) {
            System.out.println("  " + ExerciseLogPrinter.format(log));
        }
        System.out.println("═══════════════════════════════════════════════════════════════\n");
    }

    /**
     * Prompts the user to select and remove an activity by ID.
     */
    private void removeActivity() {
        List<ExerciseLog> logs = activity.getAllLogs();
        if (logs.isEmpty()) {
            System.out.println("No activities to remove.");
            return;
        }
        System.out.println("Current activities:");
        for (ExerciseLog log : logs) {
            System.out.println("  " + log.getId() + ": " + log.getName());
        }

        System.out.print("Enter activity ID to remove: ");
        int id = Integer.parseInt(scanner.nextLine().trim());

        if (activity.removeExerciseLog(id)) {
            System.out.println("Activity removed successfully.");
        } else {
            System.out.println("Activity not found with ID: " + id);
        }
    }

    /**
     * Prompts the user to select and remove an obstacle by ID.
     */
    private void removeObstacle() {
        if (grid == null) {
            System.out.println("No map exists.");
            return;
        }
        List<ObstaclePlacement> obstacles = grid.getObstacles();
        if (obstacles.isEmpty()) {
            System.out.println("No obstacles to remove.");
            return;
        }
        System.out.println("Current obstacles:");
        for (ObstaclePlacement obs : obstacles) {
            System.out.println("  ID: " + obs.getId() + ", " + obs);
        }

        System.out.print("Enter obstacle ID to remove: ");
        int id = Integer.parseInt(scanner.nextLine().trim());

        if (grid.removeObstacle(id)) {
            System.out.println("Obstacle removed successfully.");
        } else {
            System.out.println("Obstacle not found with ID: " + id);
        }
    }

    /**
     * Removes the entire map and all associated activities and obstacles.
     */
    private void removeMap() {
        if (grid == null) {
            System.out.println("No map exists.");
            return;
        }
        grid = null;
        activity.clear();
        ObstaclePlacement.resetIdCounter();
        ExerciseLog.resetIdCounter();
        System.out.println("Map and all associated data removed.");
    }

    /**
     * Exits the application with a farewell message.
     */
    private void quit() {
        System.out.println("Thank you for using Exercise Tracker. Stay active!");
        running = false;
    }
}
