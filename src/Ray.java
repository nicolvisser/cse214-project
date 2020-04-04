public class Ray {

    Vector2D start;
    Vector2D direction;

    public Ray(Vector2D start, Vector2D direction) {
        this.start = new Vector2D(start.x, start.y);
        this.direction = new Vector2D(direction.x, direction.y).normalize();
    }

    public void draw(double length) {
        Vector2D end = start.add(direction.scale(length));
        StdDraw.line(start.x, start.y, end.x, end.y);
    }

    public boolean intersects(Rectangle rect) {
        return Double.isFinite(lengthUntilIntersection(rect));
    }

    public boolean intersects(Circle circ) {
        return Double.isFinite(lengthUntilIntersection(circ));
    }

    // following zacharmarz's answer at
    // https://gamedev.stackexchange.com/questions/18436/most-efficient-aabb-vs-ray-collision-algorithms
    public double lengthUntilIntersection(Rectangle rect) {
        double t1 = (rect.xmin() - start.x) / direction.x;
        double t2 = (rect.xmax() - start.x) / direction.x;
        double t3 = (rect.ymin() - start.y) / direction.y;
        double t4 = (rect.ymax() - start.y) / direction.y;

        double tmin = Math.max(Math.min(t1, t2), Math.min(t3, t4));
        double tmax = Math.min(Math.max(t1, t2), Math.max(t3, t4));

        if (tmax < 0) {
            // extended line of ray is intersecting AABB, but the whole AABB is behind ray
            return Double.POSITIVE_INFINITY;
        }

        if (tmin > tmax) {
            // ray doesn't intersect AABB
            return Double.POSITIVE_INFINITY;
        }

        return Math.max(0, tmin);
    }

    public double lengthUntilIntersection(Circle circ) {

        Vector2D vStartToCenter = circ.center.subtract(start);
        if (vStartToCenter.magnitude() <= circ.radius) { // start inside circle
            return 0;
        }

        double projection = this.direction.dot(vStartToCenter);
        if (projection < 0) { // circle behind line
            return Double.POSITIVE_INFINITY;
        }

        double perpendic = (vStartToCenter.subtract(direction.scale(projection))).magnitude();
        if (perpendic > circ.radius) { // line outside of circle
            return Double.POSITIVE_INFINITY;
        }

        double l = Math.sqrt(circ.radius * circ.radius - perpendic * perpendic);
        return projection - l;
    }

}