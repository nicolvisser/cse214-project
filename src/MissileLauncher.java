import java.util.ArrayList;
import java.util.Iterator;

/**
 * MissileLauncher
 */
public class MissileLauncher extends DefaultCritter {

    private static final long serialVersionUID = 1L;

    public static final double DEFAULT_RELOAD_TIME = 0.15;
    private static final double TURRET_ANGULAR_ACCELERATION_MAGNITUDE = 40;

    RectangleDimension canvas;
    Shooter shooterRef;
    ArrayList<Missile> missiles;
    public double reloadTime;
    double timeSinceLastMissile;
    double chargeUpTime; // keeps track of how long user held shoot key down
    public boolean turretLeftRotateStatus;
    public boolean turretRightRotateStatus;

    private ArrayList<PowerUp> powerUpsRef;

    public MissileLauncher(RectangleDimension canvas, Shooter shooterRef) {
        this.canvas = canvas;
        this.shooterRef = shooterRef;
        missiles = new ArrayList<>();
        reloadTime = DEFAULT_RELOAD_TIME;
        timeSinceLastMissile = DEFAULT_RELOAD_TIME;
        setOrientation(Math.PI / 2);
        turretLeftRotateStatus = false;
        turretRightRotateStatus = false;

        shooterRef.addMissileLauncherReference(this);

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
        setOrientation(Math.min(getOrientation(), Math.PI - 0.2)); // TODO: fix hardcoding
        setOrientation(Math.max(getOrientation(), 0 + 0.2)); // TODO: fix hardcoding

        Iterator<Missile> missileIterator = missiles.iterator();
        while (missileIterator.hasNext()) {
            Missile missile = missileIterator.next();
            missile.renderStep(dt);
            if (missile.state == Missile.MissileState.DEAD || !canvas.doesContainPoint(missile.position)) {
                missileIterator.remove();
            } else if (missile.state == Missile.MissileState.TRAVELLING && powerUpsRef != null) {
                Iterator<PowerUp> powerUpIterator = powerUpsRef.iterator();
                while (powerUpIterator.hasNext()) {
                    PowerUp powerUp = powerUpIterator.next();
                    if (powerUp.state == PowerUp.PowerUpState.TRAVELLING && missile.isCollidingWith(powerUp)) {
                        powerUp.addEffectTo(shooterRef);
                    }
                }
            }
        }
    }

    public void draw() {

        if (shooterRef.state == Shooter.ShooterState.ALIVE) {
            StdDraw.picture(shooterRef.position.x, shooterRef.position.y, "resources/images/turret.png", 25, 10,
                    getOrientationInDegrees());
        }

        for (Missile missile : missiles) {
            missile.draw();
        }
    }

    public void startCharging() {
        chargeUpTime = 0;
    }

    public void shootMissile() {
        if (timeSinceLastMissile > reloadTime) {
            // System.out.println("Launched Missile With chargeUpTime: " + chargeUpTime);
            timeSinceLastMissile = 0;
            Vector2D missileStartPos = new Vector2D(shooterRef.position.x, shooterRef.position.y)
                    .add(lookVector().scale(12.5));
            Missile missile = new Missile(missileStartPos, this.lookVector());
            missiles.add(missile);
            StdAudio.play("resources/audio/Gun+1.wav");
        }
        chargeUpTime = 0;
    }

    public void addAbilityToEquipPowerUp(ArrayList<PowerUp> powerUpsRef) {
        this.powerUpsRef = powerUpsRef;
    }

    public ArrayList<PowerUp> getPowerUpsRef() {
        return this.powerUpsRef;
    }

}