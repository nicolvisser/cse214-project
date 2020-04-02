public class Circle {

    double x, y;
    double radius;

    public Circle(double x, double y, double radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    public boolean contains(double x, double y) {
        double dx = this.x - x;
        double dy = this.y - y;
        return dx * dx + dy * dy <= radius * radius;
    }

    public boolean contains(Vector2D point) {
        double dx = this.x - point.x;
        double dy = this.y - point.y;
        return dx * dx + dy * dy <= radius * radius;
    }

    public boolean contains(Circle other) {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        double distance = Math.sqrt(dx * dx + dy * dy);
        double maxRadius = Math.max(this.radius, other.radius);
        double minRadius = Math.min(this.radius, other.radius);
        return distance + minRadius <= maxRadius;
    }

    // also includes touching
    public boolean intersects(Circle other) {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        return dx * dx + dy * dy <= (this.radius + other.radius) * (this.radius + other.radius);
    }

    // see http://www.jeffreythompson.org/collision-detection/circle-rect.php
    public boolean intersects(Rectangle rect) {
        // temporary variables to set edges for testing
        double testX = x;
        double testY = y;

        // which edge is closest?
        if (x < rect.getXmin())
            testX = rect.getXmin(); // test left edge
        else if (x > rect.getXmax())
            testX = rect.getXmax(); // right edge
        if (y < rect.getYmin())
            testY = rect.getYmin(); // bottom edge
        else if (y > rect.getYmax())
            testY = rect.getYmax(); // top edge

        // get distance from closest edges
        double distX = x - testX;
        double distY = y - testY;
        double distance = Math.sqrt((distX * distX) + (distY * distY));

        // if the distance is less than the radius, collision!
        return distance <= radius;
    }

    public Vector2D getRandomPositionInside() {
        double rx, ry;
        do {
            rx = 2 * Math.random() - 1;
            ry = 2 * Math.random() - 1;
        } while (rx * rx + ry * ry > 1);
        return new Vector2D(x + rx * radius, y + ry * radius);
    }

    // for debugging and testing
    public void draw() {
        StdDraw.circle(x, y, radius);
    }

}