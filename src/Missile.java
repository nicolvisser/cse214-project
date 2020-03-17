/**
 * Missile
 */
public class Missile extends DefaultCritter {

    public static final int RELOAD_TIME_MS = 150;

    private static final int SPEED = 800;

    public Missile(Vector2D position, Vector2D direction) {
        super(position, Object2D.orientationFromVector(direction));
        velocity = new Vector2D(SPEED * direction.x, SPEED * direction.y);
        collisionRadius = 5;
    }

    @Override
    public void draw() {
        StdDraw.setPenColor(StdDraw.ORANGE);
        StdDraw.filledCircle(position.x, position.y, collisionRadius);
    }

}