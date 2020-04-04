public class Enemy extends DefaultCritter {

    enum EnemyState {
        ALIVE, EXPLODING, DEAD;
    }

    Rectangle canvas;

    public static int DEFAULT_HEALTH_POINTS = 100;
    public static final int DEFAULT_COLLISION_RADIUS = 5;

    private static final long serialVersionUID = 1L;

    public EnemyState state;

    private AnimatedPicture explosion;

    // private Circle boundingCircle;

    public Enemy(Rectangle canvas, Vector2D position, double orientation) {
        super(position.x, position.y, DEFAULT_COLLISION_RADIUS, orientation);
        this.canvas = canvas;
        state = EnemyState.ALIVE;
        healthPoints = DEFAULT_HEALTH_POINTS;
        // boundingCircle = (Circle) getBoundingShape(); // cast to circle to use
        // methods in this class
        explosion = new AnimatedPicture("resources/images/explosion", "png", 16,
                AnimatedPicture.AnimationType.FWD_BWD_ONCE);
    }

    @Override
    public void handlePossibleCollisionWith(Collidable other) {
        if (other instanceof Missile) {
            Missile missile = (Missile) other;

            if (missile.state == Missile.MissileState.TRAVELLING && state == EnemyState.ALIVE
                    && this.isCollidingWith(missile)) {
                takeDamage(missile.missileDamage);
                missile.takeDamage();
            }

        } else if (other instanceof Shooter) {
            Shooter shooter = (Shooter) other;

            shooter.handlePossibleCollisionWith(this);

        } else if (other instanceof Bunker) {
            Bunker bunker = (Bunker) other;

            bunker.handlePossibleCollisionWith(this);
        }
    }

    @Override
    public void render(double dt) {

        switch (state) {
            case ALIVE:
                if (healthPoints <= 0) {
                    state = EnemyState.EXPLODING;
                    break;
                }
                super.render(dt);
                break;

            case EXPLODING:
                super.render(dt);
                if (explosion.isFinished)
                    state = EnemyState.DEAD;
                break;

            case DEAD:
                break;
        }
    }

    @Override
    public void draw() {
        switch (state) {
            case ALIVE:
                StdDraw.picture(position.x, position.y, "resources/images/enemy.png", 10, 10,
                        getOrientationInDegrees());
                break;

            case EXPLODING:
                explosion.draw(position.x, position.y, 0);
                break;

            case DEAD:
                break;
        }

        // -----> for debugging:
        if (Invaders.DEBGGING_ON) {
            StdDraw.setPenColor(StdDraw.MAGENTA);
            getBoundingShape().draw();
        }
        //
    }

}