package shape;

import java.awt.*;
import java.io.Serializable;

public class Circle extends Shape implements Serializable {
    private int diameter;

    public Circle(Point startPoint) {
        super(startPoint);
        this.diameter = 0;
    }

    public void updateShape(Point endPoint) {
        int dx = endPoint.x - startPoint.x;
        int dy = endPoint.y - startPoint.y;
        diameter = Math.min(dx, dy);
    }

    public void updateShape(int dx, int dy) {
        startPoint.x += dx;
        startPoint.y += dy;
    }

    public void draw(Graphics2D g2d) {
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(lineWidth));
        g2d.drawOval(startPoint.x, startPoint.y, diameter, diameter);
    }

    public  Shape copy(Point newPoint) {
        Circle copy = new Circle(newPoint);
        copy.color = color;
        copy.lineWidth = lineWidth;
        copy.diameter = diameter;
        return copy;
    }
}