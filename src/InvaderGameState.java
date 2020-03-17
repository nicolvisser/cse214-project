import java.util.ArrayList;

/**
 * InvaderGameState
 */
public class InvaderGameState {

    /**
     * Enumeration to define different values of game state.
     */
    enum GameState {
        PLAYING, GAME_OVER
    }

    enum KeyboardKey {
        LEFT(65), RIGHT(68), ROTATE_L(37), ROTATE_R(39), SHOOT(38);

        public int keyCode;
        public boolean isDown;

        private KeyboardKey(int value) {
            this.keyCode = value;
            this.isDown = false;
        }

        public void press() {
            this.isDown = true;
        }

        public void release() {
            this.isDown = false;
        }
    }

    /**
     * Variable to hold different values of game state.
     */
    private GameState gameState;

    private boolean useMouseControl = false;

    private final int canvasWidth = 800;
    private final int canvasHeight = 800;
    private final int canvasXmin = -canvasWidth / 2;
    private final int canvasXmax = canvasWidth / 2;
    private final int canvasYmin = 0;
    private final int canvasYmax = canvasHeight;

    private final int fps = 60;
    private final int dt_ms = 1000 / fps;
    private final double dt = dt_ms / 1000.0;

    Shooter shooter;

    Enemy enemy;

    StarField starfield;

    ArrayList<Missile> missiles;
    int numMissiles = 0;
    long timeSinceLastMissile_ms = Missile.RELOAD_TIME_MS; // i.e. ready for next shot from beginning of game

    public InvaderGameState() {
        gameState = GameState.PLAYING;

        shooter = new Shooter(new Vector2D(0, 100), Math.PI / 2);

        enemy = new Enemy(new Vector2D(0, 700), 3 * Math.PI / 2);

        missiles = new ArrayList<>();

        starfield = new StarField(canvasXmin, canvasXmax, canvasYmin, canvasYmax);

        setupCanvas();
    }

    public void start() {

        while (gameState == GameState.PLAYING) {

            StdDraw.clear();

            starfield.renderStep(dt, shooter.velocity);
            starfield.draw();

            shooter.renderStep(dt);
            shooter.draw();

            enemy.renderStep(dt);
            enemy.draw();

            if (useMouseControl)
                shooter.lookAt(StdDraw.mouseX(), StdDraw.mouseY());

            for (int i = 0; i < numMissiles; i++) {
                Missile missile = missiles.get(i);
                missile.renderStep(dt);

                // remove if off screen
                if (!isPointOnCanvas(missile.position)) {
                    missiles.remove(missile);
                    i--;
                    numMissiles--;
                }

                // detect collision with enemy
                if (missile.hasCollidedWith(enemy)) {
                    StdOut.println("COLLIDED WITH ENEMY");
                }

                missile.draw();
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

        if (useMouseControl)
            if (StdDraw.isMousePressed())
                shootMissile(shooter);
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

    public boolean isPointOnCanvas(Vector2D pos) {
        if ((pos.x >= canvasXmin) && (pos.x <= canvasXmax) && (pos.y >= canvasYmin) && (pos.y <= canvasYmax))
            return true;
        else
            return false;
    }
}