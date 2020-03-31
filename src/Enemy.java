import java.util.ArrayList;

/**
 * Enemy
 */
public class Enemy extends DefaultCritter {

    enum EnemyState {
        ALIVE, EXPLODING, DEAD;
    }

    Rectangle canvas;

    public static int DEFAULT_HEALTH_POINTS = 100;
    public static int DEFAULT_COLLISION_RADIUS = 5;

    private static final long serialVersionUID = 1L;

    public EnemyState state;

    private AnimatedPicture explosion;

    public Enemy(Rectangle canvas, Vector2D position, double orientation) {
        super(position, orientation);
        this.canvas = canvas;
        state = EnemyState.ALIVE;
        healthPoints = DEFAULT_HEALTH_POINTS;
        collisionRadius = DEFAULT_COLLISION_RADIUS;
        explosion = new AnimatedPicture("resources/images/explosion", "png", 16,
                AnimatedPicture.AnimationType.FWD_BWD_ONCE);
    }

    public int handleCollisionWithMissile(Missile missile) {
        int points = 0;
        if ((state == EnemyState.ALIVE) && (missile.state == Missile.MissileState.TRAVELLING)
                && this.isCollidingWith(missile)) {
            points += missile.missileDamage; // TODO: Better points system than just missile damage
            takeDamage(missile.missileDamage);
            missile.takeDamage(Integer.MAX_VALUE);
        }
        return points;
    }

    public int handleCollisionsWithMissiles(ArrayList<Missile> missiles) {
        int points = 0;
        for (Missile missile : missiles) {
            points += handleCollisionWithMissile(missile);
        }
        return points;
    }

    public boolean isTouchingBottomOrShooter(Shooter shooter) {
        return (this.position.y - collisionRadius <= canvas.getYmin()) || (this.isCollidingWith(shooter));
    }

    @Override
    public void renderStep(double dt) {
        switch (state) {
            case ALIVE:
                if (healthPoints <= 0) {
                    state = EnemyState.EXPLODING;
                    break;
                }
                super.renderStep(dt);
                break;

            case EXPLODING:
                super.renderStep(dt);
                if (explosion.finished)
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
    }

}