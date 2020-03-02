/**
 * Missile
 */
public class Missile extends DefaultCritter {

    public Missile(Vector2D position, Vector2D direction) {
        super(position, Object2D.orientationFromVector(direction));
    }

}