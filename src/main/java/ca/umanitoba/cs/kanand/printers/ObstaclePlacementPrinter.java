package ca.umanitoba.cs.kanand.printers;

import ca.umanitoba.cs.kanand.model.ObstaclePlacement;

public final class ObstaclePlacementPrinter {
    /**
     * Prevents instantiation of this utility class.
     */
    private ObstaclePlacementPrinter() { }

    /**
     * Formats an ObstaclePlacement into a readable string representation.
     *
     * @param obstacle the ObstaclePlacement to format
     * @return a formatted string with obstacle details
     */
    public static String format(ObstaclePlacement obstacle) {
        if (obstacle == null) return "<null obstacle>";
        return String.format(
                "Obstacle #%d: %s at %s, size %dx%d",
                obstacle.getId(),
                obstacle.getType().getDisplayName(),
                PointPrinter.format(obstacle.getLocation()),
                obstacle.getWidth(),
                obstacle.getHeight()
        );
    }
}
