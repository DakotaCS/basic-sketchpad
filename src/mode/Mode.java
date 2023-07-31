package mode;

import java.awt.*;

public interface Mode {
    shape.Shape createShape(Point startPoint);
}