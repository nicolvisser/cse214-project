import java.util.ArrayList;
import java.util.Iterator;

/**
 * MissileLauncher
 */
public class MissileLauncher extends DefaultCritter {

    private static final long serialVersionUID = 1L;

    public static final double DEFAULT_RELOAD_TIME = 0.15;
    private static final double TURRET_ANGULAR_ACCELERATION_MAGNITUDE = 40;
    private static final double TURRENT_MINIMUM_ANGLE = 0.2;
    private static final double TURRENT_MAXIMUM_ANGLE = Math.PI - 0.2;

    private Rectangle drawArea;
    private Shooter shooterRef;

    public ArrayList<Missile> missiles;
    public double reloadTime;
    private double timeSinceLastMissile;
    private double chargeUpTime;

    public boolean turretLeftRotateStatus;
    public boolean turretRightRotateStatus;

    private ArrayList<PowerUp> powerUpsRef;

    public MissileLauncher(Rectangle drawArea, Shooter shooterRef) {
        super(shooterRef.position, Math.PI / 2);

        this.drawArea = drawArea;
        this.shooterRef = shooterRef;

        missiles = new ArrayList<>();
        timeSinceLastMissile = DEFAULT_RELOAD_TIME;
        reloadTime = DEFAULT_RELOAD_TIME;

        turretLeftRotateStatus = false;
        turretRightRotateStatus = false;
    }

    public void render(double dt) {
        timeSinceLastMissile += dt;
        chargeUpTime += dt;

        // determine angular velocity from turret rotation status
        if (turretLeftRotateStatus)
            angularAcceleration += TURRET_ANGULAR_ACCELERATION_MAGNITUDE;
        else if (turretRightRotateStatus)
            angularAcceleration -= TURRET_ANGULAR_ACCELERATION_MAGNITUDE;
        else
            angularAcceleration = 0;

        // if almost no 'torque' applied or 'torque' applied in opposite direction than
        // rotation, then slow down turret for fast stopping or turning
        if (angularVelocity * angularAcceleration < 0.001) {
            angularVelocity *= 0.5;
        }

        super.render(dt);

        // keep rotation in [0.2, 2*PI - 0.2] interval
        setOrientation(Math.min(getOrientation(), TURRENT_MAXIMUM_ANGLE));
        setOrientation(Math.max(getOrientation(), TURRENT_MINIMUM_ANGLE));

        Iterator<Missile> missileIterator = missiles.iterator();
        while (missileIterator.hasNext()) {
            Missile missile = missileIterator.next();
            missile.render(dt);
            if (missile.state == Missile.MissileState.DEAD || !drawArea.containsPoint(missile.position)) {
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

}