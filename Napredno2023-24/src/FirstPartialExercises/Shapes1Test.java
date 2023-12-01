package FirstPartialExercises;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

class Square {
    int side;

    public Square(int side) {
        this.side = side;
    }

    public int getPerimeter() {
        return 4 * side;
    }
}

class Canvas implements Comparable<Canvas> {
    String canvasId;
    List<Square> squares;

    public Canvas(String canvasId, List<Square> squares) {
        this.canvasId = canvasId;
        this.squares = squares;
    }

    public static Canvas createCanvas(String line) {
        //364fbe94 24 30 22 33 32 30 37 18 29 27 33 21 27 26
        String[] parts = line.split("\\s+");
        String id = parts[0];

        List<Square> squares = Arrays.stream(parts).skip(1)
                .map(part -> Integer.parseInt(part))
                .map(side -> new Square(side))
                .collect(Collectors.toList());

        return new Canvas(id, squares);
    }

    @Override
    public String toString() {
        return String.format("%s %d %d", canvasId, squares.size(), sumOfPerimeter());
    }

    private int sumOfPerimeter() {
        return squares.stream().mapToInt(square -> square.getPerimeter()).sum();
    }

    @Override
    public int compareTo(Canvas o) {
        return Integer.compare(this.sumOfPerimeter(), o.sumOfPerimeter());
    }
}

class ShapesApplication {
    private List<Canvas> canvases;

    public ShapesApplication() {
        this.canvases = new ArrayList<>();
    }

    public int readCanvases(InputStream inputStream) {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

        canvases = br.lines()
                .map(line -> Canvas.createCanvas(line))
                .collect(Collectors.toList());

        return canvases.stream().mapToInt(canvas -> canvas.squares.size()).sum();
    }

    public void printLargestCanvasTo(OutputStream outputStream) {
        PrintWriter pw = new PrintWriter(outputStream);

        Canvas max = canvases.stream().max(Comparator.naturalOrder()).get();

        pw.println(max);
        pw.flush();
    }
}

public class Shapes1Test {
    public static void main(String[] args) {

        ShapesApplication shapesApplication = new ShapesApplication();

        System.out.println("===READING SQUARES FROM INPUT STREAM===");
        System.out.println(shapesApplication.readCanvases(System.in));
        System.out.println("===PRINTING LARGEST CANVAS TO OUTPUT STREAM===");
        shapesApplication.printLargestCanvasTo(System.out);

    }
}
