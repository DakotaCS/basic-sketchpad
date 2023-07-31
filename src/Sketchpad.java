import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Optional;
import java.util.Stack;
import java.util.stream.Collectors;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Sketchpad extends JFrame implements Serializable{
    private Canvas canvas;
    private Mode mode;
    public Sketchpad() {
        setTitle("Sketchpad");
        setSize(1200, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        canvas = new Canvas();
        getContentPane().add(canvas, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        getContentPane().add(controlPanel, BorderLayout.NORTH);

        JLabel modeLabel = new JLabel("Mode:");
        controlPanel.add(modeLabel);

        JComboBox<String> modeComboBox = new JComboBox<>();
        modeComboBox.addItem("Freehand");
        modeComboBox.addItem("Line");
        modeComboBox.addItem("Rectangle");
        modeComboBox.addItem("Ellipse");
        modeComboBox.addItem("Square");
        modeComboBox.addItem("Circle");
        modeComboBox.addItem("Polygon");
        modeComboBox.addItem("Select");
        controlPanel.add(modeComboBox);

        JButton colorButton = new JButton("Select Color");
        controlPanel.add(colorButton);

        JButton popButton = new JButton("Undo");
        controlPanel.add(popButton);

        JButton pushButton = new JButton("Redo");
        controlPanel.add(pushButton);

        JButton resetButton = new JButton("Reset Canvas");
        controlPanel.add(resetButton);

        JSlider lineWidthSlider = new JSlider(JSlider.HORIZONTAL, 1, 10, 1);
        controlPanel.add(lineWidthSlider);

        JComboBox<String> objectDropdownBox = new JComboBox<>();
        controlPanel.add(objectDropdownBox);
        objectDropdownBox.addItem("none");

        JButton deleteButton = new JButton("Delete");
        controlPanel.add(deleteButton);

        JButton moveButton = new JButton("Move");
        controlPanel.add(moveButton);

        JButton copyButton = new JButton("Copy");
        controlPanel.add(copyButton);

        JButton saveButton = new JButton("Save");
        controlPanel.add(saveButton);

        JButton loadButton = new JButton("Load");
        controlPanel.add(loadButton);

        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveCanvasToFile();
            }
        });

        loadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadCanvasFromFile();
            }
        });

        copyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                modeComboBox.setSelectedItem("Select");
                Shape currentShape = getSelectedShape();

                canvas.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent em) {
                        canvas.copySelectedShape(em.getPoint(), currentShape);
                        canvas.removeMouseListener(this);
                        modeComboBox.setSelectedItem("Freehand");
                    }
                });
            }
        });

        moveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                modeComboBox.setSelectedItem("Select");
                Shape currentShape = getSelectedShape();

                canvas.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent em) {
                        canvas.moveSelectedShape(em.getPoint(), currentShape);
                        canvas.removeMouseListener(this);
                        modeComboBox.setSelectedItem("Freehand");
                    }
                });
            }
        });

        objectDropdownBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedShape = (String) objectDropdownBox.getSelectedItem();
                setSelectedShape(selectedShape);
            }
        });

        canvas.setObjectDropdownBox(objectDropdownBox);

        modeComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedMode = (String) modeComboBox.getSelectedItem();
                setMode(selectedMode);
            }
        });

        colorButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Color selectedColor = JColorChooser.showDialog(Sketchpad.this, "Select Color", Color.BLACK);
                canvas.setDrawingColor(selectedColor);
            }
        });

        popButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                canvas.popObject();
            }
        });

        pushButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                canvas.pushObject();
            }
        });

        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                modeComboBox.setSelectedItem("Freehand");
                canvas.resetCanvas();
            }
        });

        lineWidthSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int lineWidth = lineWidthSlider.getValue();
                canvas.setLineWidth(lineWidth);
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                canvas.deleteSelectedShape();
            }
        });
        setMode("Freehand");
    }

    // Method to save the canvas to a file
    private void saveCanvasToFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showSaveDialog(this);

        ArrayList<Shape> shapes = canvas.getShapes();

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
                oos.writeObject(shapes);
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error saving file", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Method to load the canvas from a file
    private void loadCanvasFromFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        ArrayList<Shape> shapes = canvas.getShapes();

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                ArrayList<Shape> loadedShapes = (ArrayList<Shape>) ois.readObject();
                shapes.clear();
                shapes.addAll(loadedShapes);
                canvas.updateObjectDropdownBox();
                repaint();
            } catch (IOException | ClassNotFoundException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error loading file", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void setSelectedShape(String selectedShape) {
        canvas.setSelectedShape(selectedShape);
    }

    private Shape getSelectedShape() {
        return canvas.getSelectedShape();
    }

    private void setMode(String selectedMode) {
        switch (selectedMode) {
            case "Freehand":
                mode = new FreehandMode();
                break;
            case "Line":
                mode = new LineMode();
                break;
            case "Rectangle":
                mode = new RectangleMode();
                break;
            case "Ellipse":
                mode = new EllipseMode();
                break;
            case "Square":
                mode = new SquareMode();
                break;
            case "Circle":
                mode = new CircleMode();
                break;
            case "Polygon":
                mode = new PolygonMode();
                break;
            case "Select":
                mode = new SelectMode();
                break;
            default:
                throw new IllegalArgumentException("Invalid mode: " + selectedMode);
        }
        canvas.setMode(mode);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Sketchpad sketchpad = new Sketchpad();
                sketchpad.setVisible(true);
            }
        });
    }
}
class Canvas extends JPanel implements Serializable{
    private Color drawingColor;
    private int lineWidth;
    private ArrayList<Shape> shapes;
    private Stack<Shape> redoStack = new Stack<>();
    private Shape currentShape;
    private Point startPoint;
    private Mode mode;
    private JComboBox<String> objectDropdownBox;
    private Shape selectedShape;
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

    public ArrayList<Shape> getShapes() {
        return shapes;
    }

    public void setShapeList(ArrayList<Shape> shapes) {
        this.shapes = shapes;
    }

    public Shape getSelectedShape() {
        String item = (String) objectDropdownBox.getSelectedItem();
        Optional<Shape> result = shapes.stream()
                .filter(Shape -> Shape.toString().equals(item))
                .findFirst();
        if (result.isPresent()) {
            Shape selectedShape = result.get();
            return selectedShape;
        }
        else {
            return null;
        }
    }

    public void copySelectedShape(Point pointToMoveTo, Shape shape) {
        if (shape != null) {
            Shape copy = shape.copy(new Point(pointToMoveTo));
            shapes.add(copy);
            updateObjectDropdownBox();
            repaint();
        }
    }

    public void moveSelectedShape(Point pointToMoveTo, Shape shape) {
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
            Optional<Shape> result = shapes.stream()
                    .filter(Shape -> Shape.toString().equals(selectedShape.toString()))
                    .findFirst();
            if (result.isPresent()) {
                Shape selectedShape = result.get();
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

        Optional<Shape> result = shapes.stream()
                .filter(Shape -> Shape.toString().equals(shapeName))
                .findFirst();
        if (result.isPresent()) {
            Shape selectedShape = result.get();
            this.selectedShapeColor = selectedShape.getColor();
            this.selectedShape = selectedShape;
            selectedShape.setColor(Color.red);
            repaint();
        }
    }

    public void popObject() {
        if (!shapes.isEmpty()) {
            Shape shape = shapes.remove(shapes.size() - 1);
            redoStack.push(shape);
            repaint();
        }
        updateObjectDropdownBox();
    }

    public void pushObject() {
        try {
            if (redoStack.peek() != null) {
                Shape shape = redoStack.pop();
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
                .map(Shape::toString)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        for (Shape shape : shapes) {
            shape.draw(g2d);
        }
    }
}

interface Mode {
    Shape createShape(Point startPoint);
}

class SelectMode implements Mode {

    public Shape createShape(Point startPoint) {
        return null;
    }
}

class FreehandMode implements Mode {
    public Shape createShape(Point startPoint) {
        return new Freehand(startPoint);
    }
}

class LineMode implements Mode {
    public Shape createShape(Point startPoint) {
        return new Line(startPoint);
    }
}

class RectangleMode implements Mode {
    public Shape createShape(Point startPoint) {
        return new Rectangle(startPoint);
    }
}

class EllipseMode implements Mode {
    public Shape createShape(Point startPoint) {
        return new Ellipse(startPoint);
    }
}

class SquareMode implements Mode {
    public Shape createShape(Point startPoint) {
        return new Square(startPoint);
    }
}

class CircleMode implements Mode {
    public Shape createShape(Point startPoint) {
        return new Circle(startPoint);
    }
}

class PolygonMode implements Mode {
    public Shape createShape(Point startPoint) {
        return new Polygon(startPoint);
    }
}

abstract class Shape implements Serializable{
    protected Point startPoint;
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

class Freehand extends Shape implements Serializable {
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

class Line extends Shape implements Serializable {
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

class Rectangle extends Shape implements Serializable {
    private int width;
    private int height;

    public Rectangle(Point startPoint) {
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
        g2d.drawRect(startPoint.x, startPoint.y, width, height);
    }

    public  Shape copy(Point newPoint) {
        Rectangle copy = new Rectangle(newPoint);
        copy.color = color;
        copy.lineWidth = lineWidth;
        copy.width = width;
        copy.height = height;
        return copy;
    }
}

class Ellipse extends Shape implements Serializable {
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

class Square extends Shape implements Serializable {
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

class Circle extends Shape implements Serializable {
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

class Polygon extends Shape implements Serializable {
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