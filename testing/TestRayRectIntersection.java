
public class TestRayRectIntersection {

    public static void main(String[] args) {

        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(-10, 10);
        StdDraw.setYscale(-10, 10);

        Rectangle rect = new Rectangle(0, 0, 3, 2);

        Vector2D start = new Vector2D(-5, 5);

        while (true) {

            StdDraw.setPenColor(StdDraw.BLACK);
            rect.draw();

            Vector2D end = new Vector2D(StdDraw.mouseX(), StdDraw.mouseY());
            Vector2D dir = end.subtract(start).normalize();
            Ray lineSeg = new Ray(start, dir);

            lineSeg.draw(10);

            StdDraw.show();
            StdDraw.pause(30);
            StdDraw.clear();
            if (rect.intersects(lineSeg)) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.filledRectangle(0, 0, 10, 10);
            }

        }

    }

}