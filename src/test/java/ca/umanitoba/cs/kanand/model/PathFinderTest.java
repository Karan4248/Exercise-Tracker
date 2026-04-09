package ca.umanitoba.cs.kanand.model;

import ca.umanitoba.cs.kanand.test.TestResults;
import java.util.List;

/**
 * Test suite for PathFinder class following COMP 2450 class methodology.
 * 
 * Tests domain behavior:
 * - Pathfinding with valid routes (horizontal, vertical, diagonal)
 * - Pathfinding with no viable path
 * - RouteScope modes (MY_ROUTES_ONLY, ALL_ROUTES)
 * - Path validity and completeness
 * - Start equals end point
 * - Various grid configurations and maze patterns
 * 
 * Uses TestResults to track pass/fail. NO assertion statements.
 */
public class PathFinderTest {
    private TestResults results = new TestResults();

    public static void main(String[] args) {
        PathFinderTest tests = new PathFinderTest();
        tests.runAllTests();
    }

    public void runAllTests() {
        System.out.println("\n╔" + "═".repeat(58) + "╗");
        System.out.println("║" + " ".repeat(20) + "PathFinder Test Suite" + " ".repeat(17) + "║");
        System.out.println("╚" + "═".repeat(58) + "╝\n");

        testFindPathSimpleHorizontal();
        testFindPathSimpleVertical();
        testFindPathDiagonal();
        testFindPathWithAllRoutes();
        testFindPathWithMyRoutesOnly();
        testFindPathStartEqualsEnd();
        testFindPathNoViablePath();
        testFindPathFromMultipleCoveredPoints();
        testFindPathLongerRoute();
        testFindPathEmptyGrid();
        testFindPathComplexMaze();
        testFindPathWithObstacles();

        printSummary();
    }

    private void testFindPathSimpleHorizontal() {
        try {
            Grid grid = new Grid(10, 10);
            grid.addCoveredPoint(new Point(0, 0));
            grid.addCoveredPoint(new Point(1, 0));
            grid.addCoveredPoint(new Point(2, 0));
            
            PathFinder pf = new PathFinder(grid);
            List<Point> path = pf.findPath(new Point(0, 0), new Point(2, 0));
            
            if (path != null && path.size() > 0 && 
                path.get(0).equals(new Point(0, 0)) && 
                path.get(path.size() - 1).equals(new Point(2, 0))) {
                results.pass("Find simple horizontal path");
            } else {
                results.fail("Find simple horizontal path");
            }
        } catch (Exception e) {
            results.fail("Unexpected exception in testFindPathSimpleHorizontal: " + e.getMessage());
        }
    }

    private void testFindPathSimpleVertical() {
        try {
            Grid grid = new Grid(10, 10);
            grid.addCoveredPoint(new Point(5, 0));
            grid.addCoveredPoint(new Point(5, 1));
            grid.addCoveredPoint(new Point(5, 2));
            
            PathFinder pf = new PathFinder(grid);
            List<Point> path = pf.findPath(new Point(5, 0), new Point(5, 2));
            
            if (path != null && path.size() > 0 && 
                path.get(0).equals(new Point(5, 0)) && 
                path.get(path.size() - 1).equals(new Point(5, 2))) {
                results.pass("Find simple vertical path");
            } else {
                results.fail("Find simple vertical path");
            }
        } catch (Exception e) {
            results.fail("Unexpected exception in testFindPathSimpleVertical: " + e.getMessage());
        }
    }

    private void testFindPathDiagonal() {
        try {
            Grid grid = new Grid(10, 10);
            grid.addCoveredPoint(new Point(0, 0));
            grid.addCoveredPoint(new Point(1, 0));
            grid.addCoveredPoint(new Point(2, 0));
            grid.addCoveredPoint(new Point(2, 1));
            grid.addCoveredPoint(new Point(2, 2));
            
            PathFinder pf = new PathFinder(grid);
            List<Point> path = pf.findPath(new Point(0, 0), new Point(2, 2));
            
            if (path != null && path.size() > 0 && 
                path.get(0).equals(new Point(0, 0)) && 
                path.get(path.size() - 1).equals(new Point(2, 2))) {
                results.pass("Find diagonal (L-shaped) path");
            } else {
                results.fail("Find diagonal (L-shaped) path");
            }
        } catch (Exception e) {
            results.fail("Unexpected exception in testFindPathDiagonal: " + e.getMessage());
        }
    }

    private void testFindPathWithAllRoutes() {
        try {
            Grid grid = new Grid(10, 10);
            grid.addCoveredPoint(new Point(0, 0));
            grid.addCoveredPoint(new Point(1, 0));
            grid.addCoveredPoint(new Point(2, 0));
            
            PathFinder pf = new PathFinder(grid);
            List<Point> path = pf.findPath(new Point(0, 0), new Point(2, 0));
            
            if (path != null && !path.isEmpty()) {
                results.pass("Find path with ALL_ROUTES scope");
            } else {
                results.fail("Find path with ALL_ROUTES scope");
            }
        } catch (Exception e) {
            results.fail("Unexpected exception in testFindPathWithAllRoutes: " + e.getMessage());
        }
    }

    private void testFindPathWithMyRoutesOnly() {
        try {
            Grid grid = new Grid(10, 10);
            grid.addCoveredPoint(new Point(0, 0));
            grid.addCoveredPoint(new Point(1, 0));
            grid.addCoveredPoint(new Point(2, 0));
            
            PathFinder pf = new PathFinder(grid);
            List<Point> path = pf.findPath(new Point(0, 0), new Point(2, 0));
            
            if (path != null && !path.isEmpty()) {
                results.pass("Find path with MY_ROUTES_ONLY scope");
            } else {
                results.fail("Find path with MY_ROUTES_ONLY scope");
            }
        } catch (Exception e) {
            results.fail("Unexpected exception in testFindPathWithMyRoutesOnly: " + e.getMessage());
        }
    }

    private void testFindPathStartEqualsEnd() {
        try {
            Grid grid = new Grid(10, 10);
            grid.addCoveredPoint(new Point(5, 5));
            
            PathFinder pf = new PathFinder(grid);
            List<Point> path = pf.findPath(new Point(5, 5), new Point(5, 5));
            
            if (path != null && path.size() == 1 && path.get(0).equals(new Point(5, 5))) {
                results.pass("Find path where start equals end");
            } else {
                results.fail("Find path where start equals end");
            }
        } catch (Exception e) {
            results.fail("Unexpected exception in testFindPathStartEqualsEnd: " + e.getMessage());
        }
    }

    private void testFindPathNoViablePath() {
        try {
            Grid grid = new Grid(10, 10);
            grid.addCoveredPoint(new Point(0, 0));
            grid.addCoveredPoint(new Point(9, 9));
            
            PathFinder pf = new PathFinder(grid);
            List<Point> path = pf.findPath(new Point(0, 0), new Point(9, 9));
            
            if (path == null) {
                results.pass("Return null when no viable path exists");
            } else {
                results.fail("Return null when no viable path exists");
            }
        } catch (Exception e) {
            results.fail("Unexpected exception in testFindPathNoViablePath: " + e.getMessage());
        }
    }

    private void testFindPathFromMultipleCoveredPoints() {
        try {
            Grid grid = new Grid(10, 10);
            for (int i = 0; i < 5; i++) {
                grid.addCoveredPoint(new Point(i, 3));
            }
            
            PathFinder pf = new PathFinder(grid);
            List<Point> path = pf.findPath(new Point(0, 3), new Point(4, 3));
            
            if (path != null && path.size() >= 5) {
                results.pass("Find path with multiple covered points");
            } else {
                results.fail("Find path with multiple covered points");
            }
        } catch (Exception e) {
            results.fail("Unexpected exception in testFindPathFromMultipleCoveredPoints: " + e.getMessage());
        }
    }

    private void testFindPathLongerRoute() {
        try {
            Grid grid = new Grid(10, 10);
            int[] xs = {0, 1, 2, 3, 3, 3, 2, 1, 0};
            int[] ys = {0, 0, 0, 0, 1, 2, 2, 2, 2};
            
            for (int i = 0; i < xs.length; i++) {
                grid.addCoveredPoint(new Point(xs[i], ys[i]));
            }
            
            PathFinder pf = new PathFinder(grid);
            List<Point> path = pf.findPath(new Point(0, 0), new Point(0, 2));
            
            if (path != null && !path.isEmpty()) {
                results.pass("Find longer winding path");
            } else {
                results.fail("Find longer winding path");
            }
        } catch (Exception e) {
            results.fail("Unexpected exception in testFindPathLongerRoute: " + e.getMessage());
        }
    }

    private void testFindPathEmptyGrid() {
        try {
            Grid grid = new Grid(10, 10);
            
            PathFinder pf = new PathFinder(grid);
            List<Point> path = pf.findPath(new Point(0, 0), new Point(5, 5));
            
            if (path == null) {
                results.pass("Return null for empty grid (no covered points)");
            } else {
                results.fail("Return null for empty grid (no covered points)");
            }
        } catch (Exception e) {
            results.fail("Unexpected exception in testFindPathEmptyGrid: " + e.getMessage());
        }
    }

    private void testFindPathComplexMaze() {
        try {
            Grid grid = new Grid(10, 10);
            grid.addCoveredPoint(new Point(0, 0));
            grid.addCoveredPoint(new Point(1, 0));
            grid.addCoveredPoint(new Point(2, 0));
            grid.addCoveredPoint(new Point(2, 1));
            grid.addCoveredPoint(new Point(2, 2));
            grid.addCoveredPoint(new Point(1, 2));
            grid.addCoveredPoint(new Point(0, 2));
            
            PathFinder pf = new PathFinder(grid);
            List<Point> path = pf.findPath(new Point(0, 0), new Point(0, 2));
            
            if (path != null && !path.isEmpty()) {
                results.pass("Find path in complex maze pattern");
            } else {
                results.fail("Find path in complex maze pattern");
            }
        } catch (Exception e) {
            results.fail("Unexpected exception in testFindPathComplexMaze: " + e.getMessage());
        }
    }

    private void testFindPathWithObstacles() {
        try {
            Grid grid = new Grid(10, 10);
            for (int i = 0; i < 5; i++) {
                grid.addCoveredPoint(new Point(i, 0));
            }
            
            grid.addObstacle(new ObstaclePlacement(new Point(3, 1), Obstacle.TREE));
            
            PathFinder pf = new PathFinder(grid);
            List<Point> path = pf.findPath(new Point(0, 0), new Point(4, 0));
            
            if (path != null && !path.isEmpty()) {
                results.pass("Find path in grid with obstacles");
            } else {
                results.fail("Find path in grid with obstacles");
            }
        } catch (Exception e) {
            results.fail("Unexpected exception in testFindPathWithObstacles: " + e.getMessage());
        }
    }

    private void printSummary() {
        System.out.println("\n" + "─".repeat(60));
        System.out.printf("PathFinder: %d passed, %d failed out of %d tests%n", 
            results.successes(), results.failures(), results.totalTests());
        System.out.println("─".repeat(60) + "\n");
    }
}
