package mode;

import shape.Ellipse;
import java.awt.*;

/**
 *
 * @author dakota
 * @version 1.0
 * @since 2023-07-31
 *
 *  Ellipse mode allows the user to draw ellipses.
 */
public class EllipseMode implements Mode {
    public shape.Shape createShape(Point startPoint) {
        return new Ellipse(startPoint);
    }
}