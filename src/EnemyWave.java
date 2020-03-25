import java.io.Serializable;
import java.util.ArrayList;

/**
 * EnemyWave
 */
public class EnemyWave implements Serializable {

    private static final long serialVersionUID = 1L;

    EnemyGroup[] enemyGroups;

    public EnemyWave() {
        enemyGroups = new EnemyGroup[2];

        enemyGroups[0] = new EnemyGroup();
        enemyGroups[0].populateInSquareFormation(new Vector2D(-300, 700), 4);
        enemyGroups[0].velocity = new Vector2D(0, -80);

        enemyGroups[1] = new EnemyGroup();
        enemyGroups[1].populateInCircleFormation(new Vector2D(0, 1300), 16, 200);
        enemyGroups[1].velocity = new Vector2D(0, -80);
    }

    public void renderStep(double dt) {
        for (EnemyGroup enemyGroup : enemyGroups) {
            enemyGroup.renderStep(dt);
        }
    }

    public void draw() {
        for (EnemyGroup enemyGroup : enemyGroups) {
            enemyGroup.draw();
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

}