import java.util.ArrayList;

/**
 * MissileLauncher
 */
public class MissileLauncher extends DefaultCritter {

    private static final long serialVersionUID = 1L;

    public static final double DEFAULT_RELOAD_TIME = 0.15;
    private static final double TURRET_ANGULAR_ACCELERATION_MAGNITUDE = 40;

    Shooter shooter;
    ArrayList<Missile> missiles;
    public double reloadTime;
    double timeSinceLastMissile;
    double chargeUpTime; // keeps track of how long user held shoot key down
    public boolean turretLeftRotateStatus;
    public boolean turretRightRotateStatus;

    public MissileLauncher(Shooter shooter) {
        this.shooter = shooter;
        missiles = new ArrayList<>();
        reloadTime = DEFAULT_RELOAD_TIME;
        timeSinceLastMissile = DEFAULT_RELOAD_TIME;
        orientation = Math.PI / 2;
        turretLeftRotateStatus = false;
        turretRightRotateStatus = false;

        shooter.addMissileLauncherReference(this);

    }

    public void renderStep(double dt) {
        timeSinceLastMissile += dt;
        chargeUpTime += dt;

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

        // keep rotation in [0.2, 2*PI - 0.2] interval
        orientation = Math.min(orientation, Math.PI - 0.2); // todo fix hardcoding
        orientation = Math.max(orientation, 0 + 0.2); // todo fix hardcoding

        for (int i = 0; i < missiles.size(); i++) {
            Missile missile = missiles.get(i);

            missile.renderStep(dt);

            if (missile.state == Missile.MissileState.DEAD || !Invaders.isPointOnCanvas(missile.position)) {
                missiles.remove(missile);
                i--;
            }

        }
    }

    public void draw() {
        StdDraw.picture(shooter.position.x, shooter.position.y, "resources/turret.png", 100, 40,
                orientationInDegrees());

        for (Missile missile : missiles) {
            missile.draw();
        }
    }

    public void startCharging() {
        chargeUpTime = 0;
    }

    public void shootMissile() {
        if (timeSinceLastMissile > reloadTime) {
            System.out.println("Launched Missile With chargeUpTime: " + chargeUpTime);
            timeSinceLastMissile = 0;
            Vector2D missileStartPos = Vector2D.sum(new Vector2D(shooter.position.x, shooter.position.y),
                    Vector2D.scalarMultiplication(50, FWDVector()));
            Missile missile = new Missile(missileStartPos, this.FWDVector());
            missiles.add(missile);
        }
        chargeUpTime = 0;
    }

}