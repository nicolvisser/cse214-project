/**
 * DefaultCritter
 */
public class DefaultCritter extends Object2D implements Critter {

    public int healthPoints;

    public double collisionRadius;

    public DefaultCritter(Vector2D position, double orientation) {
        super(position, orientation);
        healthPoints = 100;
        collisionRadius = 10;
    }

    public boolean isAlive() {
        return healthPoints > 0;
    }

    public void takeDamage(int damagePoints) {
        healthPoints -= damagePoints;
    }

    public void renderStep(double dt) {
        super.renderStep(dt);
    }

    public void draw() {

        StdDraw.setPenColor(StdDraw.RED);

        // draw line for line of sight
        Vector2D aimTarget = Vector2D.sum(position, Vector2D.scalarMultiplication(100, FWDVector()));
        StdDraw.line(position.x, position.y, aimTarget.x, aimTarget.y);

        // draw circle for body
        StdDraw.filledCircle(position.x, position.y, collisionRadius);
    }

    public boolean hasCollidedWith(DefaultCritter other) {
        double centerPointsDistance = distanceBetween(this, other);
        if (centerPointsDistance <= this.collisionRadius + other.collisionRadius)
            return true;
        else
            return false;
    }

}