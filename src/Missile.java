/**
 * Missile
 */
public class Missile extends DefaultCritter {

    enum MissileState {
        TRAVELLING, EXPLODING, DEAD;
    }

    public static final int SPEED = 200;

    private static final long serialVersionUID = 1L;
    private static final double DEFAULT_COLLISION_RADIUS = 1.5;
    private static final int DEFAULT_MISSILE_DAMAGE = 100;
    private static final int DEFAULT_HEALTH_POINTS = 10;

    public int missileDamage;
    public MissileState state;

    private AnimatedPicture explosion;

    public Missile(Vector2D position, Vector2D direction) {
        super(position, Object2D.orientationFromVector(direction));
        velocity = new Vector2D(SPEED * direction.x, SPEED * direction.y);
        healthPoints = DEFAULT_HEALTH_POINTS;
        collisionRadius = DEFAULT_COLLISION_RADIUS;
        missileDamage = DEFAULT_MISSILE_DAMAGE;
        state = MissileState.TRAVELLING;
        explosion = new AnimatedPicture("resources/images/explosion", "png", 16, AnimatedPicture.AnimationType.FWD_BWD_ONCE);
    }

    @Override
    public void draw() {
        switch (state) {
            case TRAVELLING:
                StdDraw.picture(position.x, position.y, "resources/images/missile.png", 5, 5, orientationInDegrees());
                break;

            case EXPLODING:
                explosion.draw(position.x, position.y, 5, 5);
                break;

            case DEAD:
                break;
        }
    }

    @Override
    public void renderStep(double dt) {
        switch (state) {
            case TRAVELLING:
                super.renderStep(dt);
                if (healthPoints <= 0) {
                    StdAudio.play("resources/audio/Explosion+1.wav");
                    state = MissileState.EXPLODING;
                }
                break;

            case EXPLODING:
                velocity = Vector2D.scalarMultiplication(0.85, velocity); // slow down movement speed of explosion
                super.renderStep(dt);
                if (explosion.finished) {
                    state = MissileState.DEAD;
                }
                break;

            case DEAD:
                break;
        }
    }
}