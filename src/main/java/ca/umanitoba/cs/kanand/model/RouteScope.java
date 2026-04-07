package ca.umanitoba.cs.kanand.model;

/**
 * Enum representing the scope for path-finding operations.
 * 
 * MY_ROUTES_ONLY: Search using only the current user's own routes
 * ALL_ROUTES: Search using routes from all users the current user follows
 */
public enum RouteScope {
    MY_ROUTES_ONLY,
    ALL_ROUTES
}
