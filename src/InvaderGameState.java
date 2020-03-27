import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * InvaderGameState
 */
public class InvaderGameState extends KeyListener implements Serializable {

    private static final long serialVersionUID = 1L;

    private int canvasXmin;
    private int canvasXmax;
    private int canvasYmin;
    private int canvasYmax;

    public boolean pauseFlag = false;
    public boolean quitFlag = false;
    public boolean gameOverFlag = false;

    public void resetFlags() {
        pauseFlag = false;
        quitFlag = false;
        gameOverFlag = false;
    }

    int score;

    Background background;

    Shooter shooter;

    MissileLauncher missileLauncher;

    EnemyWave enemyWave;

    ArrayList<PowerUp> powerUps;

    public InvaderGameState(int xmin, int xmax, int ymin, int ymax) {
        canvasXmin = xmin;
        canvasXmax = xmax;
        canvasYmin = ymin;
        canvasYmax = ymax;

        score = 0;

        background = new Background(canvasXmin, canvasXmax, canvasYmin, canvasYmax);

        shooter = new Shooter(new Vector2D(0, 100), Math.PI / 2);

        missileLauncher = new MissileLauncher(shooter);

        enemyWave = new EnemyWave(shooter);

        powerUps = new ArrayList<>();
        powerUps.add(new PowerUp(new Vector2D(0, 400), PowerUp.PowerUpType.FAST_RELOAD));
        powerUps.add(new PowerUp(new Vector2D(-200, 800), PowerUp.PowerUpType.GREEN));
        powerUps.add(new PowerUp(new Vector2D(200, 1200), PowerUp.PowerUpType.RED));
        powerUps.add(new PowerUp(new Vector2D(100, 1600), PowerUp.PowerUpType.YELLOW));

    }

    public void renderStep(double dt) {

        background.renderStep(dt, shooter.velocity);

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

        background.draw();

        shooter.draw();

        enemyWave.draw();

        missileLauncher.draw();

        for (PowerUp powerUp : powerUps) {
            powerUp.draw();
        }

        drawHealthBar(Math.max(0, (double) shooter.healthPoints / Shooter.DEFAULT_HEALTH_POINTS));
        drawEnergyBar(1.0);
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

            default:
                break;
        }
    }

    public void drawHealthBar(double percentage) {
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.rectangle(-300, 50, 50, 15);
        double halfWidth = Math.max(0, (50 * percentage) - 2);
        StdDraw.filledRectangle(-350 + 50 * percentage, 50, halfWidth, 15 - 2);
    }

    public void drawEnergyBar(double percentage) {
        StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
        StdDraw.rectangle(-150, 50, 50, 15);
        double halfWidth = Math.max(0, (50 * percentage) - 2);
        StdDraw.filledRectangle(-200 + 50 * percentage, 50, halfWidth, 15 - 2);
    }

    public void drawScore(int score) {
        StdDraw.setPenColor(StdDraw.ORANGE);
        StdDraw.text(200, 50, "SCORE: " + score);
    }

}