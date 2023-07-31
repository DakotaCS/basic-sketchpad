package shape;

import java.awt.*;
import java.io.Serializable;

public abstract class Shape implements Serializable {
    public Point startPoint;
    protected Color color;
    protected int lineWidth;

    public Shape(Point startPoint) {
        this.startPoint = startPoint;
        this.color = Color.BLACK;
        this.lineWidth = 1;
    }

    public abstract void updateShape(Point endPoint);

    public abstract void updateShape(int dx, int dy);

    public abstract void draw(Graphics2D g2d);

    public abstract Shape copy(Point newPoint);

    public void setColor(Color drawingColor) {
        this.color = drawingColor;
    }

    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
    }

    public Color getColor() {
        return this.color;
    }
}