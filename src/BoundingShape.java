public interface BoundingShape {

    public boolean contains(Vector2D point);

    public boolean contains(BoundingShape shape);

    public boolean intersects(Ray ray);

    public boolean intersects(BoundingShape shape);

    public Vector2D getRandomPositionInside();

    public void draw();

}