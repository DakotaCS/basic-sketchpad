package mode;

import shape.Square;

import java.awt.*;

public class SquareMode implements Mode {
    public shape.Shape createShape(Point startPoint) {
        return new Square(startPoint);
    }
}