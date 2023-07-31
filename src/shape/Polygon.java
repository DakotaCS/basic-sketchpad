package shape;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

public class Polygon extends Shape implements Serializable {
    private ArrayList<Point> points;

    public Polygon(Point startPoint) {
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
        int[] xPoints = new int[points.size()];
        int[] yPoints = new int[points.size()];

        for (int i = 0; i < points.size(); i++) {
            Point p = points.get(i);
            xPoints[i] = p.x;
            yPoints[i] = p.y;
        }
        g2d.drawPolygon(xPoints, yPoints, points.size());
    }

    public Shape copy(Point newPoint) {
        Polygon copy = new Polygon(newPoint);
        copy.color = new Color(color.getRGB());
        copy.lineWidth = lineWidth;
        copy.points = new ArrayList<>(points.size());
        for (Point point : points) {
            copy.points.add(new Point(point.x - startPoint.x + newPoint.x, point.y - startPoint.y + newPoint.y));
        }
        return copy;
    }
}