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
        // draw line for line of sight
        Vector2D aimTarget = Vector2D.sum(position, Vector2D.scalarMultiplication(100, FWDVector()));
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.line(position.x, position.y, aimTarget.x, aimTarget.y);

        // draw circle for body
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.filledCircle(position.x, position.y, collisionRadius);
    }

}