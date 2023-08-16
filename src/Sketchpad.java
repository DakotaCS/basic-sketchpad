import mode.*;
import shape.Shape;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author dakota
 * @version 1.0
 * @since 2023-07-31
 *
 *  Defines the basic controls for the layout of the sketchpad and button operations
 */
public class Sketchpad extends JFrame implements Serializable {
    private Canvas canvas;
    private Mode mode;
    public Sketchpad() {
        setTitle("Sketchpad");
        setSize(1300, 600);
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
