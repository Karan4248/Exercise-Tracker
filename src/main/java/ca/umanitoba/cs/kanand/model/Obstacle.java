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

    /**
     * Creates an Obstacle with the specified display name.
     *
     * @param displayName the name to display for this obstacle type
     */
    Obstacle(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Gets the display name of this obstacle.
     *
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }
}