import java.io.Serializable;
import java.util.ArrayList;

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

    public void resetFlags() {
        pauseFlag = false;
        quitFlag = false;
    }

    int score;

    Background background;

    Shooter shooter;

    ArrayList<Missile> missiles;
    int numMissiles = 0;
    double timeSinceLastMissile = Missile.RELOAD_TIME; // should not have overflow problems, since game will end soon
                                                       // enough if you don't shoot missiles often

    EnemyGroup enemyGroup;

    public InvaderGameState(int xmin, int xmax, int ymin, int ymax) {
        canvasXmin = xmin;
        canvasXmax = xmax;
        canvasYmin = ymin;
        canvasYmax = ymax;

        score = 0;

        background = new Background(canvasXmin, canvasXmax, canvasYmin, canvasYmax);

        shooter = new Shooter(new Vector2D(0, 100), Math.PI / 2);

        enemyGroup = new EnemyGroup(new Vector2D(-300, 700), EnemyGroup.Formation.SQUARE, 16);
        enemyGroup.velocity = new Vector2D(0, -50);

        missiles = new ArrayList<>();
    }

    public void renderStep(double dt) {

        background.renderStep(dt, shooter.velocity);

        shooter.renderStep(dt);

        enemyGroup.renderStep(dt);
        score += enemyGroup.handleCollisionsWithMissiles(missiles);

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

        enemyGroup.draw();

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
            case Q_KEY:
                quitFlag = true;
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

    public boolean isPointOnCanvas(Vector2D pos) {
        if ((pos.x >= canvasXmin) && (pos.x <= canvasXmax) && (pos.y >= canvasYmin) && (pos.y <= canvasYmax))
            return true;
        else
            return false;
    }

}