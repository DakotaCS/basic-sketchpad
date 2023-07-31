package mode;

import shape.Circle;

import java.awt.*;

public class CircleMode implements Mode {
    public shape.Shape createShape(Point startPoint) {
        return new Circle(startPoint);
    }
}