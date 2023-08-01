package shape;

import java.awt.*;
import java.io.Serializable;

/**
 *
 * @author dakota
 * @version 1.0
 * @since 2023-07-31
 *
 *  Defines an abstract object that represents a Shape.
 */
public abstract class Shape implements Serializable {

    //every object has a starting point
    public Point startPoint;

    //the object color
    protected Color color;

    //the object line width
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
    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
    }
    public Color getColor() {
        return this.color;
    }
    public void setColor(Color drawingColor) {
        this.color = drawingColor;
    }
}