package mode;

import shape.Circle;
import java.awt.*;

/**
 *
 * @author dakota
 * @version 1.0
 * @since 2023-07-31
 *
 *  Circle mode allows the user to draw circles.
 */
public class CircleMode implements Mode {
    public shape.Shape createShape(Point startPoint) {
        return new Circle(startPoint);
    }
}