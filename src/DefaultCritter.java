public class DefaultCritter extends Object2D implements Critter, Collidable {

    private static final long serialVersionUID = 1L;
    private static final int DEFAULT_HEALTH_POINTS = 100;

    private final BoundingShape boundingShape;

    public final Vector2D position; // override and make FINAL

    public int healthPoints;

    public boolean allowTranslation;
    public boolean allowRotation;

    public DefaultCritter(double x, double y, double radius, double orientation) {
        this(new Circle(x, y, radius), orientation);
    }

    public DefaultCritter(double x, double y, double width, double height, double orientation) {
        this(new Rectangle(x, y, width, height), orientation);
    }

    private DefaultCritter(BoundingShape shape, double orientation) {
        super(shape.getPosition(), orientation);
        position = shape.getPosition();
        // <--- sets position to shape's position. since position is final in
        // DefaultCritter, these two should not go out of sync, unless using
        // super class to change position's reference. IS THIS OKAY PROGRAMMING ?

        boundingShape = shape;

        healthPoints = DEFAULT_HEALTH_POINTS;

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

    public boolean isCollidingWith(Ray ray) {
        return this.getBoundingShape().intersects(ray);
    }

    public void render(double dt) {
        if (allowTranslation)
            renderTranslation(dt);
        if (allowRotation)
            renderRotation(dt);
    }

    @Override
    public void prepareToSaveState() {

    }

    @Override
    public void handlePossibleCollisionWith(Collidable other) {
        // TODO Auto-generated method stub

    }

}