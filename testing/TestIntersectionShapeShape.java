// tests ability to reference shapes of unknown type and some of its methods via interface BoundingShape
public class TestIntersectionShapeShape {

    public static void main(String[] args) {

        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(-10, 10);
        StdDraw.setYscale(-10, 10);

        BoundingShape s1;
        if (Math.random() < 0.5) {
            s1 = new Rectangle(0, 0, 5, 3);
        } else {
            s1 = new Circle(0, 0, 2);
        }

        BoundingShape s2;
        if (Math.random() < 0.5) {
            s2 = new Rectangle(0, 0, 3, 3);
        } else {
            s2 = new Circle(0, 0, 1);
        }

        while (true) {

            if (s2 instanceof Circle) {
                Circle c2 = (Circle) s2;
                c2.center = new Vector2D(StdDraw.mouseX(), StdDraw.mouseY());
            } else if (s2 instanceof Rectangle) {
                Rectangle r2 = (Rectangle) s2;
                r2.center = new Vector2D(StdDraw.mouseX(), StdDraw.mouseY());
            }

            StdDraw.setPenColor(StdDraw.BLACK);
            s1.draw();
            s2.draw();

            StdDraw.show();
            StdDraw.pause(30);
            StdDraw.clear();
            if (s2.intersects(s1)) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.filledRectangle(0, 0, 10, 10);
            }

        }
    }

}