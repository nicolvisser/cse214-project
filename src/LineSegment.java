
public class LineSegment {

    Vector2D start;
    Vector2D end;

    public LineSegment(Vector2D start, Vector2D end) {
        this.start = new Vector2D(start.x, start.y);
        this.end = new Vector2D(end.x, end.y);
    }

    public LineSegment(Vector2D start, Vector2D direction, double length) {
        this.start = new Vector2D(start.x, start.y);
        this.end = start.add(direction.scale(length));
    }

    public void draw() {
        StdDraw.line(start.x, start.y, end.x, end.y);
    }

}