package mode;

import shape.Freehand;

import java.awt.*;

public class FreehandMode implements Mode {
    public shape.Shape createShape(Point startPoint) {
        return new Freehand(startPoint);
    }
}