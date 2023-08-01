package mode;

import shape.Square;
import java.awt.*;

/**
 *
 * @author dakota
 * @version 1.0
 * @since 2023-07-31
 *
 *  Square mode allows the user to draw squares.
 */
public class SquareMode implements Mode {
    public shape.Shape createShape(Point startPoint) {
        return new Square(startPoint);
    }
}