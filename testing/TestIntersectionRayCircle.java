
public class TestIntersectionRayCircle {

    public static void main(String[] args) {

        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(-10, 10);
        StdDraw.setYscale(-10, 10);

        Circle circ = new Circle(0, 0, 3);

        Vector2D start = new Vector2D(-5, 5);

        while (true) {

            if (StdDraw.isMousePressed()) {
                start = new Vector2D(StdDraw.mouseX(), StdDraw.mouseY());
            }

            StdDraw.setPenColor(StdDraw.BLACK);
            circ.draw();

            Vector2D end = new Vector2D(StdDraw.mouseX(), StdDraw.mouseY());
            Vector2D dir = end.subtract(start).normalize();
            Ray ray = new Ray(start, dir);

            double length = ray.lengthUntilIntersection(circ);

            if (Double.isInfinite(length)) {
                length = 20;
            }

            if (!(start.x == end.x && start.y == end.y))
                ray.draw(length);

            StdDraw.show();
            StdDraw.pause(30);
            StdDraw.clear();
            if (ray.intersects(circ)) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.filledRectangle(0, 0, 10, 10);
            }

        }

    }

}