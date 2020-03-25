import java.io.Serializable;

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

    public InvaderGameState(int xmin, int xmax, int ymin, int ymax) {
        canvasXmin = xmin;
        canvasXmax = xmax;
        canvasYmin = ymin;
        canvasYmax = ymax;

        score = 0;

        background = new Background(canvasXmin, canvasXmax, canvasYmin, canvasYmax);

        shooter = new Shooter(new Vector2D(0, 100), Math.PI / 2);

        missileLauncher = new MissileLauncher(shooter);

        enemyWave = new EnemyWave();
    }

    public void renderStep(double dt) {

        background.renderStep(dt, shooter.velocity);

        shooter.renderStep(dt);

        missileLauncher.renderStep(dt);

        enemyWave.renderStep(dt);
        score += enemyWave.handleCollisionsWithMissiles(missileLauncher.missiles);
        gameOverFlag = enemyWave.checkGameOverConditions(shooter) || enemyWave.isCleared();

    }

    public void draw() {

        background.draw();

        shooter.draw();

        enemyWave.draw();

        missileLauncher.draw();

        drawHealthBar(shooter.healthPoints);
        drawEnergyBar(50);
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
        StdDraw.filledRectangle(-300, 50, (50 - 2) * percentage / 100, 15 - 2);
    }

    public void drawEnergyBar(double percentage) {
        StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
        StdDraw.rectangle(-150, 50, 50, 15);
        StdDraw.filledRectangle(-150, 50, (50 - 2) * percentage / 100, 15 - 2);
    }

    public void drawScore(int score) {
        StdDraw.setPenColor(StdDraw.ORANGE);
        StdDraw.text(200, 50, "SCORE: " + score);
    }

}