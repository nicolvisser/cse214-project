/**
 * Missile
 */
public class Missile extends DefaultCritter {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private static final int SPEED = 800;

    enum MissileState {
        TRAVELLING, EXPLODING, DEAD;
    }

    public int missileDamage;
    public MissileState state;
    private AnimatedPicture explosion;

    public Missile(Vector2D position, Vector2D direction) {
        super(position, Object2D.orientationFromVector(direction));
        velocity = new Vector2D(SPEED * direction.x, SPEED * direction.y);
        collisionRadius = 5;
        missileDamage = 100;
        state = MissileState.TRAVELLING;
        explosion = new AnimatedPicture("resources/explosion", "png", 16, AnimatedPicture.AnimationType.FWD_BWD_ONCE);
    }

    @Override
    public void draw() {
        switch (state) {
            case TRAVELLING:
                StdDraw.picture(position.x, position.y, "resources/missile.png", 20, 20, orientationInDegrees());
                break;
            case EXPLODING:
                explosion.draw(position.x, position.y, 0);
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
                if (!this.isAlive()) {
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