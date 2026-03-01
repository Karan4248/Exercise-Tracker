package ca.umanitoba.cs.kanand;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * REPL (Read-Eval-Print-Loop) for the Exercise Tracker application.
 */
public class ExerciseTrackerREPL {
    private final Scanner scanner;
    private Grid grid;
    private final Activity activity;
    private boolean running;

    public ExerciseTrackerREPL(Scanner scanner) {
        this.scanner = scanner;
        this.grid = null;
        this.activity = new Activity();
        this.running = true;
    }

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

    private void addObstacle() {
        if (grid == null) {
            System.out.println("No map exists. Create one first with ADD MAP.");
            return;
        }

        System.out.print("Enter obstacle position (x y): ");
        String[] coords = scanner.nextLine().trim().split("\\s+");
        int x = Integer.parseInt(coords[0]);
        int y = Integer.parseInt(coords[1]);

        System.out.print("Enter obstacle width: ");
        int width = Integer.parseInt(scanner.nextLine().trim());

        System.out.print("Enter obstacle height: ");
        int height = Integer.parseInt(scanner.nextLine().trim());

        System.out.println("Obstacle types: TREE, BUILDING, ROCK, WATER");
        System.out.print("Enter obstacle type: ");
        String typeStr = scanner.nextLine().trim().toUpperCase();
        Obstacle type = Obstacle.valueOf(typeStr);

        Point location = new Point(x, y);
        ObstaclePlacement obstacle = new ObstaclePlacement(location, width, height, type);
        grid.addObstacle(obstacle);

        System.out.println("Obstacle added: " + obstacle);
    }

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
                continue;
            }
            route.add(point);
        }

        Exercise exercise = new Exercise(exerciseType, unit);
        ExerciseLog log = new ExerciseLog(name, exercise, route, distance);
        activity.addExerciseLog(log);

        System.out.println("Activity added: " + log);
    }

    private void showMap() {
        if (grid == null) {
            System.out.println("No map exists. Create one first with ADD MAP.");
            return;
        }

        printMapGrid(null);
        printLegend();
        printActivitySummary();
    }

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
        System.out.println("\nShowing activity: " + selectedLog);
        printLegend();
    }

    private void printMapGrid(ExerciseLog selectedActivity) {
        int width = grid.getWidth();
        int height = grid.getHeight();

        boolean[][] routeGrid = new boolean[width][height];

        List<ExerciseLog> logsToShow = (selectedActivity != null)
                ? List.of(selectedActivity)
                : activity.getAllLogs();

        for (ExerciseLog log : logsToShow) {
            for (Point p : log.getPoints()) {
                if (p.getX() < width && p.getY() < height) {
                    routeGrid[p.getX()][p.getY()] = true;
                }
            }
        }

        System.out.println("\n   +" + "-".repeat(width * 2 + 1) + "+");
        for (int y = height - 1; y >= 0; y--) {
            System.out.printf("%2d | ", y);
            for (int px = 0; px < width; px++) {
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
        System.out.println("   +" + "-".repeat(width * 2 + 1) + "+");
        System.out.print("     ");
        for (int px = 0; px < width; px++) {
            System.out.printf("%-2d", px);
        }
        System.out.println();
    }

    private void printLegend() {
        System.out.println("""
            
            ╔═══════════════════════════════════════════════════════════════╗
            ║                         MAP LEGEND                            ║
            ╠═══════════════════════════════════════════════════════════════╣
            ║   .  = Empty location                                         ║
            ║   >  = Activity route point                                   ║
            ║   *  = Obstacle                                               ║
            ╚═══════════════════════════════════════════════════════════════╝
            
            ┌─────────────────────────────────────────────────────────────────┐
            │  "The Legend of the Tireless Runner"                            │
            │                                                                 │
            │  Long ago, there was a runner named Wisakedjak who ran          │
            │  across the endless prairies, never tiring, never stopping.     │
            │  They said he could outrun the wind itself! Each step he        │
            │  took left a mark upon the land, creating paths that others     │
            │  would follow for generations. Track your journey, and          │
            │  perhaps one day, your routes will become legendary too.        │
            └─────────────────────────────────────────────────────────────────┘
            """);
    }

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
            System.out.println("  " + obs);
        }
        System.out.println("═══════════════════════════════════════\n");
    }

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
            System.out.println("  " + log);
        }
        System.out.println("═══════════════════════════════════════════════════════════════\n");
    }

    private void removeActivity() {
        if (activity.getAllLogs().isEmpty()) {
            System.out.println("No activities to remove.");
            return;
        }

        System.out.println("Current activities:");
        for (ExerciseLog log : activity.getAllLogs()) {
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
            System.out.println("  " + obs.getId() + ": " + obs.getType().getDisplayName() + " at " + obs.getLocation());
        }

        System.out.print("Enter obstacle ID to remove: ");
        int id = Integer.parseInt(scanner.nextLine().trim());

        if (grid.removeObstacle(id)) {
            System.out.println("Obstacle removed successfully.");
        } else {
            System.out.println("Obstacle not found with ID: " + id);
        }
    }

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

    private void quit() {
        System.out.println("Thank you for using Exercise Tracker. Stay active! 🏃");
        running = false;
    }
}
