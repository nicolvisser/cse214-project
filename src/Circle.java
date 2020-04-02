public class Circle {

    Vector2D center;
    double radius;

    public Circle(double x, double y, double radius) {
        this.center.x = x;
        this.center.y = y;
        this.radius = radius;
    }

    public Circle(Vector2D center, double radius) {
        this.center.x = center.x;
        this.center.y = center.y;
        this.radius = radius;
    }

    public boolean contains(double x, double y) {
        double dx = this.center.x - x;
        double dy = this.center.y - y;
        return dx * dx + dy * dy <= radius * radius;
    }

    public boolean contains(Vector2D point) {
        double dx = this.center.x - point.x;
        double dy = this.center.y - point.y;
        return dx * dx + dy * dy <= radius * radius;
    }

    public boolean contains(Circle other) {
        double dx = this.center.x - other.center.x;
        double dy = this.center.y - other.center.y;
        double distance = Math.sqrt(dx * dx + dy * dy);
        double maxRadius = Math.max(this.radius, other.radius);
        double minRadius = Math.min(this.radius, other.radius);
        return distance + minRadius <= maxRadius;
    }

    // also includes touching
    public boolean intersects(Circle other) {
        double dx = this.center.x - other.center.x;
        double dy = this.center.y - other.center.y;
        return dx * dx + dy * dy <= (this.radius + other.radius) * (this.radius + other.radius);
    }

    // see http://www.jeffreythompson.org/collision-detection/circle-rect.php
    public boolean intersects(Rectangle rect) {
        // temporary variables to set edges for testing
        double testX = center.x;
        double testY = center.y;

        // which edge is closest?
        if (center.x < rect.xmin())
            testX = rect.xmin(); // test left edge
        else if (center.x > rect.xmax())
            testX = rect.xmax(); // right edge
        if (center.y < rect.ymin())
            testY = rect.ymin(); // bottom edge
        else if (center.y > rect.ymax())
            testY = rect.ymax(); // top edge

        // get distance from closest edges
        double distX = center.x - testX;
        double distY = center.y - testY;
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
        return new Vector2D(center.x + rx * radius, center.y + ry * radius);
    }

    // for debugging and testing
    public void draw() {
        StdDraw.circle(center.x, center.y, radius);
    }

}