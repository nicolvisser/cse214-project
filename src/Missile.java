/**
 * Missile
 */
public class Missile extends DefaultCritter {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private static final int SPEED = 800;

    public int missileDamage;

    public double burstRadius;

    public Missile(Vector2D position, Vector2D direction) {
        super(position, Object2D.orientationFromVector(direction));
        velocity = new Vector2D(SPEED * direction.x, SPEED * direction.y);
        collisionRadius = 5;
        missileDamage = 100;
        burstRadius = 5;
    }

    public Missile(Vector2D position, Vector2D direction, double burstRadius) {
        super(position, Object2D.orientationFromVector(direction));
        velocity = new Vector2D(SPEED * direction.x, SPEED * direction.y);
        collisionRadius = 5;
        missileDamage = 100;
        this.burstRadius = burstRadius;
    }

}