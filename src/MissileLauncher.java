import java.util.ArrayList;

/**
 * MissileLauncher
 */
public class MissileLauncher {

    public static final double RELOAD_TIME = 0.15;

    Shooter shooter;

    double timeSinceLastMissile; // should not have overflow problems, since game will end soon enough
                                 // if you don't shoot missiles often

    double chargeUpTime; // keeps track of how long user held shoot key down

    ArrayList<Missile> missiles;
    ArrayList<Missile> missilesReadyToBurst;

    public MissileLauncher(Shooter shooter) {
        this.shooter = shooter;
        missiles = new ArrayList<>();
        missilesReadyToBurst = new ArrayList<>();
        timeSinceLastMissile = RELOAD_TIME;

    }

    public void renderStep(double dt) {
        timeSinceLastMissile += dt;
        chargeUpTime += dt;

        for (int i = 0; i < missiles.size(); i++) {
            Missile missile = missiles.get(i);

            missile.renderStep(dt);

            if (!missile.isAlive()) {
                missiles.remove(missile);
                i--;
                missilesReadyToBurst.add(missile);

            } else if (!Invaders.isPointOnCanvas(missile.position)) {
                missiles.remove(missile);
                i--;
            }

        }
    }

    public void burstDeadMissiles(EnemyWave enemyWave) {
        for (Missile missile : missilesReadyToBurst) {
            enemyWave.handleMissileBurst(missile.position, missile.burstRadius);
            StdDraw.setPenColor(StdDraw.WHITE);
            StdDraw.circle(missile.position.x, missile.position.y, missile.burstRadius);
        }
        missilesReadyToBurst.clear();
    }

    public void draw() {
        for (Missile missile : missiles) {
            missile.draw();
        }
    }

    public void startCharging() {
        chargeUpTime = 0;
    }

    public void shootMissile() {
        if (timeSinceLastMissile > RELOAD_TIME) {
            System.out.println("Power: " + chargeUpTime);
            timeSinceLastMissile = 0;
            Vector2D missileStartPos = new Vector2D(shooter.position.x, shooter.position.y); // TODO fix missile
                                                                                             // spawning on top of
                                                                                             // player
            Missile missile = new Missile(missileStartPos, shooter.FWDVector(), 100);
            missiles.add(missile);
        }
        chargeUpTime = 0;
    }

}