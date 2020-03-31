/**
 * DefaultCritter
 */
public class DefaultCritter extends Object2D implements Critter {

    private static final long serialVersionUID = 1L;
    private static final int DEFAULT_HEALTH_POINTS = 100;
    private static final int DEFAULT_COLLISSION_RADIUS = 5;

    public int healthPoints;
    public double collisionRadius;

    public DefaultCritter() {
        super();
        healthPoints = DEFAULT_HEALTH_POINTS; // Default health points
        collisionRadius = DEFAULT_COLLISSION_RADIUS; // Default collision radius
    }

    public DefaultCritter(Vector2D position, double orientation) {
        super(position, orientation);

        healthPoints = DEFAULT_HEALTH_POINTS; // Default health points
        collisionRadius = DEFAULT_COLLISSION_RADIUS; // Default collision radius
    }

    public void takeDamage(int damagePoints) {
        healthPoints -= damagePoints;
    }

    public void draw() {
        StdDraw.setPenColor(StdDraw.RED);

        // draw line for line of sight
        Vector2D aimTarget = position.add(lookVector().scale(100));
        StdDraw.line(position.x, position.y, aimTarget.x, aimTarget.y);

        // draw circle for body
        StdDraw.filledCircle(position.x, position.y, collisionRadius);
    }

    public boolean isCollidingWith(DefaultCritter other) {
        if (this.distanceTo(other) <= this.collisionRadius + other.collisionRadius)
            return true;
        else
            return false;
    }

}