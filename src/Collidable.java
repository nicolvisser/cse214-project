public interface Collidable {

    public BoundingShape getBoundingShape();

    public boolean isCollidingWith(Collidable other);
}