import java.io.Serializable;

public class Particle2D implements Serializable {

    private static final long serialVersionUID = 1L;

    public Vector2D position;
    public Vector2D velocity;
    public Vector2D acceleration;

    public Particle2D() {
        position = new Vector2D(0, 0);
        velocity = new Vector2D(0, 0);
        acceleration = new Vector2D(0, 0);
    }

    public Particle2D(Vector2D position) {
        this.position = position;
        velocity = new Vector2D(0, 0);
        acceleration = new Vector2D(0, 0);
    }

    public void translateX(double dx) {
        position.x += dx;
    }

    public void translateY(double dy) {
        position.y += dy;
    }

    public void renderTranslation(double dt) {
        velocity.x += acceleration.x * dt;
        velocity.y += acceleration.y * dt;
        position.x += velocity.x * dt + 0.5 * acceleration.x * dt * dt;
        position.y += velocity.y * dt + 0.5 * acceleration.y * dt * dt;
    }

    public void render(double dt) {
        renderTranslation(dt);
    }

    public Vector2D positionRelativeTo(Particle2D other) {
        return this.position.subtract(other.position);
    }

    public double distanceTo(Particle2D other) {
        return positionRelativeTo(other).magnitude();
    }

}