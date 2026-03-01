package ca.umanitoba.cs.kanand.model;

/**
 * Enumeration of obstacle types.
 */
public enum Obstacle {
    TREE("Tree"),
    BUILDING("Building"),
    ROCK("Rock"),
    WATER("Water");

    private final String displayName;

    Obstacle(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}