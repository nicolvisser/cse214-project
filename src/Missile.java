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

    public DefaultCritter owner;

    // private Circle boundingCircle;

    private AnimatedPicture explosion;

    public Missile(Vector2D position, Vector2D direction, DefaultCritter owner) {
        super(position.x, position.y, DEFAULT_COLLISION_RADIUS, direction.getPolarAngle());
        velocity = new Vector2D(SPEED * direction.x, SPEED * direction.y);
        allowRotation = false;
        healthPoints = DEFAULT_HEALTH_POINTS;
        this.owner = owner;
        // boundingCircle = (Circle) getBoundingShape(); // cast to circle to use
        // methods in this class
        missileDamage = DEFAULT_MISSILE_DAMAGE;
        state = MissileState.TRAVELLING;
        explosion = new AnimatedPicture("resources/images/explosion", "png", 16,
                AnimatedPicture.AnimationType.FWD_BWD_ONCE);
    }

    public void takeDamage() { // able to 'kill' missile regardless of damage
        healthPoints = 0;
    }

    @Override
    public void draw() {
        switch (state) {
            case TRAVELLING:
                StdDraw.picture(position.x, position.y, "resources/images/missile.png", 5, 5,
                        getOrientationInDegrees());
                break;

            case EXPLODING:
                explosion.draw(position.x, position.y, 5, 5);
                break;

            case DEAD:
                break;
        }

        // -----> for debugging
        if (Invaders.DEBGGING_ON) {
            StdDraw.setPenColor(StdDraw.YELLOW);
            getBoundingShape().draw();
        }
        // <-----
    }

    @Override
    public void render(double dt) {

        switch (state) {
            case TRAVELLING:
                super.render(dt);
                if (healthPoints <= 0) {
                    StdAudio.play("resources/audio/Explosion+1.wav");
                    state = MissileState.EXPLODING;
                }
                break;

            case EXPLODING:
                velocity = velocity.scale(0.85); // slow down movement speed of explosion
                super.render(dt);
                if (explosion.isFinished) {
                    state = MissileState.DEAD;
                }
                break;

            case DEAD:
                break;
        }
    }

    @Override
    public void handlePossibleCollisionWith(Collidable other) {
        if (state == MissileState.TRAVELLING && isCollidingWith(other)) {

            if (other instanceof Missile) {
                Missile otherMissile = (Missile) other;
                this.takeDamage();
                otherMissile.takeDamage();

            } else if (other instanceof Shooter) {
                Shooter shooter = (Shooter) other;

                if (shooter.getShieldState()) {
                    this.takeDamage();
                    StdAudio.play("resources/audio/shieldUp.wav");

                } else {
                    this.takeDamage();
                    shooter.takeDamage(missileDamage);

                }

            } else if (other instanceof PowerUp) {
                PowerUp powerUp = (PowerUp) other;

                if (powerUp.state == PowerUp.PowerUpState.TRAVELLING) {

                    if (owner instanceof Shooter) {
                        Shooter shooter = (Shooter) owner;

                        powerUp.addEffectTo(shooter);

                    }
                }
            }
        }
    }
}