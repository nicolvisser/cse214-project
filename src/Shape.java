
public interface Shape {

    public boolean contains(Vector2D point);

    public boolean contains(Rectangle rect);

    public boolean contains(Circle circ);

    public boolean intersects(Rectangle rect);

    public boolean intersects(Circle circ);

    public Vector2D getRandomPositionInside();

}