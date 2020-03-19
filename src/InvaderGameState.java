import java.util.ArrayList;

/**
 * InvaderGameState
 */
public class InvaderGameState {

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

    private final int fps = 60;
    private final int dt_ms = 1000 / fps;
    private final double dt = dt_ms / 1000.0;

    StarField starfield;

    Shooter shooter;

    ArrayList<Enemy> enemies;
    int numEnemies = 0;

    ArrayList<Missile> missiles;
    int numMissiles = 0;
    long timeSinceLastMissile_ms = Missile.RELOAD_TIME_MS; // i.e. ready for next shot from beginning of game

    public InvaderGameState() {

        shooter = new Shooter(new Vector2D(0, 100), Math.PI / 2);

        missiles = new ArrayList<>();

        enemies = new ArrayList<>();

        addEnemy(new Vector2D(-300, 700));
        addEnemy(new Vector2D(-250, 700));
        addEnemy(new Vector2D(-200, 700));
        enemies.get(0).velocity = new Vector2D(0, -50);
        enemies.get(1).velocity = new Vector2D(0, -50);
        enemies.get(2).velocity = new Vector2D(0, -50);

        starfield = new StarField(canvasXmin, canvasXmax, canvasYmin, canvasYmax);

        setupCanvas();
    }

    public void start() {

        while (true) {

            StdDraw.clear();

            starfield.renderStep(dt, shooter.velocity);
            starfield.draw();

            shooter.renderStep(dt);
            shooter.draw();

            for (int i = 0; i < numEnemies; i++) {
                Enemy enemy = enemies.get(i);
                enemy.renderStep(dt);
                if (!enemy.isAlive()) {
                    enemies.remove(enemy);
                    i--;
                    numEnemies--;
                } else {
                    enemy.draw();
                }
            }

            for (int i = 0; i < numMissiles; i++) {
                Missile missile = missiles.get(i);
                missile.renderStep(dt);

                // remove missile if off screen or if 'dead'
                if (!isPointOnCanvas(missile.position) || !missile.isAlive()) {
                    missiles.remove(missile);
                    i--;
                    numMissiles--;
                } else {

                    // detect collision with enemies
                    for (int j = 0; j < numEnemies; j++) {
                        Enemy enemy = enemies.get(j);
                        if (missile.hasCollidedWith(enemy)) {
                            enemy.takeDamage(50);
                            missile.takeDamage(Integer.MAX_VALUE);
                            break;
                        }
                    }

                    missile.draw();
                }
            }
            timeSinceLastMissile_ms += dt_ms;

            StdDraw.show();
            StdDraw.pause(dt_ms);

            listenForInputChanges();
        }

    }

    private void setupCanvas() {
        StdDraw.enableDoubleBuffering();
        StdDraw.clear();
        StdDraw.setCanvasSize(canvasWidth, canvasHeight);
        StdDraw.setXscale(canvasXmin, canvasXmax);
        StdDraw.setYscale(canvasYmin, canvasYmax);
    }

    private void listenForInputChanges() {
        /**
         * for each key in the set of keys used by game, get the key's new state from
         * StdDraw. If the state changed from previous, update the new state and call
         * appropriate function to handle the change event.
         *
         **/
        for (KeyboardKey key : KeyboardKey.values()) {
            boolean keyIsDownInNewFrame = StdDraw.isKeyPressed(key.keyCode);
            if (!key.isDown && keyIsDownInNewFrame) {
                key.isDown = true;
                onKeyPress(key);
            } else if (key.isDown && !keyIsDownInNewFrame) {
                key.isDown = false;
                onKeyRelease(key);
            }
        }
    }

    private void onKeyPress(KeyboardKey key) {
        switch (key) {
            case LEFT:
                shooter.thrusterLeftMoveStatus = true;
                break;
            case RIGHT:
                shooter.thrusterRightMoveStatus = true;
                break;
            case ROTATE_L:
                shooter.turretLeftRotateStatus = true;
                break;
            case ROTATE_R:
                shooter.turretRightRotateStatus = true;
                break;

            default:
                break;
        }
    }

    private void onKeyRelease(KeyboardKey key) {
        switch (key) {
            case LEFT:
                shooter.thrusterLeftMoveStatus = false;
                break;
            case RIGHT:
                shooter.thrusterRightMoveStatus = false;
                break;
            case ROTATE_L:
                shooter.turretLeftRotateStatus = false;
                break;
            case ROTATE_R:
                shooter.turretRightRotateStatus = false;
                break;
            case SHOOT:
                shootMissile(shooter);
                break;

            default:
                break;
        }
    }

    public void shootMissile(Shooter player) {
        if (timeSinceLastMissile_ms > Missile.RELOAD_TIME_MS) {
            numMissiles++;
            timeSinceLastMissile_ms = 0;
            Vector2D missileStartPos = new Vector2D(player.position.x, player.position.y);
            missiles.add(new Missile(missileStartPos, player.FWDVector()));
        }
    }

    public void addEnemy(Vector2D pos) {
        numEnemies++;
        enemies.add(new Enemy(pos, 3 * Math.PI / 2));
    }

    public boolean isPointOnCanvas(Vector2D pos) {
        if ((pos.x >= canvasXmin) && (pos.x <= canvasXmax) && (pos.y >= canvasYmin) && (pos.y <= canvasYmax))
            return true;
        else
            return false;
    }
}