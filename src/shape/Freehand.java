package shape;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

public class Freehand extends Shape implements Serializable {
    private ArrayList<Point> points;

    public Freehand(Point startPoint) {
        super(startPoint);
        points = new ArrayList<>();
        points.add(startPoint);
    }
    public void updateShape(Point endPoint) {
        points.add(endPoint);
    }

    public void updateShape(int dx, int dy) {
        for (Point point : points) {
            point.x += dx;
            point.y += dy;
        }
    }

    public void draw(Graphics2D g2d) {
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(lineWidth));
        for (int i = 0; i < points.size() - 1; i++) {
            Point p1 = points.get(i);
            Point p2 = points.get(i + 1);
            g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
        }
    }

    public Shape copy(Point newPoint) {
        Freehand copy = new Freehand(newPoint);
        copy.color = new Color(color.getRGB());
        copy.lineWidth = lineWidth;
        copy.points = new ArrayList<>(points.size());
        for (Point point : points) {
            copy.points.add(new Point(point.x - startPoint.x + newPoint.x, point.y - startPoint.y + newPoint.y));
        }
        return copy;
    }
}