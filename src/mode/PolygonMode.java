package mode;

import java.awt.*;

/**
 *
 * @author dakota
 * @version 1.0
 * @since 2023-07-31
 *
 *  Polygon mode allows the user to draw closed polygons.
 */
public class PolygonMode implements Mode {
    public shape.Shape createShape(Point startPoint) {
        return new shape.Polygon(startPoint);
    }
}