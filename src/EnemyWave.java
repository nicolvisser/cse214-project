import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * EnemyWave
 */
public class EnemyWave implements Serializable {

    private static final long serialVersionUID = 1L;

    private RectangleDimension canvas;
    private EnemyGroup[] enemyGroups;
    private Shooter shooterRef;
    private double timeUntilNextCounterAttack;
    private ArrayList<Missile> enemyMissiles;

    public EnemyWave(RectangleDimension canvas, Shooter shooterRef) {
        this.canvas = canvas;

        enemyGroups = new EnemyGroup[2];

        enemyGroups[0] = new EnemyGroup(canvas);
        enemyGroups[0].populateInSquareFormation(new Vector2D(-75, 75), 4);
        enemyGroups[0].velocity = new Vector2D(0, -20);

        enemyGroups[1] = new EnemyGroup(canvas);
        enemyGroups[1].populateInCircleFormation(new Vector2D(0, 225), 16, 50);
        enemyGroups[1].velocity = new Vector2D(0, -20);

        this.shooterRef = shooterRef;
        timeUntilNextCounterAttack = 1;
        enemyMissiles = new ArrayList<>();
    }

    public void renderStep(double dt) {
        timeUntilNextCounterAttack -= dt;

        if (checkGameOverConditions(shooterRef)) {
            shooterRef.takeDamage(Integer.MAX_VALUE);
        }

        if (timeUntilNextCounterAttack < 0) {
            /*
             * if at least one enemy on canvas
             * 
             * 
             * 
             */
            counterAttack();
        }

        for (EnemyGroup enemyGroup : enemyGroups) {
            enemyGroup.renderStep(dt);
        }

        Iterator<Missile> enemyMissileIterator = enemyMissiles.iterator();
        while (enemyMissileIterator.hasNext()) {
            Missile enemyMissile = enemyMissileIterator.next();
            enemyMissile.renderStep(dt);
            if (enemyMissile.state == Missile.MissileState.DEAD || !canvas.doesContainPoint(enemyMissile.position)) {
                enemyMissileIterator.remove();
            } else if (enemyMissile.state == Missile.MissileState.TRAVELLING
                    && enemyMissile.isCollidingWith(shooterRef)) {
                if (shooterRef.getShieldState() == false) {
                    shooterRef.takeDamage(enemyMissile.missileDamage);
                } else {
                    StdAudio.play("resources/audio/shieldUp.wav");
                }
                enemyMissile.takeDamage(Integer.MAX_VALUE);
            } else if (enemyMissile.state == Missile.MissileState.TRAVELLING) {

                Iterator<Missile> shooterMissileIterator = shooterRef.getMissileLauncherReference().missiles.iterator();

                while (shooterMissileIterator.hasNext()) {
                    Missile shooterMissile = shooterMissileIterator.next();
                    if (shooterMissile.state == Missile.MissileState.TRAVELLING
                            && enemyMissile.isCollidingWith(shooterMissile)) {
                        shooterMissile.takeDamage(Integer.MAX_VALUE);
                        enemyMissile.takeDamage(Integer.MAX_VALUE);
                    }
                }

            }
        }
    }

    public void draw() {
        for (EnemyGroup enemyGroup : enemyGroups) {
            enemyGroup.draw();
        }
        for (Missile missile : enemyMissiles) {
            missile.draw();
        }
    }

    public int handleCollisionsWithMissiles(ArrayList<Missile> missiles) {
        int points = 0;
        for (EnemyGroup enemyGroup : enemyGroups) {
            points += enemyGroup.handleCollisionsWithMissiles(missiles);
        }
        return points;
    }

    public boolean checkGameOverConditions(Shooter shooter) {
        for (EnemyGroup enemyGroup : enemyGroups) {
            if (enemyGroup.isTouchingBottomOrShooter(shooter)) {
                return true;
            }
        }
        return false;
    }

    public boolean isCleared() {
        for (EnemyGroup enemyGroup : enemyGroups) {
            if (enemyGroup.hasEnemies()) {
                return false;
            }
        }
        return true;
    }

    public boolean atLeastOneAliveEnemyOnCanvas() {
        for (EnemyGroup enemyGroup : enemyGroups) {
            for (Enemy enemy : enemyGroup.enemies) {
                if (canvas.doesContainPoint(enemy.position) && enemy.state == Enemy.EnemyState.ALIVE) {
                    return true;
                }
            }
        }
        return false;
    }

    public EnemyGroup getRandomEnemyGroup() {
        if (enemyGroups.length == 0) {
            return null;
        } else {
            int randomIndex = (int) (Math.random() * enemyGroups.length);
            return enemyGroups[randomIndex];
        }
    }

    // Todo: make this more readable
    public Enemy getRandomEnemyOnCanvas() {
        if (atLeastOneAliveEnemyOnCanvas()) {
            Enemy rEnemy;
            do {
                EnemyGroup rGroup = getRandomEnemyGroup();
                if (rGroup == null) {
                    rEnemy = null;
                } else {
                    rEnemy = rGroup.getRandomEnemy();
                }
            } while (rEnemy == null || !canvas.doesContainPoint(rEnemy.position)
                    || rEnemy.state != Enemy.EnemyState.ALIVE);
            return rEnemy;
        } else {
            return null;
        }
    }

    public void counterAttack() {
        timeUntilNextCounterAttack = 1;
        Enemy attackingEnemy = getRandomEnemyOnCanvas();
        if (attackingEnemy != null) {
            Vector2D missileSpawnLocation = new Vector2D(attackingEnemy.position.x, attackingEnemy.position.y);
            Vector2D shooterPositionRelativeToEnemy = Object2D.relativePositionVector(attackingEnemy, shooterRef);
            Missile missile = new Missile(missileSpawnLocation, shooterPositionRelativeToEnemy.normalize());
            StdDraw.filledCircle(missileSpawnLocation.x, missileSpawnLocation.y, 5);
            missile.velocity = shooterPositionRelativeToEnemy.normalize().scale(Missile.SPEED);
            enemyMissiles.add(missile);
            StdAudio.play("resources/audio/Gun+1.wav");
        }
    }

}