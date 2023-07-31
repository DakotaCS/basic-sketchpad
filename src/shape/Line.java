package shape;

import java.awt.*;
import java.io.Serializable;

public class Line extends Shape implements Serializable {
    private Point endPoint;

    public Line(Point startPoint) {
        super(startPoint);
        this.endPoint = startPoint;
    }

    public void updateShape(Point endPoint) {
        this.endPoint = endPoint;
    }

    public void updateShape(int dx, int dy) {
        startPoint.x += dx;
        startPoint.y += dy;
        endPoint.x += dx;
        endPoint.y += dy;
    }

    public void draw(Graphics2D g2d) {
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(lineWidth));
        g2d.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
    }

    public Shape copy(Point newPoint) {
        Line copy = new Line(newPoint);
        copy.color = color;
        copy.lineWidth = lineWidth;
        copy.endPoint = new Point(endPoint.x - startPoint.x + newPoint.x, endPoint.y - startPoint.y + newPoint.y);
        return copy;
    }
}