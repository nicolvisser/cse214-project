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
    }

    public boolean hasCollidedWith(DefaultCritter other) {
        double centerPointsDistance = distanceBetween(this, other);
        if (centerPointsDistance <= this.collisionRadius + other.collisionRadius)
            return true;
        else
            return false;
    }

}