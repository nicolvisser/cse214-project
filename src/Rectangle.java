public class Rectangle {

    public double width, height;
    public Vector2D center;

    public Rectangle(double x, double y, double width, double height) {
        this.center = new Vector2D(x, y);
        this.width = width;
        this.height = height;
    }

    // note: setting center REFERENCE
    public Rectangle(Vector2D center, double width, double height) {
        this.center = center;
        this.width = width;
        this.height = height;
    }

    public double xmin() {
        return center.x - width / 2;
    }

    public double xmax() {
        return center.x + width / 2;
    }

    public double ymin() {
        return center.y - height / 2;
    }

    public double ymax() {
        return center.y + height / 2;
    }

    public void setPosition(double x, double y) {
        center.x = x;
        center.y = y;
    }

    public void setPositionFrom(Vector2D center) {
        this.center.x = center.x;
        this.center.y = center.y;
    }

    public void setPositionAs(Vector2D center) {
        this.center = center;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
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
        return (x >= xmin()) && (x <= xmax()) && (y >= ymin()) && (y <= ymax());
    }

    public boolean contains(Vector2D point) {
        return contains(point.x, point.y);
    }

    public boolean contains(Rectangle other) {
        return (other.xmin() >= this.xmin() && other.xmax() <= this.xmax() && other.ymin() >= this.ymin()
                && other.ymax() <= this.ymax());
    }

    // also includes touching
    public boolean intersects(Rectangle other) {
        return !(this.xmin() > other.xmax() || this.xmax() < other.xmin() || this.ymin() > other.ymax()
                || this.ymax() < other.ymin());
    }

    // see http://www.jeffreythompson.org/collision-detection/circle-rect.php
    public boolean intersects(Circle circle) {
        // temporary variables to set edges for testing
        double testX = circle.x;
        double testY = circle.y;

        // which edge is closest?
        if (circle.x < xmin())
            testX = xmin(); // test left edge
        else if (circle.x > xmax())
            testX = xmax(); // right edge
        if (circle.y < ymin())
            testY = ymin(); // bottom edge
        else if (circle.y > ymax())
            testY = ymax(); // top edge

        // get distance from closest edges
        double distX = circle.x - testX;
        double distY = circle.y - testY;
        double distance = Math.sqrt((distX * distX) + (distY * distY));

        // if the distance is less than the radius, collision!
        return distance <= circle.radius;
    }

    public Rectangle intersection(Rectangle other) {
        if (intersects(other)) {
            double newXmin = Math.max(this.xmin(), other.xmin());
            double newXmax = Math.min(this.xmax(), other.xmax());
            double newYmin = Math.max(this.ymin(), other.ymin());
            double newYmax = Math.min(this.ymax(), other.ymax());

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
            return (center.x == rect.center.x && center.y == rect.center.y && width == rect.width && height == rect.height);
        }
        return super.equals(obj);
    }

    public Vector2D getRandomPositionInside() {
        double rx = xmin() + Math.random() * width;
        double ry = ymin() + Math.random() * height;
        return new Vector2D(rx, ry);
    }

    // for debugging and testing
    public void draw() {
        StdDraw.rectangle(center.x, center.y, width / 2, height / 2);
    }
}