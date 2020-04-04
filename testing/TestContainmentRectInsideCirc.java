
public class TestContainmentRectInsideCirc {

    public static void main(String[] args) {

        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(-10, 10);
        StdDraw.setYscale(-10, 10);

        Circle c = new Circle(0, 0, 5);

        while (true) {

            StdDraw.setPenColor(StdDraw.BLACK);
            c.draw();

            Rectangle r = new Rectangle(StdDraw.mouseX(), StdDraw.mouseY(), 2, 2);

            r.draw();

            StdOut.println(c.contains(r));

            StdDraw.show();
            StdDraw.pause(30);
            StdDraw.clear();
            if (c.contains(r)) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.filledRectangle(0, 0, 10, 10);
            }

        }
    }

}