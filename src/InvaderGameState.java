import java.io.Serializable;
import java.util.ArrayList;

/**
 * InvaderGameState
 */
public class InvaderGameState extends KeyListener implements Serializable {

    private static final long serialVersionUID = 1L;

    enum KeyboardKey {
        LEFT(65), RIGHT(68), ROTATE_L(37), ROTATE_R(39), SHOOT(38);

        public int keyCode;
        public boolean isDown;

        private KeyboardKey(int value) {
            this.keyCode = value;
            this.isDown = false;
        }
    }

    private final int canvasWidth = 800;
    private final int canvasHeight = 800;
    private final int canvasXmin = -canvasWidth / 2;
    private final int canvasXmax = canvasWidth / 2;
    private final int canvasYmin = 0;
    private final int canvasYmax = canvasHeight;

    public boolean pauseFlag = false;

    // private final int fps = 60;
    // private final int dt_ms = 1000 / fps;
    // private final double dt = dt_ms / 1000.0;

    Background background;

    int score;

    Shooter shooter;

    ArrayList<Enemy> enemyIndividuals;
    int numEnemies = 0;

    EnemyGrid enemyGrid;

    ArrayList<Missile> missiles;
    int numMissiles = 0;
    double timeSinceLastMissile = Missile.RELOAD_TIME; // TODO: Sort out overflow

    public InvaderGameState() {

        score = 0;

        shooter = new Shooter(new Vector2D(0, 100), Math.PI / 2);

        missiles = new ArrayList<>();

        enemyGrid = new EnemyGrid(0, 600, 4, 3);

        background = new Background(canvasXmin, canvasXmax, canvasYmin, canvasYmax);

    }

    public void renderStep(double dt) {

        background.renderStep(dt, shooter.velocity);

        shooter.renderStep(dt);

        enemyGrid.renderStep(dt);
        enemyGrid.handleCollisionsWithMissiles(missiles);

        for (int i = 0; i < numMissiles; i++) {
            Missile missile = missiles.get(i);
            missile.renderStep(dt);

            // remove missile if off screen or if 'dead'
            if (!isPointOnCanvas(missile.position) || !missile.isAlive()) {
                missiles.remove(missile);
                i--;
                numMissiles--;
            }
        }
        timeSinceLastMissile += dt;
    }

    public void draw() {

        background.draw();

        shooter.draw();

        enemyGrid.draw();

        for (Missile missile : missiles) {
            missile.draw();
        }

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
                shooter.turretLeftRotateStatus = true;
                break;
            case RIGHT_ARROW:
                shooter.turretRightRotateStatus = true;
                break;
            case ESC_KEY:
                pauseFlag = true;
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
                shooter.turretLeftRotateStatus = false;
                break;
            case RIGHT_ARROW:
                shooter.turretRightRotateStatus = false;
                break;
            case UP_ARROW:
                shootMissile(shooter);
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

    public void shootMissile(Shooter player) {
        if (timeSinceLastMissile > Missile.RELOAD_TIME) {
            numMissiles++;
            timeSinceLastMissile = 0;
            Vector2D missileStartPos = new Vector2D(player.position.x, player.position.y);
            missiles.add(new Missile(missileStartPos, player.FWDVector()));
        }
    }

    public void addEnemy(Vector2D pos) {
        numEnemies++;
        enemyIndividuals.add(new Enemy(pos, 3 * Math.PI / 2));
    }

    public boolean isPointOnCanvas(Vector2D pos) {
        if ((pos.x >= canvasXmin) && (pos.x <= canvasXmax) && (pos.y >= canvasYmin) && (pos.y <= canvasYmax))
            return true;
        else
            return false;
    }
}