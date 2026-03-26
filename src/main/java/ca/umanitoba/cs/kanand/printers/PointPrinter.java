package ca.umanitoba.cs.kanand.printers;

import ca.umanitoba.cs.kanand.model.Point;

public final class PointPrinter {
    /**
     * Prevents instantiation of this utility class.
     */
    private PointPrinter() { }

    /**
     * Formats a Point into a readable coordinate string representation.
     *
     * @param point the Point to format
     * @return a formatted string showing the coordinates
     */
    public static String format(Point point) {
        if (point == null) return "<null point>";
        return "(" + point.getX() + ", " + point.getY() + ")";
    }
}
