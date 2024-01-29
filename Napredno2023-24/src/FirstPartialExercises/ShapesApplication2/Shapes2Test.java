package FirstPartialExercises.ShapesApplication2;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.DoubleSummaryStatistics;
import java.util.List;

interface Shape {
    double area();

}

class IrregularCanvasException extends Exception {
    public IrregularCanvasException(String message) {
        super(message);
    }
}

class Square implements Shape {
    int side;

    public Square(int side) {
        this.side = side;
    }

    @Override
    public double area() {
        return Math.pow(side, 2);
    }
}

class Circle implements Shape {
    int radius;

    public Circle(int radius) {
        this.radius = radius;
    }

    @Override
    public double area() {
        return Math.PI * radius * radius;
    }
}

class Canvas implements Comparable<Canvas> {
    String id;
    List<Shape> shapes;
    int squares;
    int circles;

    public Canvas(String id, List<Shape> shapes, int circles, int squares) {
        this.id = id;
        this.shapes = shapes;
        this.squares = squares;
        this.circles = circles;
    }

    public static Canvas create(String line) {
        //0cc31e47 C 27 C 13 C 29 C 15 C 22
        String[] parts = line.split("\\s+");
        String id = parts[0];
        List<Shape> shapes = new ArrayList<>();
        int squares = 0;
        int circles = 0;
        for (int i = 1; i < parts.length - 1; i += 2) {
            if (parts[i].equals("C")) {
                shapes.add(new Circle(Integer.parseInt(parts[i + 1])));
                circles++;
            } else if (parts[i].equals("S")) {
                shapes.add(new Square(Integer.parseInt(parts[i + 1])));
                squares++;
            }
        }
        return new Canvas(id, shapes, circles, squares);
    }

    public double sumOfAreas() {
        return shapes.stream()
                .mapToDouble(shape -> shape.area())
                .sum();
    }

    @Override
    public String toString() {
        DoubleSummaryStatistics dss = shapes.stream().mapToDouble(s -> s.area()).summaryStatistics();
        //ID total_shapes total_circles total_squares min_area max_area average_area.
        return String.format("%s %d %d %d %.2f %.2f %.2f", this.id, shapes.size(), circles, squares, dss.getMin(), dss.getMax(), dss.getAverage());
    }

    @Override
    public int compareTo(Canvas o) {
        return Double.compare(o.sumOfAreas(), this.sumOfAreas());
    }
}

class ShapesApplication {
    double maxArea;
    List<Canvas> canvases;

    public ShapesApplication(double maxArea) {
        this.maxArea = maxArea;
        this.canvases = new ArrayList<>();
    }

    public void readCanvases(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = br.readLine()) != null) {
            Canvas c = Canvas.create(line);
            try {
                isValidCanvas(c, maxArea);
                canvases.add(c);
            } catch (IrregularCanvasException e) {
                System.out.println(e.getMessage());
            }
        }
        br.close();
    }

    public void printCanvases(OutputStream os) {
        PrintWriter pw = new PrintWriter(os);

        Collections.sort(canvases);
        canvases.stream().forEach(canvas -> pw.println(canvas));
        pw.flush();
        pw.close();
    }

    private void isValidCanvas(Canvas c, double maxArea) throws IrregularCanvasException {
        for (Shape shape : c.shapes) {
            if (shape.area() > maxArea) {
                throw new IrregularCanvasException(String.format("Canvas %s has a shape with area larger than %.2f", c.id, (float) maxArea));
            }
        }
    }
}

public class Shapes2Test {

    public static void main(String[] args) {

        ShapesApplication shapesApplication = new ShapesApplication(10000);

        System.out.println("===READING CANVASES AND SHAPES FROM INPUT STREAM===");
        try {
            shapesApplication.readCanvases(System.in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("===PRINTING SORTED CANVASES TO OUTPUT STREAM===");
        shapesApplication.printCanvases(System.out);


    }
}