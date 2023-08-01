package shape;

import java.awt.*;
import java.io.Serializable;

/**
 *
 * @author dakota
 * @version 1.0
 * @since 2023-07-31
 *
 *  Defines a Shape of type Ellipse.
 */
public class Ellipse extends Shape implements Serializable {
    private int width;
    private int height;

    public Ellipse(Point startPoint) {
        super(startPoint);
        this.width = 0;
        this.height = 0;
    }
    public void updateShape(Point endPoint) {
        width = endPoint.x - startPoint.x;
        height = endPoint.y - startPoint.y;
    }
    public void updateShape(int dx, int dy) {
        startPoint.x += dx;
        startPoint.y += dy;
    }
    public void draw(Graphics2D g2d) {
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(lineWidth));
        g2d.drawOval(startPoint.x, startPoint.y, width, height);
    }
    public  Shape copy(Point newPoint) {
        Ellipse copy = new Ellipse(newPoint);
        copy.color = color;
        copy.lineWidth = lineWidth;
        copy.width = width;
        copy.height = height;
        return copy;
    }
}