package mode;

import java.awt.*;

/**
 *
 * @author dakota
 * @version 1.0
 * @since 2023-07-31
 *
 *  Defines an interface for different "modes" (i.e. states) that the sketchpad can be in.
 */
public interface Mode {
    shape.Shape createShape(Point startPoint);
}