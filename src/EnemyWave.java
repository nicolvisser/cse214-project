import java.io.Serializable;
import java.util.ArrayList;

/**
 * EnemyWave
 */
public class EnemyWave implements Serializable {

    private static final long serialVersionUID = 1L;

    EnemyGroup[] enemyGroups;
    Shooter shooterRef;
    double timeUntilNextCounterAttack;
    ArrayList<Missile> enemyMissiles;

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

        for (int i = 0; i < enemyMissiles.size(); i++) {
            Missile missile = enemyMissiles.get(i);

            missile.renderStep(dt);

            if (missile.state == Missile.MissileState.DEAD || !Invaders.isPointOnCanvas(missile.position)) {
                enemyMissiles.remove(i);
                i--;
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
            if (enemyGroup.isAlive()) {
                return false;
            }
        }
        return true;
    }

    public EnemyGroup randomEnemyGroup() {
        if (enemyGroups.length == 0) {
            return null;
        } else {
            int randomIndex = (int) (Math.random() * enemyGroups.length);
            return enemyGroups[randomIndex];
        }
    }

    public Enemy randomEnemyOnCanvas() {
        Enemy rEnemy;
        do {
            EnemyGroup rGroup = randomEnemyGroup();
            if (rGroup == null) {
                return null;
            }
            rEnemy = rGroup.randomEnemy();
            if (rEnemy == null) {
                return null;
            }
        } while (!Invaders.isPointOnCanvas(rEnemy.position));
        return rEnemy;
    }

    public void counterAttack() {
        timeUntilNextCounterAttack = 1;
        Enemy attackingEnemy = randomEnemyOnCanvas();
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