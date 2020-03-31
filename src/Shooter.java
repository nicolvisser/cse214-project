/**
 * Shooter
 */
public class Shooter extends DefaultCritter {

    public static final int DEFAULT_HEALTH_POINTS = 300;
    public static final int DEFAULT_ENERGY_POINTS = 100;
    public static final double DEFAULT_ENERGY_GAIN_PER_TIMESTEP = 0.1;

    private static final long serialVersionUID = 1L;
    public static final int DEFAULT_COLLISION_RADIUS = 7;
    private static final int SHIELD_COLLISION_RADIUS = 12;
    private static final int SHIELD_ENERGY_USAGE_INITIAL = 10;
    private static final double SHIELD_ENERGY_USAGE_PER_TIMESTEP = 0.5;
    private static final int MOVEMENT_BOUNDARY_XMIN = -95;
    private static final int MOVEMENT_BOUNDARY_XMAX = 95;
    private static final int THRUSTER_ACCELERATION_MAGNITUDE = 1000;

    public ShooterState state;
    private MissileLauncher missileLauncherRef; // TODO dont only ref, but also move and init inside this class

    public boolean isThrusterLeftActive;
    public boolean isThrusterRightActive;
    private boolean isShieldActive;

    public double energyPoints;
    public double energyGainPerTimeStep;

    private AnimatedPicture explosionAnimation;

    enum ShooterState {
        ALIVE, EXPLODING, DEAD;
    }

    public Shooter(Vector2D position, double orientation) {
        super(position, orientation);
        healthPoints = DEFAULT_HEALTH_POINTS;
        state = ShooterState.ALIVE;
        collisionRadius = DEFAULT_COLLISION_RADIUS;
        energyPoints = DEFAULT_ENERGY_POINTS;
        energyGainPerTimeStep = DEFAULT_ENERGY_GAIN_PER_TIMESTEP;
        isThrusterLeftActive = false;
        isThrusterRightActive = false;
        isShieldActive = false;
        explosionAnimation = new AnimatedPicture("resources/images/explosion", "png", 16,
                AnimatedPicture.AnimationType.FWD_BWD_ONCE);
    }

    @Override
    public void render(double dt) {
        switch (state) {
            case ALIVE:
                if (healthPoints <= 0) {
                    StdAudio.play("resources/audio/Explosion+1.wav");
                    state = ShooterState.EXPLODING;
                    break;
                }

                if (energyPoints <= 0) {
                    deactivateShield();
                }

                // gain energy
                energyPoints = Math.min(energyPoints + energyGainPerTimeStep, DEFAULT_ENERGY_POINTS);

                // drain energy
                if (isShieldActive) {
                    energyPoints -= SHIELD_ENERGY_USAGE_PER_TIMESTEP;
                }

                // determine acceleration from thrusterstatuses
                if (isThrusterLeftActive && !isThrusterRightActive)
                    acceleration = new Vector2D(-THRUSTER_ACCELERATION_MAGNITUDE, 0);
                else if (isThrusterRightActive && !isThrusterLeftActive)
                    acceleration = new Vector2D(+THRUSTER_ACCELERATION_MAGNITUDE, 0);
                else
                    acceleration = Vector2D.zero();

                // if almost no 'thrust' applied or thrust applied in opposite direction than
                // movement, then slow down shooter for fast stopping or turning
                if (velocity.x * acceleration.x < 0.001) {
                    velocity = velocity.scale(0.5);
                }

                // render new position and velocity from kinematic equations
                super.render(dt);

                // keep player in boundaries
                if (position.x > MOVEMENT_BOUNDARY_XMAX) {
                    position.x = MOVEMENT_BOUNDARY_XMAX;
                    velocity.x = 0;
                    acceleration.x = 0;
                } else if (position.x < MOVEMENT_BOUNDARY_XMIN) {
                    position.x = MOVEMENT_BOUNDARY_XMIN;
                    velocity.x = 0;
                    acceleration.x = 0;
                }

                break;

            case EXPLODING:
                if (explosionAnimation.isFinished) {
                    state = ShooterState.DEAD;
                }
                break;

            case DEAD:
                break;
        }

    }

    @Override
    public void draw() {
        switch (state) {
            case ALIVE:
                if (isThrusterLeftActive & !isThrusterRightActive) {
                    StdDraw.picture(position.x, position.y, "resources/images/shooterL.png", 20, 20,
                            getOrientationInDegrees());
                } else if (isThrusterRightActive & !isThrusterLeftActive) {
                    StdDraw.picture(position.x, position.y, "resources/images/shooterR.png", 20, 20,
                            getOrientationInDegrees());
                } else {
                    StdDraw.picture(position.x, position.y, "resources/images/shooter.png", 20, 20,
                            getOrientationInDegrees());
                }

                if (isShieldActive) {
                    StdDraw.picture(position.x, position.y, "resources/images/shield.png", 30, 30, 0);
                }

                break;

            case EXPLODING:
                explosionAnimation.draw(position.x, position.y);
                break;

            case DEAD:
                break;
        }

    }

    @Override
    public void prepareToSaveState() {
        // dont save these statuses:
        // otherwise for example if shield was active at time of save, it remains active
        // after load until deactivated with key release
        isThrusterLeftActive = false;
        isThrusterRightActive = false;
        isShieldActive = false;
    }

    public void addMissileLauncherReference(MissileLauncher missileLauncher) {
        this.missileLauncherRef = missileLauncher;
    }

    public MissileLauncher getMissileLauncherReference() {
        return missileLauncherRef;
    }

    public boolean getShieldState() {
        return isShieldActive;
    }

    public void activateShield() {
        if (!isShieldActive && energyPoints > SHIELD_ENERGY_USAGE_INITIAL) {
            StdAudio.play("resources/audio/shieldUp.wav");
            collisionRadius = SHIELD_COLLISION_RADIUS;
            energyPoints -= SHIELD_ENERGY_USAGE_INITIAL;
            isShieldActive = true;
        }
    }

    public void deactivateShield() {
        if (isShieldActive) {
            StdAudio.play("resources/audio/shieldDown.wav");
            collisionRadius = DEFAULT_COLLISION_RADIUS;
            isShieldActive = false;
        }
    }
}