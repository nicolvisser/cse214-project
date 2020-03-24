import java.util.ArrayList;

/**
 * Enemy
 */
public class Enemy extends DefaultCritter {

    public static int DEFAULT_COLLISION_RADIUS = 20;

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public Enemy() {
        super();
        healthPoints = 100;
        collisionRadius = DEFAULT_COLLISION_RADIUS;
    }

    public Enemy(Vector2D position, double orientation) {
        super(position, orientation);
        healthPoints = 100;
        collisionRadius = DEFAULT_COLLISION_RADIUS;
    }

    public int handleCollisionsWithMissiles(ArrayList<Missile> missiles) {
        int points = 0;
        for (Missile missile : missiles) {
            if (this.isCollidingWith(missile)) {
                points += missile.missileDamage; // TODO: Better points system than just missile damage
                takeDamage(missile.missileDamage);
                missile.takeDamage(Integer.MAX_VALUE);
                break;
            }
        }
        return points;
    }

    public boolean isTouchingBottomOrShooter(Shooter shooter) {
        return (this.position.y - collisionRadius <= 0) || (this.isCollidingWith(shooter));
    }

}