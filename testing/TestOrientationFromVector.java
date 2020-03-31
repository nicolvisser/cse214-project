/**
 * TestOrientationFromVector
 */
public class TestOrientationFromVector {

    public static void main(String[] args) {
        StdOut.println(new Vector2D(1, 0).getPolarAngle());
        StdOut.println(new Vector2D(0, 1).getPolarAngle());
        StdOut.println(new Vector2D(-1, 0).getPolarAngle());
        StdOut.println(new Vector2D(0, -1).getPolarAngle());

    }
}