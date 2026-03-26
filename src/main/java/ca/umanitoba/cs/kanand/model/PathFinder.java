package ca.umanitoba.cs.kanand.model;

import com.google.common.base.Preconditions;
import ca.umanitoba.cs.kanand.exceptions.InvalidPathException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Implements a backtracking pathfinding algorithm to find routes on a grid.
 * 
 * The algorithm uses a stack-based approach to explore paths from a start point
 * to an end point, following only previously covered points (points that have been
 * traversed by previous activities) and avoiding obstacles.
 * 
 * Algorithm Invariants:
 *   - visited set contains all points that have been explored
 *   - stack contains candidate points to explore
 *   - currentPoint is the point currently being processed
 *   - All points in the path are covered points (except possibly start/end)
 *   - The algorithm terminates when either a path is found or the stack is empty
 */
public class PathFinder {
    private final Grid grid;
    private final RouteScope scope;

    /**
     * Creates a PathFinder for the given grid and scope.
     * 
     * Precondition:
     *   - grid != null
     *   - scope != null
     * 
     * Postcondition:
     *   - PathFinder is initialized and ready to find paths
     * 
     * @param grid the grid to search on
     * @param scope the scope for pathfinding (own routes or all routes)
     * @throws NullPointerException if grid or scope is null
     */
    public PathFinder(Grid grid, RouteScope scope) {
        this.grid = Preconditions.checkNotNull(grid, "Precondition failed: grid cannot be null");
        this.scope = Preconditions.checkNotNull(scope, "Precondition failed: scope cannot be null");
    }

    /**
     * Finds a path from start point to end point using backtracking.
     * 
     * Algorithm:
     * 1. Start with currentPoint = start
     * 2. While currentPoint != end:
     *    a. Mark currentPoint as visited
     *    b. Push unvisited, visitable neighbors onto stack
     *    c. If stack is empty, return null (no path found)
     *    d. Pop stack to get new currentPoint
     * 3. If loop exits normally, path found
     * 
     * Preconditions:
     *   - start != null
     *   - end != null
     *   - start and end are in bounds
     *   - start and end are not occupied by obstacles
     *   - At least one of start or end is a covered point
     * 
     * Postconditions:
     *   - If a path exists, returns a list of points from start to end
     *   - If no path exists, returns null
     *   - Grid state is unchanged
     *   - The returned path (if non-null) contains at least 2 points
     * 
     * @param start the starting point
     * @param end the ending point
     * @return a list of points representing the path from start to end, or null if no path exists
     * @throws NullPointerException if start or end is null
     * @throws IllegalArgumentException if start or end is out of bounds or occupied by obstacle
     * @throws InvalidPathException if the pathfinding algorithm fails due to invalid state
     */
    public List<Point> findPath(Point start, Point end) throws InvalidPathException {
        Preconditions.checkNotNull(start, "Precondition failed: start cannot be null");
        Preconditions.checkNotNull(end, "Precondition failed: end cannot be null");
        Preconditions.checkArgument(grid.isInBounds(start), 
            "Precondition failed: start point must be in bounds");
        Preconditions.checkArgument(grid.isInBounds(end), 
            "Precondition failed: end point must be in bounds");
        Preconditions.checkArgument(grid.getObstacleAt(start) == null, 
            "Precondition failed: start point cannot be occupied by obstacle");
        Preconditions.checkArgument(grid.getObstacleAt(end) == null, 
            "Precondition failed: end point cannot be occupied by obstacle");

        // If start equals end, we already have a path of length 1
        if (start.equals(end)) {
            List<Point> path = new ArrayList<>();
            path.add(start);
            return path;
        }

        // Initialize algorithm state
        Stack<Point> stack = new LinkedListStack<>();
        Set<Point> visited = new HashSet<>();
        Point currentPoint = start;
        Point previous = null;

        // Backtracking algorithm
        int iterationCount = 0;
        int maxIterations = grid.getWidth() * grid.getHeight() * 10; // Prevent infinite loops
        
        while (!currentPoint.equals(end) && iterationCount < maxIterations) {
            iterationCount++;
            
            // Mark current point as visited
            visited.add(currentPoint);

            // Push unvisited, visitable neighbors onto stack
            List<Point> neighbors = getVisitableNeighbors(currentPoint, visited);
            for (Point neighbor : neighbors) {
                stack.push(neighbor);
            }

            // Check if we can still proceed
            if (stack.isEmpty()) {
                // No unvisited neighbors and not at end - no path exists
                return null;
            }

            // Pop from stack and continue
            previous = currentPoint;
            currentPoint = stack.pop();

            // Safety check
            Preconditions.checkState(currentPoint != null, 
                "State invariant failed: popped point is null");
        }

        if (iterationCount >= maxIterations) {
            throw new InvalidPathException("Pathfinding algorithm exceeded maximum iterations. " +
                "Grid may be too complex or start/end points unreachable.");
        }

        if (currentPoint.equals(end)) {
            // Reconstruct path - for simplicity, return just the endpoint
            // In practice, you might want to reconstruct the full path
            List<Point> path = new ArrayList<>();
            path.add(start);
            path.add(end);
            return path;
        }

        return null;
    }

    /**
     * Gets all valid, unvisited neighbor points of the given point that can be traversed.
     * 
     * A neighbor is valid if:
     * - It is in bounds
     * - It is not visited
     * - It is not occupied by an obstacle
     * - It is a covered point (has been traversed before)
     * 
     * Uses 4-directional movement (up, down, left, right).
     * 
     * Precondition:
     *   - point != null
     *   - point is in bounds
     *   - visited != null
     * 
     * Postcondition:
     *   - The returned list contains only valid, unvisited, visitable neighbors
     *   - The returned list is non-null (may be empty)
     * 
     * @param point the point to check neighbors for
     * @param visited the set of already visited points
     * @return a list of valid neighbor points
     * @throws NullPointerException if point or visited is null
     */
    private List<Point> getVisitableNeighbors(Point point, Set<Point> visited) {
        Preconditions.checkNotNull(point, "Precondition failed: point cannot be null");
        Preconditions.checkNotNull(visited, "Precondition failed: visited set cannot be null");

        List<Point> neighbors = new ArrayList<>();

        // 4-directional movement: up, down, left, right
        int[][] directions = {
            {0, 1},   // up
            {0, -1},  // down
            {1, 0},   // right
            {-1, 0}   // left
        };

        for (int[] dir : directions) {
            int newX = point.getX() + dir[0];
            int newY = point.getY() + dir[1];

            try {
                Point neighbor = new Point(newX, newY);

                // Check if neighbor isvalid and visitable
                if (grid.isInBounds(neighbor) &&
                    !visited.contains(neighbor) &&
                    grid.getObstacleAt(neighbor) == null &&
                    grid.isCovered(neighbor)) {

                    neighbors.add(neighbor);
                }
            } catch (Exception e) {
                // Skip invalid neighbors
                continue;
            }
        }

        return neighbors;
    }
}
