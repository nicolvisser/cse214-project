/**
 * PowerUp
 */
public class PowerUp extends DefaultCritter {

    private static final long serialVersionUID = 1L;

    enum PowerUpType {
        FAST_RELOAD, RED, GREEN, YELLOW;
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
            case RED:
                filename = "resources/powerUpRed";
                break;
            case GREEN:
                filename = "resources/powerUpGreen";
                break;
            case YELLOW:
                filename = "resources/powerUpYellow";
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

                double frame_scale_factor = 1;
                int timer_x_pos = 0;
                String iconFilename = "";

                switch (type) {
                    case FAST_RELOAD:
                        StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
                        frame_scale_factor = 0.995;
                        timer_x_pos = -340;
                        iconFilename = "resources/fastReload.png";
                        break;
                    case RED:
                        StdDraw.setPenColor(StdDraw.RED);
                        frame_scale_factor = 0.99;
                        timer_x_pos = -320;
                        iconFilename = "resources/fastReload.png";
                        break;
                    case GREEN:
                        StdDraw.setPenColor(StdDraw.GREEN);
                        frame_scale_factor = 0.985;
                        timer_x_pos = -300;
                        iconFilename = "resources/fastReload.png";
                        break;
                    case YELLOW:
                        StdDraw.setPenColor(StdDraw.YELLOW);
                        frame_scale_factor = 0.98;
                        timer_x_pos = -280;
                        iconFilename = "resources/fastReload.png";
                        break;
                }

                // draw green box around canvas to indicate a powerup is active
                StdDraw.setPenRadius(0.005);
                int x = (Invaders.CANVAS_XMAX + Invaders.CANVAS_XMIN) / 2;
                int y = (Invaders.CANVAS_YMAX + Invaders.CANVAS_YMIN) / 2;
                double halfWidth = frame_scale_factor * Invaders.CANVAS_WIDTH / 2;
                double halfHeight = frame_scale_factor * Invaders.CANVAS_HEIGHT / 2;
                StdDraw.rectangle(x, y, halfWidth, halfHeight);
                StdDraw.setPenRadius();

                // draw timer bar
                double percentageTimeRemaining = Math.max(remainingLifetime / DEFAULT_LIFETIME, 0);
                StdDraw.rectangle(timer_x_pos, 150, 5, 50);
                StdDraw.filledRectangle(timer_x_pos, 100 + percentageTimeRemaining * 50, 5,
                        percentageTimeRemaining * 50);

                // draw icon above timer bar
                StdDraw.picture(timer_x_pos, 215, iconFilename, 10, 10);

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

        switch (type) {
            case FAST_RELOAD:
                shooter.getMissileLauncherReference().reloadTime = MissileLauncher.DEFAULT_RELOAD_TIME / 2;
                break;
            case RED:
                break;
            case GREEN:
                break;
            case YELLOW:
                break;
        }

        state = PowerUpState.ACTIVE;
    }

    public void deactivateEffect() {

        switch (type) {
            case FAST_RELOAD:
                shooter.getMissileLauncherReference().reloadTime = MissileLauncher.DEFAULT_RELOAD_TIME;
                break;
            case RED:
                break;
            case GREEN:
                break;
            case YELLOW:
                break;
        }

        state = PowerUpState.DEACTIVE;
    }

}