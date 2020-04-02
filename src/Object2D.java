public class Object2D extends Particle2D {

    private static final long serialVersionUID = 1L;

    private double orientation;
    public double angularVelocity;
    public double angularAcceleration;

    public Object2D() {
        super();
        orientation = 0;
        angularVelocity = 0;
        angularAcceleration = 0;
    }

    public Object2D(Vector2D position) {
        super(position);
        orientation = 0;
        angularVelocity = 0;
        angularAcceleration = 0;
    }

    public Object2D(Vector2D position, double orientation) {
        super(position);
        this.orientation = orientation;
        angularVelocity = 0;
        angularAcceleration = 0;
    }

    public Double getOrientation() {
        return orientation;
    }

    public Double getOrientationInDegrees() {
        return orientation / Math.PI * 180;
    }

    // keeps orientation within [0, 2*PI), hence private implementation
    public void setOrientation(double orientation) {
        while (orientation >= 2 * Math.PI)
            orientation -= 2 * Math.PI;
        while (orientation < 0)
            orientation += 2 * Math.PI;
        this.orientation = orientation;
    }

    public void rotate(double radians) {
        setOrientation(orientation + radians);
    }

    public void lookAt(Particle2D other) {
        setOrientation(this.positionRelativeTo(other).getPolarAngle());
    }

    public void lookAt(double x, double y) {
        Vector2D relativeVector = new Vector2D(x - position.x, y - position.y);
        setOrientation(relativeVector.getPolarAngle());
    }

    public Vector2D lookVector() {
        return new Vector2D(Math.cos(orientation), Math.sin(orientation));
    }

    public Vector2D backVector() {
        return new Vector2D(-Math.cos(orientation), -Math.sin(orientation));
    }

    public Vector2D leftVector() {
        return new Vector2D(-Math.sin(orientation), Math.cos(orientation));
    }

    public Vector2D rightVector() {
        return new Vector2D(Math.sin(orientation), -Math.cos(orientation));
    }

    public void renderRotation(double dt) {
        angularVelocity += angularAcceleration * dt;
        orientation += angularVelocity * dt + 0.5 * angularAcceleration * dt * dt;
    }

    public void render(double dt) {
        super.renderTranslation(dt);
        this.renderRotation(dt);
    }

}