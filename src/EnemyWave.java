import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * EnemyWave
 */
public class EnemyWave implements Serializable {

    private static final long serialVersionUID = 1L;

    private EnemyGroup[] enemyGroups;
    private Shooter shooterRef;
    private double timeUntilNextCounterAttack;
    private ArrayList<Missile> enemyMissiles;

    public EnemyWave(Shooter shooterRef) {
        enemyGroups = new EnemyGroup[2];

        enemyGroups[0] = new EnemyGroup();
        enemyGroups[0].populateInSquareFormation(new Vector2D(-300, 700), 4);
        enemyGroups[0].velocity = new Vector2D(0, -80);

        enemyGroups[1] = new EnemyGroup();
        enemyGroups[1].populateInCircleFormation(new Vector2D(0, 1300), 16, 200);
        enemyGroups[1].velocity = new Vector2D(0, -80);

        this.shooterRef = shooterRef;
        timeUntilNextCounterAttack = 1;
        enemyMissiles = new ArrayList<>();
    }

    public void renderStep(double dt) {
        timeUntilNextCounterAttack -= dt;

        if (timeUntilNextCounterAttack < 0) {
            counterAttack();
        }

        for (EnemyGroup enemyGroup : enemyGroups) {
            enemyGroup.renderStep(dt);
        }

        Iterator<Missile> itr = enemyMissiles.iterator();
        while (itr.hasNext()) {
            Missile missile = itr.next();
            missile.renderStep(dt);
            if (missile.state == Missile.MissileState.DEAD || !Invaders.isPointOnCanvas(missile.position)) {
                itr.remove();
            } else if (missile.state == Missile.MissileState.TRAVELLING && missile.isCollidingWith(shooterRef)) {
                shooterRef.takeDamage(missile.missileDamage);
                missile.takeDamage(Integer.MAX_VALUE);
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

    public EnemyGroup getRandomEnemyGroup() {
        if (enemyGroups.length == 0) {
            return null;
        } else {
            int randomIndex = (int) (Math.random() * enemyGroups.length);
            return enemyGroups[randomIndex];
        }
    }

    public Enemy getRandomEnemyOnCanvas() {
        Enemy rEnemy;
        do {
            EnemyGroup rGroup = getRandomEnemyGroup();
            if (rGroup == null) {
                return null;
            }
            rEnemy = rGroup.getRandomEnemy();
            if (rEnemy == null) {
                return null;
            }
        } while (!Invaders.isPointOnCanvas(rEnemy.position));
        return rEnemy;
    }

    public void counterAttack() {
        timeUntilNextCounterAttack = 1;
        Enemy attackingEnemy = getRandomEnemyOnCanvas();
        if (attackingEnemy != null) {
            Vector2D missileSpawnLocation = new Vector2D(attackingEnemy.position.x, attackingEnemy.position.y);
            Vector2D shooterPositionRelativeToEnemy = Object2D.relativePositionVector(attackingEnemy, shooterRef);
            Missile missile = new Missile(missileSpawnLocation, shooterPositionRelativeToEnemy.unitVector());
            StdDraw.filledCircle(missileSpawnLocation.x, missileSpawnLocation.y, 30);
            missile.velocity = Vector2D.scalarMultiplication(Missile.SPEED,
                    shooterPositionRelativeToEnemy.unitVector());
            enemyMissiles.add(missile);
            StdAudio.play("resources/audio/Gun+1.wav");
        }
    }

}