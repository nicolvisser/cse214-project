/**
 * Shooter
 */
public class Shooter extends DefaultCritter {

    enum ShooterState {
        ALIVE, EXPLODING, DEAD;
    }

    public static final int DEFAULT_HEALTH_POINTS = 300;
    public static final int DEFAULT_ENERGY_POINTS = 100;
    public static final int DEFAULT_COLLISION_RADIUS = 7;
    public static final double DEFAULT_ENERGY_GAIN_PER_TIMESTEP = 0.1;
    public static final int SHIELD_COLLISION_RADIUS = 12;
    public static final int SHIELD_ENERGY_USAGE_INITIAL = 10;
    public static final double SHIELD_ENERGY_USAGE_PER_TIMESTEP = 0.5;

    private static final long serialVersionUID = 1L;
    private static final int MOVEMENT_BOUNDARY_XMIN = -95;
    private static final int MOVEMENT_BOUNDARY_XMAX = 95;
    private static final int THRUSTER_ACCELERATION_MAGNITUDE = 1000;

    public boolean thrusterLeftMoveStatus = false;
    public boolean thrusterRightMoveStatus = false;
    public ShooterState state;
    private boolean shieldActive;

    double energyPoints;
    double energyGainPerTimeStep;

    private MissileLauncher missileLauncherRef;
    private AnimatedPicture explosion;

    public Shooter(Vector2D position, double orientation) {
        super(position, orientation);
        healthPoints = DEFAULT_HEALTH_POINTS;
        energyPoints = DEFAULT_ENERGY_POINTS;
        energyGainPerTimeStep = DEFAULT_ENERGY_GAIN_PER_TIMESTEP;
        collisionRadius = DEFAULT_COLLISION_RADIUS;
        state = ShooterState.ALIVE;
        shieldActive = false;
        explosion = new AnimatedPicture("resources/explosion", "png", 16, AnimatedPicture.AnimationType.FWD_BWD_ONCE);
    }

    @Override
    public void renderStep(double dt) {
        switch (state) {
            case ALIVE:
                if (healthPoints <= 0) {
                    StdAudio.play("resources/audio/Explosion+1.wav");
                    state = ShooterState.EXPLODING;
                    break;
                }

                if (energyPoints <= 0) {
                    if (shieldActive) {
                        deactivateShield();
                    }
                }

                energyPoints = Math.min(energyPoints + energyGainPerTimeStep, DEFAULT_ENERGY_POINTS);

                if (shieldActive) {
                    energyPoints -= SHIELD_ENERGY_USAGE_PER_TIMESTEP;
                }

                // determine acceleration from thrusterstatuses
                acceleration = Vector2D.zeroVector();
                if (thrusterLeftMoveStatus && !thrusterRightMoveStatus)
                    acceleration = new Vector2D(-THRUSTER_ACCELERATION_MAGNITUDE, 0);
                if (thrusterRightMoveStatus && !thrusterLeftMoveStatus)
                    acceleration = new Vector2D(+THRUSTER_ACCELERATION_MAGNITUDE, 0);

                // if almost no 'thrust' applied or thrust applied in opposite direction than
                // movement, then slow down shooter for fast stopping or turning
                if (velocity.x * acceleration.x < 0.001) {
                    velocity = Vector2D.scalarMultiplication(0.5, velocity);
                }

                // render new position and velocity from kinematic equations
                super.renderStep(dt);

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
                if (explosion.finished) {
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
                if (thrusterLeftMoveStatus & !thrusterRightMoveStatus) {
                    StdDraw.picture(position.x, position.y, "resources/shooterL.png", 20, 20, orientationInDegrees());
                } else if (thrusterRightMoveStatus & !thrusterLeftMoveStatus) {
                    StdDraw.picture(position.x, position.y, "resources/shooterR.png", 20, 20, orientationInDegrees());
                } else {
                    StdDraw.picture(position.x, position.y, "resources/shooter.png", 20, 20, orientationInDegrees());
                }

                if (shieldActive) {
                    StdDraw.picture(position.x, position.y, "resources/shield.png", 30, 30, 0);
                }

                break;

            case EXPLODING:
                explosion.draw(position.x, position.y, 0);
                break;

            case DEAD:
                break;
        }

    }

    public void addMissileLauncherReference(MissileLauncher missileLauncher) {
        this.missileLauncherRef = missileLauncher;
    }

    public MissileLauncher getMissileLauncherReference() {
        return missileLauncherRef;
    }

    public boolean getShieldState() {
        return shieldActive;
    }

    public void activateShield() {
        if (!shieldActive && energyPoints > SHIELD_ENERGY_USAGE_INITIAL) {
            StdAudio.play("resources/audio/shieldUp.wav");
            collisionRadius = SHIELD_COLLISION_RADIUS;
            energyPoints -= SHIELD_ENERGY_USAGE_INITIAL;
            shieldActive = true;
        }
    }

    public void deactivateShield() {
        if (shieldActive) {
            StdAudio.play("resources/audio/shieldDown.wav");
            collisionRadius = DEFAULT_COLLISION_RADIUS;
            shieldActive = false;
        }
    }
}