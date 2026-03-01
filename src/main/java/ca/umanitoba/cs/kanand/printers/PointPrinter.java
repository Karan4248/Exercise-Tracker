package ca.umanitoba.cs.kanand.printers;

import ca.umanitoba.cs.kanand.model.Point;

public final class PointPrinter {
    private PointPrinter() { }

    public static String format(Point point) {
        if (point == null) return "<null point>";
        return "(" + point.getX() + ", " + point.getY() + ")";
    }
}
