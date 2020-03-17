/**
 * DefaultCritter
 */
public class DefaultCritter extends Object2D implements Critter {

    private boolean isAlive;
    private int healthPoints;

    // TODO CONTINUE WITH COLLISION DETECTION STUFF
    private double collisionRadius;

    public DefaultCritter(Vector2D position, double orientation) {
        super(position, orientation);
        isAlive = true;
        healthPoints = 100;
    }

    public boolean isAlive() {
        return healthPoints > 0;
    }

    public void takeDamage(int damagePoints) {
        healthPoints -= damagePoints;

        // kill if no more healthPoints left
        if (healthPoints <= 0) {
            isAlive = false;
        }
    }

    public void renderStep(double dt) {
        super.renderStep(dt);
    }

    public void draw() {
    }

}