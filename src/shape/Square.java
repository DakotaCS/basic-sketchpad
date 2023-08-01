package shape;

import java.awt.*;
import java.io.Serializable;

/**
 *
 * @author dakota
 * @version 1.0
 * @since 2023-07-31
 *
 *  Defines a Shape of type Square.
 */
public class Square extends Shape implements Serializable {
    private int sideLength;
    public Square(Point startPoint) {
        super(startPoint);
        this.sideLength = 0;
    }
    public void updateShape(Point endPoint) {
        int dx = endPoint.x - startPoint.x;
        int dy = endPoint.y - startPoint.y;
        sideLength = Math.min(dx, dy);
    }
    public void updateShape(int dx, int dy) {
        startPoint.x += dx;
        startPoint.y += dy;
    }
    public void draw(Graphics2D g2d) {
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(lineWidth));
        g2d.drawRect(startPoint.x, startPoint.y, sideLength, sideLength);
    }
    public  Shape copy(Point newPoint) {
        Square copy = new Square(newPoint);
        copy.color = color;
        copy.lineWidth = lineWidth;
        copy.sideLength = sideLength;
        return copy;
    }
}