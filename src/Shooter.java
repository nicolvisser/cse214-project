/**
 * Shooter
 */
public class Shooter extends DefaultCritter {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public final int MOVEMENT_BOUNDARY_XMIN = -350;
    public final int MOVEMENT_BOUNDARY_XMAX = 350;

    private final int THRUSTER_ACCELERATION_MAGNITUDE = 4000;
    public boolean thrusterLeftMoveStatus = false;
    public boolean thrusterRightMoveStatus = false;

    public Shooter(Vector2D position, double orientation) {
        super(position, orientation);
        collisionRadius = 20;
    }

    @Override
    public void renderStep(double dt) {

        // determine acceleration from thrusterstatuses
        acceleration = Vector2D.zeroVector();
        if (thrusterLeftMoveStatus)
            acceleration = Vector2D.sum(acceleration,
                    Vector2D.scalarMultiplication(THRUSTER_ACCELERATION_MAGNITUDE, new Vector2D(-1, 0)));
        if (thrusterRightMoveStatus)
            acceleration = Vector2D.sum(acceleration,
                    Vector2D.scalarMultiplication(THRUSTER_ACCELERATION_MAGNITUDE, new Vector2D(1, 0)));

        // if almost no 'thrust' applied or thrust applied in opposite direction than
        // movement, then slow down shooter for fast stopping or turning
        if (velocity.x * acceleration.x < 0.001) {
            velocity = Vector2D.scalarMultiplication(0.5, velocity);
        }

        super.renderStep(dt);

        // keep player in boundaries
        if (position.x > MOVEMENT_BOUNDARY_XMAX) {
            position.x = MOVEMENT_BOUNDARY_XMAX;
            velocity.x = 0;
            acceleration.x = 0;
        } else if (position.x < MOVEMENT_BOUNDARY_XMIN) {
            position.x = MOVEMENT_BOUNDARY_XMIN;
            velocity.x = 0;
            acceleration.x = 0;
        }

    }

    @Override
    public void draw() {
        if (thrusterLeftMoveStatus & !thrusterRightMoveStatus) {
            StdDraw.picture(position.x, position.y, "resources/shooterL.png", 80, 80, orientationInDegrees());
        } else if (thrusterRightMoveStatus & !thrusterLeftMoveStatus) {
            StdDraw.picture(position.x, position.y, "resources/shooterR.png", 80, 80, orientationInDegrees());
        } else {
            StdDraw.picture(position.x, position.y, "resources/shooter.png", 80, 80, orientationInDegrees());
        }
    }

}