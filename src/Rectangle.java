public class Rectangle {

    private double width, height;
    private double x, y; // position of center of rectangle
    private double xmin, xmax, ymin, ymax;

    public Rectangle(double x, double y, double width, double height) {
        set(x, y, width, height);
    }

    public void set(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.xmin = x - width / 2;
        this.xmax = x + width / 2;
        this.ymin = y - height / 2;
        this.ymax = y + height / 2;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getXmin() {
        return xmin;
    }

    public double getXmax() {
        return xmax;
    }

    public double getYmin() {
        return ymin;
    }

    public double getYmax() {
        return ymax;
    }

    public void setWidth(double width) {
        set(x, y, width, height);
    }

    public void setHeight(double height) {
        set(x, y, width, height);
    }

    public void setXcenter(double xcenter) {
        set(xcenter, y, width, height);
    }

    public void setYcenter(double ycenter) {
        set(x, ycenter, width, height);
    }

    public void setPosition(double x, double y) {
        set(x, y, width, height);
    }

    public void setSize(int width, int height) {
        set(x, y, width, height);
    }

    public static Rectangle zero() {
        return new Rectangle(0, 0, 0, 0);
    }

    public static Rectangle infinite() {
        return new Rectangle(0, 0, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    public boolean isInfinite() {
        return Double.isInfinite(width) || Double.isInfinite(height);
    }

    public boolean isEmpty() {
        return width == 0 || height == 0;
    }

    public boolean contains(double x, double y) {
        return (x >= xmin) && (x <= xmax) && (y >= ymin) && (y <= ymax);
    }

    public boolean contains(Vector2D pos) {
        return contains(pos.x, pos.y);
    }

    public boolean contains(Rectangle other) {
        return (other.xmin >= this.xmin && other.xmax <= this.xmax && other.ymin >= this.ymin
                && other.ymax <= this.ymax);
    }

    // also includes touching
    public boolean intersects(Rectangle other) {
        return !(this.xmin > other.xmax || this.xmax < other.xmin || this.ymin > other.ymax || this.ymax < other.ymin);
    }

    // see http://www.jeffreythompson.org/collision-detection/circle-rect.php
    public boolean intersects(Circle circle) {
        // temporary variables to set edges for testing
        double testX = circle.x;
        double testY = circle.y;

        // which edge is closest?
        if (circle.x < xmin)
            testX = xmin; // test left edge
        else if (circle.x > xmax)
            testX = xmax; // right edge
        if (circle.y < ymin)
            testY = ymin; // bottom edge
        else if (circle.y > ymax)
            testY = ymax; // top edge

        // get distance from closest edges
        double distX = circle.x - testX;
        double distY = circle.y - testY;
        double distance = Math.sqrt((distX * distX) + (distY * distY));

        // if the distance is less than the radius, collision!
        return distance <= circle.radius;
    }

    public Rectangle intersection(Rectangle other) {
        if (intersects(other)) {
            double newXmin = Math.max(this.xmin, other.xmin);
            double newXmax = Math.min(this.xmax, other.xmax);
            double newYmin = Math.max(this.ymin, other.ymin);
            double newYmax = Math.min(this.ymax, other.ymax);

            double newX = (newXmin + newXmax) / 2;
            double newY = (newYmin + newYmax) / 2;
            double newWidth = newXmax - newXmin;
            double newHeight = newYmax - newYmin;

            return new Rectangle(newX, newY, newWidth, newHeight);// possibly also returns rectangle of zero width
        } else {
            return zero();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Rectangle) {
            Rectangle rect = (Rectangle) obj;
            return (x == rect.x && y == rect.y && width == rect.width && height == rect.height);
        }
        return super.equals(obj);
    }

    public Vector2D getRandomPositionInside() {
        double rx = xmin + Math.random() * width;
        double ry = ymin + Math.random() * height;
        return new Vector2D(rx, ry);
    }

    // for debugging and testing
    public void draw() {
        StdDraw.rectangle(x, y, width / 2, height / 2);
    }
}