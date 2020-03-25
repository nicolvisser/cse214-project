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

    public MissileLauncher(Shooter shooter) {
        this.shooter = shooter;
        missiles = new ArrayList<>();
        timeSinceLastMissile = RELOAD_TIME;

    }

    public void renderStep(double dt) {
        timeSinceLastMissile += dt;
        chargeUpTime += dt;

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
        for (Missile missile : missiles) {
            missile.draw();
        }
    }

    public void startCharging() {
        chargeUpTime = 0;
    }

    public void shootMissile() {
        if (timeSinceLastMissile > RELOAD_TIME) {
            System.out.println("Launched Missile With chargeUpTime: " + chargeUpTime);
            timeSinceLastMissile = 0;
            Vector2D missileStartPos = new Vector2D(shooter.position.x, shooter.position.y); // TODO fix missile
                                                                                             // spawning on top of
                                                                                             // player
            Missile missile = new Missile(missileStartPos, shooter.FWDVector());
            missiles.add(missile);
        }
        chargeUpTime = 0;
    }

}