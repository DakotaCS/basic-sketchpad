package mode;

import shape.Freehand;
import java.awt.*;

/**
 *
 * @author dakota
 * @version 1.0
 * @since 2023-07-31
 *
 *  Freehand mode allows the user to draw in freehand.
 */
public class FreehandMode implements Mode {
    public shape.Shape createShape(Point startPoint) {
        return new Freehand(startPoint);
    }
}