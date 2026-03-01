package ca.umanitoba.cs.kanand.printers;

import ca.umanitoba.cs.kanand.model.ObstaclePlacement;

public final class ObstaclePlacementPrinter {
    private ObstaclePlacementPrinter() { }

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
