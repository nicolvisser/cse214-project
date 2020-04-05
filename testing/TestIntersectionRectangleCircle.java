public class TestIntersectionRectangleCircle {

    public static void main(String[] args) {

        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(-10, 10);
        StdDraw.setYscale(-10, 10);

        Rectangle r = new Rectangle(0, 0, 5, 5);

        while (true) {

            StdDraw.setPenColor(StdDraw.BLACK);
            r.draw();

            Circle c = new Circle(StdDraw.mouseX(), StdDraw.mouseY(), 3);
            c.draw();

            StdOut.println(c.intersects(r));

            StdDraw.show();
            StdDraw.pause(30);
            StdDraw.clear();
            if (c.intersects(r)) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.filledRectangle(0, 0, 10, 10);
            }

        }
    }

}