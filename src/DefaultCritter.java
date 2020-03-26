/**
 * DefaultCritter
 */
public class DefaultCritter extends Object2D implements Critter {

    private static final long serialVersionUID = 1L;

    public int healthPoints;
    public double collisionRadius;

    public DefaultCritter() {
        super();
        healthPoints = 100; // Default health points
        collisionRadius = 10; // Default collision radius
    }

    public DefaultCritter(Vector2D position, double orientation) {
        super(position, orientation);

        healthPoints = 100; // Default health points
        collisionRadius = 10; // Default collision radius
    }

    public void takeDamage(int damagePoints) {
        healthPoints -= damagePoints;
    }

    public void draw() {
        StdDraw.setPenColor(StdDraw.RED);

        // draw line for line of sight
        Vector2D aimTarget = Vector2D.sum(position, Vector2D.scalarMultiplication(100, FWDVector()));
        StdDraw.line(position.x, position.y, aimTarget.x, aimTarget.y);

        // draw circle for body
        StdDraw.filledCircle(position.x, position.y, collisionRadius);
    }

    public boolean isCollidingWith(DefaultCritter other) {
        if (distanceBetween(this, other) <= this.collisionRadius + other.collisionRadius)
            return true;
        else
            return false;
    }

}