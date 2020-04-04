import java.util.ArrayList;
import java.util.Iterator;

public class Turret extends DefaultCritter {

    private static final long serialVersionUID = 1L;

    public static final double DEFAULT_RELOAD_TIME = 0.15;
    private static final double TURRET_ANGULAR_ACCELERATION_MAGNITUDE = 50;
    private static final double TURRENT_MINIMUM_ANGLE = 0.2;
    private static final double TURRENT_MAXIMUM_ANGLE = Math.PI - 0.2;
    private static final int DEFAULT_COLLISION_RADIUS = 1; // TODO rather use rectangle for turret

    private Rectangle drawArea;
    public Shooter shooterRef;

    public ArrayList<Missile> missiles;
    public double reloadTime;
    private double timeSinceLastMissile;
    private double chargeUpTime;

    public boolean turretLeftRotateStatus;
    public boolean turretRightRotateStatus;

    public boolean laserIsActive;

    private ArrayList<PowerUp> powerUpsRef;

    public Turret(Rectangle canvas, Shooter shooterRef) {
        super(shooterRef.position.x, shooterRef.position.y, DEFAULT_COLLISION_RADIUS, Math.PI / 2);
        allowTranslation = false;

        this.drawArea = canvas;
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
            angularAcceleration = TURRET_ANGULAR_ACCELERATION_MAGNITUDE;
        else if (turretRightRotateStatus)
            angularAcceleration = -TURRET_ANGULAR_ACCELERATION_MAGNITUDE;
        else
            angularAcceleration = 0;

        // if almost no 'torque' applied or 'torque' applied in opposite direction than
        // rotation, then slow down turret for fast stopping or turning
        if (angularVelocity * angularAcceleration < 0.001) {
            angularVelocity *= 0.5;
        }

        super.render(dt);

        // keep rotation in [0.2, 2*PI - 0.2] interval
        if (getOrientation() > TURRENT_MAXIMUM_ANGLE) {
            setOrientation(TURRENT_MAXIMUM_ANGLE);
            angularVelocity = 0;
            angularAcceleration = 0;
        } else if (getOrientation() < TURRENT_MINIMUM_ANGLE) {
            setOrientation(TURRENT_MINIMUM_ANGLE);
            angularVelocity = 0;
            angularAcceleration = 0;
        }

        Iterator<Missile> missileIterator = missiles.iterator();
        while (missileIterator.hasNext()) {
            Missile missile = missileIterator.next();

            missile.render(dt);

            if (missile.state == Missile.MissileState.DEAD || !drawArea.contains(missile.position)) {
                missileIterator.remove();

            } else if (missile.state == Missile.MissileState.TRAVELLING && powerUpsRef != null) {

                Iterator<PowerUp> powerUpIterator = powerUpsRef.iterator();
                while (powerUpIterator.hasNext()) {
                    PowerUp powerUp = powerUpIterator.next();

                    missile.handlePossibleCollisionWith(powerUp);

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
            Vector2D missileStartPos = getPositionOfEndOfTurret();
            Missile missile = new Missile(missileStartPos, this.lookVector(), shooterRef);
            missiles.add(missile);
            StdAudio.play("resources/audio/Gun+1.wav");
        }
        chargeUpTime = 0;
    }

    public void activateLaser() {
        laserIsActive = true;
    }

    public void deactivateLaser() {
        laserIsActive = false;
    }

    public void addAbilityToEquipPowerUp(ArrayList<PowerUp> powerUpsRef) {
        this.powerUpsRef = powerUpsRef;
    }

    public Vector2D getPositionOfEndOfTurret() {
        return new Vector2D(shooterRef.position.x, shooterRef.position.y).add(lookVector().scale(12.5));
    }

    public LineSegment getAimLine(ArrayList<Bunker> bunkers, EnemyWave enemyWave) {
        Vector2D start = getPositionOfEndOfTurret();

        Ray aimRay = new Ray(start, lookVector());
        double lengthOfAimLine = 200;

        for (Bunker bunker : bunkers) {
            if (bunker.getBoundingShape().intersects(aimRay)) {
                for (Bunker.Block block : bunker.blocks) {
                    Double lengthUntilCollision = aimRay.lengthUntilIntersection(block.getBoundingShape());
                    if (Double.isFinite(lengthUntilCollision) && lengthUntilCollision < lengthOfAimLine) {
                        lengthOfAimLine = lengthUntilCollision;
                    }
                }
            }
        }

        for (EnemyGroup enemyGroup : enemyWave.enemyGroups) {
            if (enemyGroup.boundingRect.intersects(aimRay)) {
                for (Enemy enemy : enemyGroup.enemies) {
                    if (enemy.state == Enemy.EnemyState.ALIVE) {
                        Double lengthUntilCollision = aimRay.lengthUntilIntersection(enemy.getBoundingShape());
                        if (Double.isFinite(lengthUntilCollision) && lengthUntilCollision < lengthOfAimLine) {
                            lengthOfAimLine = lengthUntilCollision;
                        }
                    }
                }
            }
        }

        return new LineSegment(start, lookVector(), lengthOfAimLine);

    }

    public void drawAimLine(ArrayList<Bunker> bunkers, EnemyWave enemyWave) {
        StdDraw.setPenColor(laserIsActive ? StdDraw.RED : StdDraw.GRAY);
        getAimLine(bunkers, enemyWave).draw();
    }

}