/**
 * Missile
 */
public class Missile extends DefaultCritter {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static final double RELOAD_TIME = 0.15;

    private static final int SPEED = 800;

    public Missile(Vector2D position, Vector2D direction) {
        super(position, Object2D.orientationFromVector(direction));
        velocity = new Vector2D(SPEED * direction.x, SPEED * direction.y);
        collisionRadius = 5;
    }

    @Override
    public void draw() {
        // TODO Auto-generated method stub
        super.draw();
    }

}