package mode;

import java.awt.*;

public class RectangleMode implements Mode {
    public shape.Shape createShape(Point startPoint) {
        return new shape.Rectangle(startPoint);
    }
}