package ca.umanitoba.cs.kanand.model;

/**
 * Enum representing the scope for path-finding operations.
 * 
 * MY_ROUTES_ONLY: Search using only the current user's own routes
 * ALL_ROUTES: Search using routes from all users the current user follows
 */
public enum RouteScope {
    MY_ROUTES_ONLY("Only my routes"),
    ALL_ROUTES("All followed users' routes");

    private final String description;

    RouteScope(String description) {
        this.description = description;
    }

    /**
     * Gets the human-readable description of this scope.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }
}
