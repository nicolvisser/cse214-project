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

    MissileLauncher missileLauncher;

    EnemyWave enemyWave;

    ArrayList<PowerUp> powerUps;

    public InvaderGameState(RectangleDimension canvas) {

        score = 0;

        shooter = new Shooter(new Vector2D(0, -75), Math.PI / 2);

        missileLauncher = new MissileLauncher(canvas, shooter);

        enemyWave = new EnemyWave(canvas, shooter);

        powerUps = new ArrayList<>();
        powerUps.add(new PowerUp(new Vector2D(0, 0), PowerUp.PowerUpType.FAST_RELOAD));
        powerUps.add(new PowerUp(new Vector2D(10, 200), PowerUp.PowerUpType.FAST_ENERGY_GAIN));
        powerUps.add(new PowerUp(new Vector2D(-10, 400), PowerUp.PowerUpType.RED));
        powerUps.add(new PowerUp(new Vector2D(0, 600), PowerUp.PowerUpType.GREEN));

    }

    public void renderStep(double dt) {

        shooter.renderStep(dt);

        missileLauncher.renderStep(dt);

        Iterator<PowerUp> itr = powerUps.iterator();
        while (itr.hasNext()) {
            PowerUp powerUp = itr.next();
            if (powerUp.state == PowerUp.PowerUpState.DEACTIVE) {
                itr.remove();
            } else {
                powerUp.renderStep(dt);
                if (shooter.isCollidingWith(powerUp)) {
                    powerUp.addEffectTo(shooter);
                }
            }
        }

        missileLauncher.addAbilityToEquipPowerUp(powerUps);

        enemyWave.renderStep(dt);
        score += enemyWave.handleCollisionsWithMissiles(missileLauncher.missiles);
        gameOverFlag = enemyWave.isCleared() || (shooter.state == Shooter.ShooterState.DEAD);

    }

    public void draw() {

        shooter.draw();

        enemyWave.draw();

        missileLauncher.draw();

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
                shooter.thrusterLeftMoveStatus = true;
                break;
            case D_KEY:
                shooter.thrusterRightMoveStatus = true;
                break;
            case LEFT_ARROW:
                missileLauncher.turretLeftRotateStatus = true;
                break;
            case RIGHT_ARROW:
                missileLauncher.turretRightRotateStatus = true;
                break;
            case ESC_KEY:
                pauseFlag = true;
                break;
            case Q_KEY:
                quitFlag = true;
                break;
            case UP_ARROW:
                missileLauncher.startCharging();
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
                shooter.thrusterLeftMoveStatus = false;
                break;
            case D_KEY:
                shooter.thrusterRightMoveStatus = false;
                break;
            case LEFT_ARROW:
                missileLauncher.turretLeftRotateStatus = false;
                break;
            case RIGHT_ARROW:
                missileLauncher.turretRightRotateStatus = false;
                break;
            case UP_ARROW:
                missileLauncher.shootMissile();
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