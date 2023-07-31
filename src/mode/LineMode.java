package mode;

import shape.Line;

import java.awt.*;

public class LineMode implements Mode {
    public shape.Shape createShape(Point startPoint) {
        return new Line(startPoint);
    }
}