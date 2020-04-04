public class DefaultCritter extends Object2D implements Critter, Collidable {

    private static final long serialVersionUID = 1L;
    private static final int DEFAULT_HEALTH_POINTS = 100;
    private static final int DEFAULT_COLLISION_RADIUS = 5;

    public int healthPoints;

    private final BoundingShape boundingShape;

    public boolean allowTranslation;
    public boolean allowRotation;

    public DefaultCritter() {
        this(new Vector2D(0, 0), 0);
    }

    public DefaultCritter(Vector2D position, double orientation) {
        super(position, orientation);

        healthPoints = DEFAULT_HEALTH_POINTS;
        boundingShape = new Circle(position, DEFAULT_COLLISION_RADIUS);

        allowTranslation = true;
        allowRotation = true;
    }

    public void takeDamage(int damagePoints) {
        healthPoints -= damagePoints;
    }

    // draw simple representation of DefaultCritter for debugging / testing
    public void draw() {
        StdDraw.setPenColor(StdDraw.RED);
        // draw line of sight
        Vector2D aimTarget = position.add(lookVector().scale(100));
        StdDraw.line(position.x, position.y, aimTarget.x, aimTarget.y);
        // draw bounding shape
        boundingShape.draw();
    }

    @Override
    public BoundingShape getBoundingShape() {
        return boundingShape;
    }

    @Override
    public boolean isCollidingWith(Collidable other) {
        return this.getBoundingShape().intersects(other.getBoundingShape());
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