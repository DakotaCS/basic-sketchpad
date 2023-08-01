import mode.Mode;
import shape.Shape;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Optional;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 *
 * @author dakota
 * @version 1.0
 * @since 2023-07-31
 *
 *  Defines operations that pertain to the Canvas object of the Sketchpad.
 */
class Canvas extends JPanel implements Serializable {
    private Color drawingColor;
    private int lineWidth;
    private ArrayList<Shape> shapes;
    private Stack<Shape> redoStack = new Stack<>();
    private shape.Shape currentShape;
    private Point startPoint;
    private Mode mode;
    private JComboBox<String> objectDropdownBox;
    private shape.Shape selectedShape;
    private Color selectedShapeColor;

    public Canvas() {

        setBackground(Color.WHITE);
        drawingColor = Color.BLACK;
        lineWidth = 1;
        shapes = new ArrayList<>();

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                try {
                    startPoint = e.getPoint();
                    currentShape = mode.createShape(startPoint);
                    currentShape.setColor(drawingColor);
                    currentShape.setLineWidth(lineWidth);
                    shapes.add(currentShape);
                }
                catch(NullPointerException ex)  {}
            }

            public void mouseReleased(MouseEvent e) {
                updateObjectDropdownBox();
                currentShape = null;

            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (currentShape != null) {
                    Point currentPoint = e.getPoint();
                    currentShape.updateShape(currentPoint);
                    repaint();
                }
            }
        });
    }

    public ArrayList<shape.Shape> getShapes() {
        return shapes;
    }

    public shape.Shape getSelectedShape() {
        String item = (String) objectDropdownBox.getSelectedItem();
        Optional<Shape> result = shapes.stream()
                .filter(Shape -> Shape.toString().equals(item))
                .findFirst();
        if (result.isPresent()) {
            shape.Shape selectedShape = result.get();
            return selectedShape;
        }
        else {
            return null;
        }
    }

    public void copySelectedShape(Point pointToMoveTo, shape.Shape shape) {
        if (shape != null) {
            shape.Shape copy = shape.copy(new Point(pointToMoveTo));
            shapes.add(copy);
            updateObjectDropdownBox();
            repaint();
        }
    }

    public void moveSelectedShape(Point pointToMoveTo, shape.Shape shape) {
        if (shape != null) {
            int dx = pointToMoveTo.x - shape.startPoint.x;
            int dy = pointToMoveTo.y - shape.startPoint.y;
            shape.updateShape(dx, dy);
            repaint();
        }
    }

    public void setObjectDropdownBox(JComboBox<String> objectDropdownBox) {
        this.objectDropdownBox = objectDropdownBox;
    }

    void updateObjectDropdownBox() {
        ArrayList<String> shapeList = getShapeListToString();
        objectDropdownBox.removeAllItems();
        objectDropdownBox.addItem("none");
        for (String shape : shapeList) {
            objectDropdownBox.addItem(shape);
        }
    }

    public void setDrawingColor(Color color) {
        drawingColor = color;
    }

    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public void deleteSelectedShape() {
        if(selectedShape!=null) {
            Optional<shape.Shape> result = shapes.stream()
                    .filter(Shape -> Shape.toString().equals(selectedShape.toString()))
                    .findFirst();
            if (result.isPresent()) {
                shape.Shape selectedShape = result.get();
                redoStack.push(selectedShape);
                shapes.remove(selectedShape);
                repaint();
            }
            updateObjectDropdownBox();
        }
    }

    public void setSelectedShape(String shapeName) {
        if(selectedShape!=null) {
            selectedShape.setColor(selectedShapeColor);
        }

        if (shapeName!=null && shapeName.equals("none") &&
                selectedShape!=null &&  selectedShapeColor!=null) {
            this.selectedShape.setColor(selectedShapeColor);
            this.selectedShape=null;
            this.selectedShapeColor=null;
            repaint();
        }

        this.selectedShape=null;
        this.selectedShapeColor=null;

        Optional<shape.Shape> result = shapes.stream()
                .filter(Shape -> Shape.toString().equals(shapeName))
                .findFirst();
        if (result.isPresent()) {
            shape.Shape selectedShape = result.get();
            this.selectedShapeColor = selectedShape.getColor();
            this.selectedShape = selectedShape;
            selectedShape.setColor(Color.red);
            repaint();
        }
    }

    public void popObject() {
        if (!shapes.isEmpty()) {
            shape.Shape shape = shapes.remove(shapes.size() - 1);
            redoStack.push(shape);
            repaint();
        }
        updateObjectDropdownBox();
    }

    public void pushObject() {
        try {
            if (redoStack.peek() != null) {
                shape.Shape shape = redoStack.pop();
                shapes.add(shape);
                repaint();
            }
        } catch (EmptyStackException e) {
            System.out.println("Nothing to redo");
        }
        updateObjectDropdownBox();
    }

    public void resetCanvas() {
        shapes.clear();
        while (!redoStack.isEmpty()) {
            redoStack.pop();
        }
        objectDropdownBox.removeAllItems();
        objectDropdownBox.addItem("none");
        repaint();
    }

    public ArrayList<String> getShapeListToString() {
        return shapes.stream()
                .map(shape.Shape::toString)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        for (shape.Shape shape : shapes) {
            shape.draw(g2d);
        }
    }
}