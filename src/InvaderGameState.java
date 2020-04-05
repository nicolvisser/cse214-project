import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.Color;

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

    CollisionHandler collisionHandler;
    SceneHandler sceneHandler;

    int score;

    private final ArrayList<Shooter> shooters;
    private final EnemyWave enemyWave;
    private final ArrayList<PowerUp> powerUps;
    private final ArrayList<Bunker> bunkers;

    private final Ray groundRay;

    public InvaderGameState(Rectangle canvas) {

        score = 0;

        groundRay = new Ray(new Vector2D(canvas.xmin(), canvas.ymin()), new Vector2D(1, 0));

        shooters = new ArrayList<>();
        Shooter shooter = new Shooter(new Vector2D(0, -75), Math.PI / 2, canvas);
        shooter.allowRotation = false;
        shooters.add(shooter);

        enemyWave = new EnemyWave(canvas, shooter);

        powerUps = new ArrayList<>();
        powerUps.add(new PowerUp(new Vector2D(0, 0), PowerUp.PowerUpType.FAST_RELOAD));
        powerUps.add(new PowerUp(new Vector2D(10, 200), PowerUp.PowerUpType.FAST_ENERGY_GAIN));
        powerUps.add(new PowerUp(new Vector2D(-10, 400), PowerUp.PowerUpType.RED));
        powerUps.add(new PowerUp(new Vector2D(0, 600), PowerUp.PowerUpType.GREEN));

        bunkers = new ArrayList<>();
        bunkers.add(new Bunker(new Rectangle(-20, -30, 30, 10), 5, 15));
        bunkers.add(new Bunker(new Rectangle(-60, -30, 30, 10), 5, 15));
        bunkers.add(new Bunker(new Rectangle(20, -30, 30, 10), 5, 15));
        bunkers.add(new Bunker(new Rectangle(60, -30, 30, 10), 5, 15));

        sceneHandler = new SceneHandler();
        sceneHandler.add(shooters);
        sceneHandler.add(enemyWave);
        sceneHandler.add(powerUps);
        sceneHandler.add(bunkers);

        collisionHandler = new CollisionHandler();
        collisionHandler.add(shooters, powerUps);
        collisionHandler.add(shooters, enemyWave.enemyMissiles);
        collisionHandler.add(shooter.getTurret().missiles, enemyWave.enemyMissiles); // TODO: improve
        collisionHandler.add(shooter.getTurret().missiles, powerUps); // TODO: improve
        collisionHandler.add(shooter.getTurret().missiles, bunkers); // TODO: improve
        collisionHandler.add(enemyWave.enemyMissiles, bunkers);
        collisionHandler.add(enemyWave, shooter.getTurret().missiles);
        collisionHandler.add(enemyWave, shooter);
        collisionHandler.add(enemyWave, bunkers);

    }

    public void render(double dt) {

        collisionHandler.handleCollisions();

        sceneHandler.render(dt);

        for (Shooter shooter : shooters) {
            if (shooter.state == Shooter.ShooterState.DEAD) {
                gameOverFlag = true;
            }
        }

        if (enemyWave.isCleared() || enemyWave.isCollidingWith(groundRay))
            gameOverFlag = true;

        Iterator<PowerUp> powerUpIterator = powerUps.iterator();
        while (powerUpIterator.hasNext()) {
            PowerUp powerUp = powerUpIterator.next();
            if (powerUp.state == PowerUp.PowerUpState.DEACTIVE) {
                powerUpIterator.remove();
            }
        }

        Iterator<Bunker> bunkerIterator = bunkers.iterator();
        while (bunkerIterator.hasNext()) {
            Bunker bunker = bunkerIterator.next();

            if (bunker.isCleared()) {
                bunkerIterator.remove();
            }
        }
    }

    public void draw() {

        sceneHandler.draw();

        for (Shooter shooter : shooters) {
            shooter.getTurret().drawAimLine(bunkers, enemyWave);
        }

        if (shooters.size() == 1) {
            drawHealthBar((double) shooters.get(0).healthPoints / Shooter.DEFAULT_HEALTH_POINTS);
            drawEnergyBar(shooters.get(0).energyPoints / Shooter.DEFAULT_ENERGY_POINTS);
        }

        drawScore(score);

    }

    @Override
    public void onKeyPress(KeyListener.KeyboardKey key) {
        switch (key) {
            case A_KEY:
                shooters.get(0).isThrusterLeftActive = true;
                break;
            case D_KEY:
                shooters.get(0).isThrusterRightActive = true;
                break;
            case LEFT_ARROW:
                shooters.get(0).getTurret().turretLeftRotateStatus = true;
                break;
            case RIGHT_ARROW:
                shooters.get(0).getTurret().turretRightRotateStatus = true;
                break;
            case ESC_KEY:
                pauseFlag = true;
                break;
            case Q_KEY:
                quitFlag = true;
                break;
            case UP_ARROW:
                shooters.get(0).getTurret().startCharging();
                break;
            case DOWN_ARROW:
                shooters.get(0).activateShield();
                break;
            case SPACE:
                shooters.get(0).getTurret().activateLaser();
                break;

            default:
                break;
        }
    }

    @Override
    public void onKeyRelease(KeyListener.KeyboardKey key) {
        switch (key) {
            case A_KEY:
                shooters.get(0).isThrusterLeftActive = false;
                break;
            case D_KEY:
                shooters.get(0).isThrusterRightActive = false;
                break;
            case LEFT_ARROW:
                shooters.get(0).getTurret().turretLeftRotateStatus = false;
                break;
            case RIGHT_ARROW:
                shooters.get(0).getTurret().turretRightRotateStatus = false;
                break;
            case UP_ARROW:
                shooters.get(0).getTurret().shootMissile();
                break;
            case DOWN_ARROW:
                shooters.get(0).deactivateShield();
                break;
            case SPACE:
                shooters.get(0).getTurret().deactivateLaser();
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

        percentage = Math.min(percentage, 1); // dont overdraw higher values
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
        if (shooters.size() > 0) {
            return shooters.get(0).velocity;
        } else {
            return new Vector2D(0, 0);
        }
    }
}