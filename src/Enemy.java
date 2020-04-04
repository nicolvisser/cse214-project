import java.util.ArrayList;

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

    private Circle boundingCircle;

    public Enemy(Rectangle canvas, Vector2D position, double orientation) {
        super(position, orientation);
        this.canvas = canvas;
        state = EnemyState.ALIVE;
        healthPoints = DEFAULT_HEALTH_POINTS;
        boundingCircle = (Circle) getBoundingShape();
        boundingCircle.radius = DEFAULT_COLLISION_RADIUS; // TODO maybe rather use constructor for this?
        explosion = new AnimatedPicture("resources/images/explosion", "png", 16,
                AnimatedPicture.AnimationType.FWD_BWD_ONCE);
    }

    public int handleCollisionWithMissile(Missile missile) {
        int points = 0;
        if ((state == EnemyState.ALIVE) && (missile.state == Missile.MissileState.TRAVELLING)
                && this.isCollidingWith(missile)) {
            points += missile.missileDamage; // TODO: Better points system than just missile damage
            takeDamage(missile.missileDamage);
            missile.takeDamage();
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

    @Override
    public void render(double dt) {

        boundingCircle.center = position; // TODO: Stop forcing these to be equal, and use some other mechanism

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