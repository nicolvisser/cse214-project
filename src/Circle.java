public class Circle implements BoundingShape {

    Vector2D center;
    double radius;

    public Circle(double x, double y, double radius) {
        this.center = new Vector2D(x, y);
        this.radius = radius;
    }

    public Circle(Vector2D center, double radius) {
        this.center = new Vector2D(center.x, center.y);
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

    @Override
    public boolean contains(BoundingShape shape) {
        if (shape instanceof Circle) {
            return contains((Circle) shape);
        } else if (shape instanceof Rectangle) {
            return contains((Rectangle) shape);
        }
        return false;
    }

    public boolean contains(Circle other) {
        double dx = this.center.x - other.center.x;
        double dy = this.center.y - other.center.y;
        double distance = Math.sqrt(dx * dx + dy * dy);
        double maxRadius = Math.max(this.radius, other.radius);
        double minRadius = Math.min(this.radius, other.radius);
        return distance + minRadius <= maxRadius;
    }

    // see JimBalter's comment @
    // https://stackoverflow.com/questions/14097290/check-if-circle-contains-rectangle
    public boolean contains(Rectangle rect) {
        double dx = Math.max(center.x - rect.xmin(), rect.xmax() - center.x);
        double dy = Math.max(center.y - rect.ymin(), rect.ymax() - center.y);
        return radius * radius >= dx * dx + dy * dy;
    }

    @Override
    public boolean intersects(Ray ray) {
        return ray.intersects(this); // get code from Ray class instead
    }

    @Override
    public boolean intersects(BoundingShape shape) {
        if (shape instanceof Circle) {
            return intersects((Circle) shape);
        } else if (shape instanceof Rectangle) {
            return intersects((Rectangle) shape);
        }
        return false;
    }

    // also includes touching
    public boolean intersects(Circle other) {
        double dx = this.center.x - other.center.x;
        double dy = this.center.y - other.center.y;
        return dx * dx + dy * dy <= (this.radius + other.radius) * (this.radius + other.radius);
    }

    public boolean intersects(Rectangle rect) {
        return rect.intersects(this);
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