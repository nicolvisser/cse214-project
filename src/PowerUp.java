/**
 * PowerUp
 */
public class PowerUp extends DefaultCritter {

    enum PowerUpType {
        FAST_RELOAD, RED, GREEN, YELLOW;
    }

    enum PowerUpState {
        TRAVELLING, ACTIVE, DEACTIVE;
    }

    private static final long serialVersionUID = 1L;
    private static final int DEFAULT_LIFETIME = 5;
    private static final int DEFAULT_COLLISION_RADIUS = 3;

    public PowerUpState state;

    private PowerUpType type;
    private double remainingLifetime;
    private Shooter shooterOwner;
    private AnimatedPicture animatedPowerUpSprite;

    public PowerUp(Vector2D position, PowerUpType type) {
        this.position = position;
        velocity = new Vector2D(0, -50);
        this.type = type;
        state = PowerUpState.TRAVELLING;
        remainingLifetime = DEFAULT_LIFETIME;
        collisionRadius = DEFAULT_COLLISION_RADIUS;
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
        animatedPowerUpSprite = new AnimatedPicture(filename, "png", 6, AnimatedPicture.AnimationType.REPEAT);
    }

    @Override
    public void draw() {
        switch (state) {
            case TRAVELLING:
                animatedPowerUpSprite.draw(position.x, position.y, 0);
                break;

            case ACTIVE:

                double frame_scale_factor = 1;
                int timer_x_pos = 0;
                String iconFilename = "";

                switch (type) {
                    case FAST_RELOAD:
                        StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
                        frame_scale_factor = 0.995;
                        timer_x_pos = -85;
                        iconFilename = "resources/fastReload.png";
                        break;
                    case RED:
                        StdDraw.setPenColor(StdDraw.RED);
                        frame_scale_factor = 0.99;
                        timer_x_pos = -80;
                        iconFilename = "resources/fastReload.png";
                        break;
                    case GREEN:
                        StdDraw.setPenColor(StdDraw.GREEN);
                        frame_scale_factor = 0.985;
                        timer_x_pos = -75;
                        iconFilename = "resources/fastReload.png";
                        break;
                    case YELLOW:
                        StdDraw.setPenColor(StdDraw.YELLOW);
                        frame_scale_factor = 0.98;
                        timer_x_pos = -70;
                        iconFilename = "resources/fastReload.png";
                        break;
                }

                // draw green box around canvas to indicate a powerup is active
                StdDraw.setPenRadius(0.005);
                StdDraw.rectangle(0, 0, frame_scale_factor * 100, frame_scale_factor * 100);
                StdDraw.setPenRadius();

                // draw timer bar
                double percentageTimeRemaining = Math.max(remainingLifetime / DEFAULT_LIFETIME, 0);
                StdDraw.rectangle(timer_x_pos, -80 + 10, 1, 10);
                StdDraw.filledRectangle(timer_x_pos, -80 + percentageTimeRemaining * 10, 1,
                        percentageTimeRemaining * 10);

                // draw icon above timer bar
                StdDraw.picture(timer_x_pos, -55, iconFilename, 3, 3);

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
        this.shooterOwner = shooter;

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
                shooterOwner.getMissileLauncherReference().reloadTime = MissileLauncher.DEFAULT_RELOAD_TIME;
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