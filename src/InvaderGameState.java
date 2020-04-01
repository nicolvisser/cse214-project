import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.Color;

/**
 * InvaderGameState
 */
public class InvaderGameState extends KeyListener implements Serializable {

    private static final long serialVersionUID = 1L;

    public boolean pauseFlag = false;
    public boolean quitFlag = false;
    public boolean gameOverFlag = false;

    public void resetFlags() {
        pauseFlag = false;
        quitFlag = false;
        gameOverFlag = false;
    }

    int score;

    Shooter shooter;

    EnemyWave enemyWave;

    ArrayList<PowerUp> powerUps;

    ArrayList<Bunker> bunkers;

    public InvaderGameState(Rectangle drawArea) {

        score = 0;

        shooter = new Shooter(new Vector2D(0, -75), Math.PI / 2, drawArea);
        enemyWave = new EnemyWave(drawArea, shooter);

        powerUps = new ArrayList<>();
        powerUps.add(new PowerUp(new Vector2D(0, 0), PowerUp.PowerUpType.FAST_RELOAD));
        powerUps.add(new PowerUp(new Vector2D(10, 200), PowerUp.PowerUpType.FAST_ENERGY_GAIN));
        powerUps.add(new PowerUp(new Vector2D(-10, 400), PowerUp.PowerUpType.RED));
        powerUps.add(new PowerUp(new Vector2D(0, 600), PowerUp.PowerUpType.GREEN));

        bunkers = new ArrayList<>();
        bunkers.add(new Bunker(new Rectangle(-20, -30, 30, 10), 20, 5));
        bunkers.add(new Bunker(new Rectangle(-60, -30, 30, 10), 20, 5));
        bunkers.add(new Bunker(new Rectangle(20, -30, 30, 10), 20, 5));
        bunkers.add(new Bunker(new Rectangle(60, -30, 30, 10), 20, 5));

    }

    public void render(double dt) {

        shooter.render(dt);
        shooter.getMissileLauncher().addAbilityToEquipPowerUp(powerUps);

        Iterator<PowerUp> itr = powerUps.iterator();
        while (itr.hasNext()) {
            PowerUp powerUp = itr.next();
            if (powerUp.state == PowerUp.PowerUpState.DEACTIVE) {
                itr.remove();
            } else {
                powerUp.render(dt);
                if (shooter.isCollidingWith(powerUp)) {
                    powerUp.addEffectTo(shooter);
                }
            }
        }

        enemyWave.render(dt);
        score += enemyWave.handleCollisionsWithMissiles(shooter.getMissileLauncher().missiles);
        gameOverFlag = enemyWave.isCleared() || (shooter.state == Shooter.ShooterState.DEAD);

        for (Bunker bunker : bunkers) {
            for (Missile missile : shooter.getMissileLauncher().missiles) {
                bunker.handlePossibleCollisionWith(missile);
            }

            for (Missile enemyMissile : enemyWave.enemyMissiles) {
                bunker.handlePossibleCollisionWith(enemyMissile);
            }
        }
    }

    public void draw() {

        for (Bunker bunker : bunkers) {
            bunker.draw();
        }

        shooter.draw();

        enemyWave.draw();

        for (PowerUp powerUp : powerUps) {
            powerUp.draw();
        }

        drawHealthBar((double) shooter.healthPoints / Shooter.DEFAULT_HEALTH_POINTS);
        drawEnergyBar(shooter.energyPoints / Shooter.DEFAULT_ENERGY_POINTS);
        drawScore(score);

    }

    @Override
    public void onKeyPress(KeyListener.KeyboardKey key) {
        switch (key) {
            case A_KEY:
                shooter.isThrusterLeftActive = true;
                break;
            case D_KEY:
                shooter.isThrusterRightActive = true;
                break;
            case LEFT_ARROW:
                shooter.getMissileLauncher().turretLeftRotateStatus = true;
                break;
            case RIGHT_ARROW:
                shooter.getMissileLauncher().turretRightRotateStatus = true;
                break;
            case ESC_KEY:
                pauseFlag = true;
                break;
            case Q_KEY:
                quitFlag = true;
                break;
            case UP_ARROW:
                shooter.getMissileLauncher().startCharging();
                break;
            case DOWN_ARROW:
                shooter.activateShield();
                break;

            default:
                break;
        }
    }

    @Override
    public void onKeyRelease(KeyListener.KeyboardKey key) {
        switch (key) {
            case A_KEY:
                shooter.isThrusterLeftActive = false;
                break;
            case D_KEY:
                shooter.isThrusterRightActive = false;
                break;
            case LEFT_ARROW:
                shooter.getMissileLauncher().turretLeftRotateStatus = false;
                break;
            case RIGHT_ARROW:
                shooter.getMissileLauncher().turretRightRotateStatus = false;
                break;
            case UP_ARROW:
                shooter.getMissileLauncher().shootMissile();
                break;
            case DOWN_ARROW:
                shooter.deactivateShield();
                break;

            default:
                break;
        }
    }

    public void drawHealthBar(double percentage) {
        final Color MAROON = new Color(128, 0, 0);
        drawStatusBar(percentage, -92.5, -87.5, MAROON, Color.RED, "resources/images/heart.png");
    }

    public void drawEnergyBar(double percentage) {
        final Color NAVY = new Color(0, 0, 128);
        final Color LIGHT_BLUE = new Color(128, 223, 255);
        drawStatusBar(percentage, -92.5, -92.5, NAVY, LIGHT_BLUE, "resources/images/energy.png");
    }

    public void drawStatusBar(double percentage, double x, double y, Color back, Color front, String iconFilename) {
        final int SPACING_ICON_BAR = 5;
        final int BAR_WIDTH = 80;

        StdDraw.picture(x, y, iconFilename, 4, 4);

        StdDraw.setPenColor(back);
        StdDraw.filledRectangle(x + SPACING_ICON_BAR + BAR_WIDTH / 2 * 1, y, BAR_WIDTH / 2, 1);

        if (percentage > 0) {
            StdDraw.setPenColor(front);
            StdDraw.filledRectangle(x + SPACING_ICON_BAR + BAR_WIDTH / 2 * percentage, y, BAR_WIDTH / 2 * percentage,
                    1);
        }
    }

    public void drawScore(int score) {
        StdDraw.setPenColor(StdDraw.ORANGE);
        StdDraw.text(50, -87.5, "SCORE: " + score);
    }

    public Vector2D getShooterVelocity() {
        return shooter.velocity;
    }

}