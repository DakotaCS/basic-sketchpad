package mode;

import java.awt.*;

/**
 *
 * @author dakota
 * @version 1.0
 * @since 2023-07-31
 *
 *  Rectangle mode allows the user to draw rectangles.
 */
public class RectangleMode implements Mode {
    public shape.Shape createShape(Point startPoint) {
        return new shape.Rectangle(startPoint);
    }
}