package mode;

import shape.Ellipse;

import java.awt.*;

public class EllipseMode implements Mode {
    public shape.Shape createShape(Point startPoint) {
        return new Ellipse(startPoint);
    }
}