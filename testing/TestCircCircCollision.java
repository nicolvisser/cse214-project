public class TestCircCircCollision {

    public static void main(String[] args) {

        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(-10, 10);
        StdDraw.setYscale(-10, 10);

        Circle c1 = new Circle(0, 0, 5);

        while (true) {

            StdDraw.setPenColor(StdDraw.BLACK);
            c1.draw();

            Circle c2 = new Circle(StdDraw.mouseX(), StdDraw.mouseY(), 3);
            c2.draw();

            StdDraw.show();
            StdDraw.pause(30);
            StdDraw.clear();
            if (c1.intersects(c2)) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.filledRectangle(0, 0, 10, 10);
            }

        }
    }

}