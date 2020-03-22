import java.util.ArrayList;

/**
 * Enemy
 */
public class Enemy extends DefaultCritter {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public Enemy() {
        super();
        healthPoints = 100;
        collisionRadius = 20;
    }

    public Enemy(Vector2D position, double orientation) {
        super(position, orientation);
        healthPoints = 100;
        collisionRadius = 20;
    }

    public int handleCollisionsWithMissiles(ArrayList<Missile> missiles) {

        int points = 0;

        for (Missile missile : missiles) {

            Vector2D relativePositionVector = new Vector2D(position.x - missile.position.x,
                    position.y - missile.position.y);

            Double distanceBetween = relativePositionVector.magnitude();

            if (distanceBetween <= collisionRadius + missile.collisionRadius) {
                points += missile.missileDamage; // TODO: Better points system than just missile damage
                takeDamage(missile.missileDamage);
                missile.takeDamage(Integer.MAX_VALUE);
                break;
            }
        }

        return points;
    }

}