package mode;

import java.awt.*;

public class PolygonMode implements Mode {
    public shape.Shape createShape(Point startPoint) {
        return new shape.Polygon(startPoint);
    }
}