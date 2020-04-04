public class TestIntersectionRectangleRectangle {

    public static void main(String[] args) {

        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(-10, 10);
        StdDraw.setYscale(-10, 10);

        Rectangle r1 = new Rectangle(0, 0, 5, 5);

        while (true) {

            StdDraw.setPenColor(StdDraw.BLACK);
            r1.draw();

            Rectangle r2 = new Rectangle(StdDraw.mouseX(), StdDraw.mouseY(), 3, 2);
            r2.draw();

            StdOut.println(r2.intersects(r1));

            StdDraw.show();
            StdDraw.pause(30);
            StdDraw.clear();
            if (r2.intersects(r1)) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.filledRectangle(0, 0, 10, 10);
            }

        }
    }

}