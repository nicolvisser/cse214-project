/**
 * TestOrientationFromVector
 */
public class TestOrientationFromVector {

    public static void main(String[] args) {
        StdOut.println(Object2D.orientationFromVector(new Vector2D(1, 0)));
        StdOut.println(Object2D.orientationFromVector(new Vector2D(0, 1)));
        StdOut.println(Object2D.orientationFromVector(new Vector2D(-1, 0)));
        StdOut.println(Object2D.orientationFromVector(new Vector2D(0, -1)));

    }
}