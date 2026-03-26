package ca.umanitoba.cs.kanand.model;

import ca.umanitoba.cs.kanand.test.TestResults;
import java.util.List;
import java.util.Set;

/**
 * Test suite for Grid class following COMP 2450 class methodology.
 * 
 * Tests domain behavior:
 * - Grid creation with various dimensions
 * - Bounds checking for valid/invalid points
 * - Obstacle management (add, remove, retrieval)
 * - Covered point tracking for pathfinding
 * - Edge cases: small and large grids, boundary points
 * 
 * Uses TestResults to track pass/fail. NO assertion statements.
 */
public class GridTest {
    private TestResults results = new TestResults();

    public static void main(String[] args) {
        GridTest tests = new GridTest();
        tests.runAllTests();
    }

    public void runAllTests() {
        System.out.println("\n╔" + "═".repeat(58) + "╗");
        System.out.println("║" + " ".repeat(22) + "Grid Test Suite" + " ".repeat(21) + "║");
        System.out.println("╚" + "═".repeat(58) + "╝\n");

        testGridCreation();
        testGridDimensions();
        testIsInBoundsValid();
        testIsInBoundsInvalid();
        testAddObstacle();
        testRemoveObstacle();
        testGetObstacleAt();
        testGetObstacles();
        testAddCoveredPoint();
        testIsCovered();
        testGetCoveredPoints();
        testClearCoveredPoints();
        testEdgeCaseSmallGrid();
        testEdgeCaseLargeGrid();

        printSummary();
    }

    private void testGridCreation() {
        try {
            Grid grid = new Grid(5, 5);
            if (grid.getWidth() == 5 && grid.getHeight() == 5) {
                results.pass("Grid creation with valid dimensions");
            } else {
                results.fail("Grid creation with valid dimensions");
            }
        } catch (Exception e) {
            results.fail("Unexpected exception in testGridCreation: " + e.getMessage());
        }
    }

    private void testGridDimensions() {
        try {
            int[] widths = {1, 5, 10, 100};
            int[] heights = {1, 5, 10, 100};
            boolean allCorrect = true;
            
            for (int i = 0; i < widths.length; i++) {
                Grid grid = new Grid(widths[i], heights[i]);
                if (grid.getWidth() != widths[i] || grid.getHeight() != heights[i]) {
                    allCorrect = false;
                    break;
                }
            }
            
            if (allCorrect) {
                results.pass("Grid stores multiple dimensions correctly");
            } else {
                results.fail("Grid stores multiple dimensions correctly");
            }
        } catch (Exception e) {
            results.fail("Unexpected exception in testGridDimensions: " + e.getMessage());
        }
    }

    private void testIsInBoundsValid() {
        try {
            Grid grid = new Grid(10, 10);
            Point[] validPoints = {
                new Point(0, 0),
                new Point(10, 10),
                new Point(5, 5)
            };
            
            boolean allInBounds = true;
            for (Point p : validPoints) {
                if (!grid.isInBounds(p)) {
                    allInBounds = false;
                    break;
                }
            }
            
            if (allInBounds) {
                results.pass("isInBounds returns true for valid points");
            } else {
                results.fail("isInBounds returns true for valid points");
            }
        } catch (Exception e) {
            results.fail("Unexpected exception in testIsInBoundsValid: " + e.getMessage());
        }
    }

    private void testIsInBoundsInvalid() {
        try {
            Grid grid = new Grid(10, 10);
            Point[] invalidPoints = {
                new Point(-1, 0),
                new Point(0, -1),
                new Point(11, 5),
                new Point(5, 11)
            };
            
            boolean allOutOfBounds = true;
            for (Point p : invalidPoints) {
                if (grid.isInBounds(p)) {
                    allOutOfBounds = false;
                    break;
                }
            }
            
            if (allOutOfBounds) {
                results.pass("isInBounds returns false for out of bounds points");
            } else {
                results.fail("isInBounds returns false for out of bounds points");
            }
        } catch (Exception e) {
            results.fail("Unexpected exception in testIsInBoundsInvalid: " + e.getMessage());
        }
    }

    private void testAddObstacle() {
        try {
            Grid grid = new Grid(10, 10);
            Point location = new Point(5, 5);
            ObstaclePlacement obstacle = new ObstaclePlacement(location, Obstacle.TREE);
            
            grid.addObstacle(obstacle);
            List<ObstaclePlacement> obstacles = grid.getObstacles();
            
            if (obstacles.size() == 1 && obstacles.get(0).equals(obstacle)) {
                results.pass("Add obstacle adds to obstacles list");
            } else {
                results.fail("Add obstacle adds to obstacles list");
            }
        } catch (Exception e) {
            results.fail("Unexpected exception in testAddObstacle: " + e.getMessage());
        }
    }

    private void testRemoveObstacle() {
        try {
            Grid grid = new Grid(10, 10);
            Point location = new Point(3, 3);
            ObstaclePlacement obstacle = new ObstaclePlacement(location, Obstacle.ROCK);
            
            grid.addObstacle(obstacle);
            int id = obstacle.getId();
            
            boolean removed = grid.removeObstacle(id);
            if (removed && grid.getObstacles().isEmpty()) {
                results.pass("Remove obstacle works correctly");
            } else {
                results.fail("Remove obstacle works correctly");
            }
        } catch (Exception e) {
            results.fail("Unexpected exception in testRemoveObstacle: " + e.getMessage());
        }
    }

    private void testGetObstacleAt() {
        try {
            Grid grid = new Grid(10, 10);
            Point location = new Point(7, 7);
            ObstaclePlacement obstacle = new ObstaclePlacement(location, Obstacle.BUILDING);
            
            grid.addObstacle(obstacle);
            
            ObstaclePlacement found = grid.getObstacleAt(location);
            if (found != null && found.equals(obstacle)) {
                results.pass("getObstacleAt returns correct obstacle at location");
            } else {
                results.fail("getObstacleAt returns correct obstacle at location");
            }
        } catch (Exception e) {
            results.fail("Unexpected exception in testGetObstacleAt: " + e.getMessage());
        }
    }

    private void testGetObstacles() {
        try {
            Grid grid = new Grid(10, 10);
            ObstaclePlacement obs1 = new ObstaclePlacement(new Point(1, 1), Obstacle.TREE);
            ObstaclePlacement obs2 = new ObstaclePlacement(new Point(2, 2), Obstacle.WATER);
            
            grid.addObstacle(obs1);
            grid.addObstacle(obs2);
            
            List<ObstaclePlacement> obstacles = grid.getObstacles();
            if (obstacles.size() == 2 && obstacles.contains(obs1) && obstacles.contains(obs2)) {
                results.pass("getObstacles returns all obstacles");
            } else {
                results.fail("getObstacles returns all obstacles");
            }
        } catch (Exception e) {
            results.fail("Unexpected exception in testGetObstacles: " + e.getMessage());
        }
    }

    private void testAddCoveredPoint() {
        try {
            Grid grid = new Grid(10, 10);
            Point point = new Point(4, 4);
            
            grid.addCoveredPoint(point);
            if (grid.isCovered(point)) {
                results.pass("addCoveredPoint adds point to covered set");
            } else {
                results.fail("addCoveredPoint adds point to covered set");
            }
        } catch (Exception e) {
            results.fail("Unexpected exception in testAddCoveredPoint: " + e.getMessage());
        }
    }

    private void testIsCovered() {
        try {
            Grid grid = new Grid(10, 10);
            Point covered = new Point(2, 3);
            Point uncovered = new Point(8, 8);
            
            grid.addCoveredPoint(covered);
            
            if (grid.isCovered(covered) && !grid.isCovered(uncovered)) {
                results.pass("isCovered correctly identifies covered points");
            } else {
                results.fail("isCovered correctly identifies covered points");
            }
        } catch (Exception e) {
            results.fail("Unexpected exception in testIsCovered: " + e.getMessage());
        }
    }

    private void testGetCoveredPoints() {
        try {
            Grid grid = new Grid(10, 10);
            Point p1 = new Point(1, 1);
            Point p2 = new Point(5, 5);
            Point p3 = new Point(9, 9);
            
            grid.addCoveredPoint(p1);
            grid.addCoveredPoint(p2);
            grid.addCoveredPoint(p3);
            
            Set<Point> covered = grid.getCoveredPoints();
            if (covered.size() == 3 && covered.contains(p1) && covered.contains(p2) && covered.contains(p3)) {
                results.pass("getCoveredPoints returns all covered points");
            } else {
                results.fail("getCoveredPoints returns all covered points");
            }
        } catch (Exception e) {
            results.fail("Unexpected exception in testGetCoveredPoints: " + e.getMessage());
        }
    }

    private void testClearCoveredPoints() {
        try {
            Grid grid = new Grid(10, 10);
            grid.addCoveredPoint(new Point(1, 1));
            grid.addCoveredPoint(new Point(5, 5));
            
            grid.clearCoveredPoints();
            
            if (grid.getCoveredPoints().isEmpty() && !grid.isCovered(new Point(1, 1))) {
                results.pass("clearCoveredPoints removes all covered points");
            } else {
                results.fail("clearCoveredPoints removes all covered points");
            }
        } catch (Exception e) {
            results.fail("Unexpected exception in testClearCoveredPoints: " + e.getMessage());
        }
    }

    private void testEdgeCaseSmallGrid() {
        try {
            Grid grid = new Grid(1, 1);
            Point point = new Point(0, 0);
            Point outside = new Point(1, 1);
            
            if (grid.isInBounds(point) && !grid.isInBounds(outside) && grid.getWidth() == 1) {
                results.pass("Edge case: 1x1 grid works correctly");
            } else {
                results.fail("Edge case: 1x1 grid works correctly");
            }
        } catch (Exception e) {
            results.fail("Unexpected exception in testEdgeCaseSmallGrid: " + e.getMessage());
        }
    }

    private void testEdgeCaseLargeGrid() {
        try {
            Grid grid = new Grid(100, 100);
            Point corner = new Point(100, 100);
            Point inside = new Point(50, 50);
            
            if (grid.isInBounds(corner) && grid.isInBounds(inside) && grid.getWidth() == 100) {
                results.pass("Edge case: large grid (100x100) works correctly");
            } else {
                results.fail("Edge case: large grid (100x100) works correctly");
            }
        } catch (Exception e) {
            results.fail("Unexpected exception in testEdgeCaseLargeGrid: " + e.getMessage());
        }
    }

    private void printSummary() {
        System.out.println("\n" + "─".repeat(60));
        System.out.printf("Grid: %d passed, %d failed out of %d tests%n", 
            results.successes(), results.failures(), results.totalTests());
        System.out.println("─".repeat(60) + "\n");
    }
}
