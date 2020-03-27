import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * InvaderGameState
 */
public class InvaderGameState extends KeyListener implements Serializable {

    private static final long serialVersionUID = 1L;

    /*
     * // Todo see how we can use this variable rather than directly accessing
     * Invaders.canvas which is bad practice
     */
    private final RectangleDimension canvas;

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

    public InvaderGameState(RectangleDimension canvas) {
        this.canvas = canvas;

        score = 0;

        background = new Background(canvas);

        shooter = new Shooter(new Vector2D(0, -75), Math.PI / 2);

        missileLauncher = new MissileLauncher(canvas, shooter);

        enemyWave = new EnemyWave(canvas, shooter);

        powerUps = new ArrayList<>();
        powerUps.add(new PowerUp(new Vector2D(0, 0), PowerUp.PowerUpType.FAST_RELOAD));
        powerUps.add(new PowerUp(new Vector2D(0, 100), PowerUp.PowerUpType.GREEN));
        powerUps.add(new PowerUp(new Vector2D(0, 200), PowerUp.PowerUpType.RED));
        powerUps.add(new PowerUp(new Vector2D(0, 300), PowerUp.PowerUpType.YELLOW));

    }

    // change values of canvas dimensions, e.g. for on change of game resolution
    public void setCanvasDimension(RectangleDimension otherCanvas) {
        this.canvas.setFrom(otherCanvas);
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
        StdDraw.rectangle(-87.5 + 12.5, -87.5, 12.5, 3.75);
        double halfWidth = Math.max(0, (12.5 * percentage));
        StdDraw.filledRectangle(-87.5 + 12.5 * percentage, -87.5, halfWidth, 3.75);
    }

    public void drawEnergyBar(double percentage) {
        StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
        StdDraw.rectangle(-50 + 12.5, -87.5, 12.5, 3.75);
        double halfWidth = Math.max(0, (12.5 * percentage));
        StdDraw.filledRectangle(-50 + 12.5 * percentage, -87.5, halfWidth, 3.75);
    }

    public void drawScore(int score) {
        StdDraw.setPenColor(StdDraw.ORANGE);
        StdDraw.text(50, -87.5, "SCORE: " + score);
    }

}