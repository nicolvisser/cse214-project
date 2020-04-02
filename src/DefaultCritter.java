/**
 * DefaultCritter
 */
public class DefaultCritter extends Object2D implements Critter {

    private static final long serialVersionUID = 1L;
    private static final int DEFAULT_HEALTH_POINTS = 100;
    private static final int DEFAULT_COLLISION_RADIUS = 5;

    public int healthPoints;
    public double collisionRadius;

    public boolean allowTranslation;
    public boolean allowRotation;

    public DefaultCritter() {
        super();

        healthPoints = DEFAULT_HEALTH_POINTS;
        collisionRadius = DEFAULT_COLLISION_RADIUS;

        allowTranslation = true;
        allowRotation = true;
    }

    public DefaultCritter(Vector2D position, double orientation) {
        super(position, orientation);

        healthPoints = DEFAULT_HEALTH_POINTS;
        collisionRadius = DEFAULT_COLLISION_RADIUS;

        allowTranslation = true;
        allowRotation = true;
    }

    public void takeDamage(int damagePoints) {
        healthPoints -= damagePoints;
    }

    // draw simple representation of DefaultCritter for debugging / testing
    public void draw() {
        StdDraw.setPenColor(StdDraw.RED);
        // draw line for line of sight
        Vector2D aimTarget = position.add(lookVector().scale(100));
        StdDraw.line(position.x, position.y, aimTarget.x, aimTarget.y);
        // draw circle for body
        StdDraw.filledCircle(position.x, position.y, collisionRadius);
    }

    public boolean isCollidingWith(DefaultCritter other) {
        return this.distanceTo(other) <= this.collisionRadius + other.collisionRadius;
    }

    @Override
    public void render(double dt) {
        if (allowTranslation)
            renderTranslation(dt);
        if (allowRotation)
            renderRotation(dt);
    }

    @Override
    public void prepareToSaveState() {

    }

}