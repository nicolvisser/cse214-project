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

}