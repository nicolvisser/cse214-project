/**
 * PowerUp
 */
public class PowerUp extends DefaultCritter {

    private static final long serialVersionUID = 1L;

    enum PowerUpType {
        FAST_RELOAD;
    }

    enum PowerUpState {
        TRAVELLING, ACTIVE, DEACTIVE;
    }

    private static final int DEFAULT_LIFETIME = 5;

    PowerUpType type;
    PowerUpState state;
    double remainingLifetime;
    Shooter shooter;
    AnimatedPicture animatedPicture;

    public PowerUp(Vector2D position, PowerUpType type) {
        this.position = position;
        velocity = new Vector2D(0, -200);
        this.type = type;
        state = PowerUpState.TRAVELLING;
        remainingLifetime = DEFAULT_LIFETIME;

        String filename = "";
        switch (type) {
            case FAST_RELOAD:
                filename = "resources/powerUpBlue";
                break;
        }
        animatedPicture = new AnimatedPicture(filename, "png", 6, AnimatedPicture.AnimationType.REPEAT);
    }

    @Override
    public void draw() {
        switch (state) {
            case TRAVELLING:
                animatedPicture.draw(position.x, position.y, 0);
                break;

            case ACTIVE:

                // draw green box around canvas to indicate a powerup is active
                StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
                StdDraw.setPenRadius(0.01);
                int x = (Invaders.CANVAS_XMAX + Invaders.CANVAS_XMIN) / 2;
                int y = (Invaders.CANVAS_YMAX + Invaders.CANVAS_YMIN) / 2;
                double halfWidth = 0.99 * Invaders.CANVAS_WIDTH / 2;
                double halfHeight = 0.99 * Invaders.CANVAS_HEIGHT / 2;
                StdDraw.rectangle(x, y, halfWidth, halfHeight);
                StdDraw.setPenRadius();

                // TODO add countdown bar to show remaining time

                break;

            default:

                break;
        }
    }

    @Override
    public void renderStep(double dt) {
        switch (state) {
            case TRAVELLING:
                super.renderStep(dt);
                break;

            case ACTIVE:
                remainingLifetime -= dt;
                if (remainingLifetime < 0) {
                    deactivateEffect();
                }
                break;

            default:
                break;
        }
    }

    public void addEffectTo(Shooter shooter) {
        this.shooter = shooter;
        shooter.getMissileLauncherReference().reloadTime = MissileLauncher.DEFAULT_RELOAD_TIME / 2;
        state = PowerUpState.ACTIVE;
    }

    public void deactivateEffect() {
        shooter.getMissileLauncherReference().reloadTime = MissileLauncher.DEFAULT_RELOAD_TIME;
        state = PowerUpState.DEACTIVE;
    }

}