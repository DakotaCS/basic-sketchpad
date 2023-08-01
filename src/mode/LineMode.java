package mode;

import shape.Line;
import java.awt.*;

/**
 *
 * @author dakota
 * @version 1.0
 * @since 2023-07-31
 *
 *  Line mode allows the user to draw lines.
 */
public class LineMode implements Mode {
    public shape.Shape createShape(Point startPoint) {
        return new Line(startPoint);
    }
}