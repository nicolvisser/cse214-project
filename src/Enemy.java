/**
 * Enemy
 */
public class Enemy extends DefaultCritter {

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