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

    @Override
    public void draw() {
        // TODO Auto-generated method stub
        super.draw();
    }

}