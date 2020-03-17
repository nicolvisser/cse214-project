import java.awt.Color;

/**
 * Shooter
 */
public class Shooter extends DefaultCritter {

    public final int MOVEMENT_BOUNDARY_XMIN = -350;
    public final int MOVEMENT_BOUNDARY_XMAX = 350;

    private final int THRUSTER_ACCELERATION_MAGNITUDE = 4000;
    public boolean thrusterLeftMoveStatus = false;
    public boolean thrusterRightMoveStatus = false;

    private final double TURRET_ANGULAR_ACCELERATION_MAGNITUDE = 40;
    public boolean turretLeftRotateStatus = false;
    public boolean turretRightRotateStatus = false;

    private int playerIdx;

    public Shooter(Vector2D position, double orientation, int playerIdx) {
        super(position, orientation);
        this.playerIdx = playerIdx;
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

        // determine angular velocity from turret rotation status
        angularAcceleration = 0;
        if (turretLeftRotateStatus)
            angularAcceleration += TURRET_ANGULAR_ACCELERATION_MAGNITUDE;
        if (turretRightRotateStatus)
            angularAcceleration -= TURRET_ANGULAR_ACCELERATION_MAGNITUDE;

        // if almost no 'torque' applied or 'torque' applied in opposite direction than
        // rotation, then slow down turret for fast stopping or turning
        if (angularVelocity * angularAcceleration < 0.001) {
            angularVelocity *= 0.5;
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

        // keep rotation in [0.2, 2*PI - 0.2] interval
        orientation = Math.min(orientation, Math.PI - 0.2); // todo fix hardcoding
        orientation = Math.max(orientation, 0 + 0.2); // todo fix hardcoding

    }

    @Override
    public void draw() {

        Color playerColor = playerIdx == 0 ? StdDraw.BLUE : StdDraw.RED;

        // draw line for line of sight
        Vector2D aimTarget = Vector2D.sum(position, Vector2D.scalarMultiplication(500, FWDVector()));
        StdDraw.setPenColor(playerColor);
        StdDraw.line(position.x, position.y, aimTarget.x, aimTarget.y);

        // draw circle for body
        StdDraw.setPenColor(playerColor);
        StdDraw.filledCircle(position.x, position.y, 10);

    }

}