package LaboratoryExercise02;

import java.util.*;

enum TYPE {
    POINT,
    CIRCLE
}

enum DIRECTION {
    UP,
    DOWN,
    LEFT,
    RIGHT
}

class ObjectCanNotBeMovedException extends Exception {
    ObjectCanNotBeMovedException(int x, int y) {
        super(String.format("Point (%d,%d) is out of bounds", x, y));
    }
}

class MovableObjectNotFittableException extends Exception {
    public MovableObjectNotFittableException(String message) {
        super(message);
    }
}

interface Movable {
    void moveUp() throws ObjectCanNotBeMovedException;

    void moveLeft() throws ObjectCanNotBeMovedException;

    void moveRight() throws ObjectCanNotBeMovedException;

    void moveDown() throws ObjectCanNotBeMovedException;

    int getCurrentXPosition();

    int getCurrentYPosition();
}

class MovablePoint implements Movable {

    private int x;
    private int y;
    private int xSpeed;
    private int ySpeed;

    public MovablePoint(int x, int y, int xSpeed, int ySpeed) {
        this.x = x;
        this.y = y;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }

    @Override
    public void moveUp() throws ObjectCanNotBeMovedException {
        if (y < 0 || y + ySpeed > MovablesCollection.y_MAX)
            throw new ObjectCanNotBeMovedException(x, y + ySpeed);

        y += ySpeed;
    }

    @Override
    public void moveLeft() throws ObjectCanNotBeMovedException {
        if (x - xSpeed < 0 || x > MovablesCollection.x_MAX)
            throw new ObjectCanNotBeMovedException(x - xSpeed, y);
        x -= xSpeed;
    }

    @Override
    public void moveRight() throws ObjectCanNotBeMovedException {
        if (x < 0 || x + xSpeed > MovablesCollection.x_MAX)
            throw new ObjectCanNotBeMovedException(x + xSpeed, y);
        x += xSpeed;
    }

    @Override
    public void moveDown() throws ObjectCanNotBeMovedException {
        if (y - ySpeed < 0 || y > MovablesCollection.y_MAX)
            throw new ObjectCanNotBeMovedException(x, y - ySpeed);

        y -= ySpeed;
    }

    @Override
    public int getCurrentXPosition() {
        return x;
    }

    @Override
    public int getCurrentYPosition() {
        return y;
    }

    @Override
    public String toString() {
        return String.format("Movable point with coordinates (%d,%d)", x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovablePoint that = (MovablePoint) o;
        return x == that.x && y == that.y && xSpeed == that.xSpeed && ySpeed == that.ySpeed;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, xSpeed, ySpeed);
    }
}

class MovableCircle implements Movable {
    private int radius;
    private MovablePoint center;

    public MovableCircle(int radius, MovablePoint center) {
        this.radius = radius;
        this.center = center;
    }

    @Override
    public void moveUp() throws ObjectCanNotBeMovedException {
        int x = getCurrentXPosition(), y = getCurrentYPosition();
        if (y < 0 || y + radius > MovablesCollection.y_MAX)
            throw new ObjectCanNotBeMovedException(x, y);
        center.moveUp();
    }

    @Override
    public void moveLeft() throws ObjectCanNotBeMovedException {
        int x = getCurrentXPosition(), y = getCurrentYPosition();
        if (x - radius < 0 || x > MovablesCollection.x_MAX)
            throw new ObjectCanNotBeMovedException(x, y);
        center.moveLeft();
    }

    @Override
    public void moveRight() throws ObjectCanNotBeMovedException {
        int x = getCurrentXPosition(), y = getCurrentYPosition();
        if (x < 0 || x + radius > MovablesCollection.x_MAX)
            throw new ObjectCanNotBeMovedException(x, y);
        center.moveRight();
    }

    @Override
    public void moveDown() throws ObjectCanNotBeMovedException {
        int x = getCurrentXPosition(), y = getCurrentYPosition();
        if (y - radius < 0 || y > MovablesCollection.y_MAX)
            throw new ObjectCanNotBeMovedException(x, y);
        center.moveDown();
    }

    @Override
    public int getCurrentXPosition() {
        return center.getCurrentXPosition();
    }

    @Override
    public int getCurrentYPosition() {
        return center.getCurrentYPosition();
    }

    public int getRadius() {
        return radius;
    }

    @Override
    public String toString() {
        return "Movable circle with center coordinates (" + getCurrentXPosition() + "," + getCurrentYPosition() + ") and radius " + radius;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovableCircle that = (MovableCircle) o;
        return radius == that.radius && Objects.equals(center, that.center);
    }

    @Override
    public int hashCode() {
        return Objects.hash(radius, center);
    }
}

class MovablesCollection {
    private Movable[] movable;
    public static int x_MAX = 0;
    public static int y_MAX = 0;

    public MovablesCollection(int x_max, int y_max) {
        movable = new Movable[0];
        x_MAX = x_max;
        y_MAX = y_max;
    }

    public static void setxMax(int i) {
        x_MAX = i;
    }

    public static void setyMax(int i) {
        y_MAX = i;
    }

    public void addMovableObject(Movable m) throws MovableObjectNotFittableException {
        int x = m.getCurrentXPosition();
        int y = m.getCurrentYPosition();

        MovablePoint point = m instanceof MovablePoint ? (MovablePoint) m : null;
        MovableCircle circle = m instanceof MovableCircle ? (MovableCircle) m : null;

        if (point != null) {
            if (x < 0 || x > x_MAX)
                throw new MovableObjectNotFittableException(m + " can not be fitted into the collection");
            if (y < 0 || y > y_MAX)
                throw new MovableObjectNotFittableException(m + " can not be fitted into the collection");
        } else if (circle != null) {
            int r = circle.getRadius();
            if (x - r < 0 || x + r > x_MAX)
                throw new MovableObjectNotFittableException(String.format("Movable circle with center (%d,%d) and radius %d can not be fitted into the collection", x, y, r));
            if (y - r < 0 || x + r > y_MAX)
                throw new MovableObjectNotFittableException(String.format("Movable circle with center (%d,%d) and radius %d can not be fitted into the collection", x, y, r));
        }

        Movable[] newArr = Arrays.copyOf(movable, movable.length + 1);
        newArr[newArr.length - 1] = m;
        movable = newArr;
    }

    public void moveObjectsFromTypeWithDirection(TYPE type, DIRECTION direction) {
        for (Movable m : movable) {
            MovablePoint point = m instanceof MovablePoint ? (MovablePoint) m : null;
            MovableCircle circle = m instanceof MovableCircle ? (MovableCircle) m : null;
            if ((type == TYPE.POINT && point == null) || (type == TYPE.CIRCLE && circle == null)) {
                continue;
            }
            if (direction == DIRECTION.UP) {
                try {
                    m.moveUp();
                } catch (ObjectCanNotBeMovedException e) {
                    System.out.println(e.getMessage());
                }
            } else if (direction == DIRECTION.DOWN) {
                try {
                    m.moveDown();
                } catch (ObjectCanNotBeMovedException e) {
                    System.out.println(e.getMessage());
                }
            } else if (direction == DIRECTION.LEFT) {
                try {
                    m.moveLeft();
                } catch (ObjectCanNotBeMovedException e) {
                    System.out.println(e.getMessage());
                }
            } else if (direction == DIRECTION.RIGHT) {
                try {
                    m.moveRight();
                } catch (ObjectCanNotBeMovedException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Collection of movable objects with size %d:", movable.length)).append("\n");
        for (Movable m : movable) {
            sb.append(m + "\n");
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovablesCollection that = (MovablesCollection) o;
        return Arrays.equals(movable, that.movable);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(movable);
    }
}

public class CirclesTest {

    public static void main(String[] args) {

        System.out.println("===COLLECTION CONSTRUCTOR AND ADD METHOD TEST===");
        MovablesCollection collection = new MovablesCollection(100, 100);
        Scanner sc = new Scanner(System.in);
        int samples = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < samples; i++) {
            String inputLine = sc.nextLine();
            String[] parts = inputLine.split(" ");

            int x = Integer.parseInt(parts[1]);
            int y = Integer.parseInt(parts[2]);
            int xSpeed = Integer.parseInt(parts[3]);
            int ySpeed = Integer.parseInt(parts[4]);

            if (Integer.parseInt(parts[0]) == 0) { //point
                try {
                    collection.addMovableObject(new MovablePoint(x, y, xSpeed, ySpeed));
                } catch (MovableObjectNotFittableException e) {
                    System.out.println(e.getMessage());
                }
            } else { //circle
                int radius = Integer.parseInt(parts[5]);
                try {
                    collection.addMovableObject(new MovableCircle(radius, new MovablePoint(x, y, xSpeed, ySpeed)));
                } catch (MovableObjectNotFittableException e) {
                    System.out.println(e.getMessage());
                }
            }

        }
        System.out.println(collection.toString());

        System.out.println("MOVE POINTS TO THE LEFT");
        collection.moveObjectsFromTypeWithDirection(TYPE.POINT, DIRECTION.LEFT);
        System.out.println(collection.toString());

        System.out.println("MOVE CIRCLES DOWN");
        collection.moveObjectsFromTypeWithDirection(TYPE.CIRCLE, DIRECTION.DOWN);
        System.out.println(collection.toString());

        System.out.println("CHANGE X_MAX AND Y_MAX");
        MovablesCollection.setxMax(90);
        MovablesCollection.setyMax(90);

        System.out.println("MOVE POINTS TO THE RIGHT");
        collection.moveObjectsFromTypeWithDirection(TYPE.POINT, DIRECTION.RIGHT);
        System.out.println(collection.toString());

        System.out.println("MOVE CIRCLES UP");
        collection.moveObjectsFromTypeWithDirection(TYPE.CIRCLE, DIRECTION.UP);
        System.out.println(collection.toString());


    }


}
