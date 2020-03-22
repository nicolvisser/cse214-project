import java.util.ArrayList;

public class EnemyGroup extends Enemy {

    private static final long serialVersionUID = 1L;

    ArrayList<Enemy> enemies;

    public EnemyGroup() {
        super();
        enemies = new ArrayList<>();
    }

    public void add(Enemy enemy) {
        enemies.add(enemy);
    }

    public void remove(Enemy enemy) {
        enemies.remove(enemy);
    }

    @Override
    public boolean isAlive() {
        return enemies.size() > 0;
    }

    @Override
    public void draw() {
        for (Enemy enemy : enemies) {
            enemy.draw(position);
        }
    }

    @Override
    public void renderStep(double dt) {

        for (int i = 0; i < enemies.size(); i++) {
            Enemy enemy = enemies.get(i);
            enemy.renderStep(dt);

            // remove enemy if no longer alive
            if (!enemy.isAlive()) {
                enemies.remove(enemy);
                i--;
            }
        }

        super.renderStep(dt);
    }

    @Override
    public int handleCollisionsWithMissiles(ArrayList<Missile> missiles) {
        int points = 0;

        for (Enemy enemy : enemies) {
            for (Missile missile : missiles) {

                Vector2D enemyGlobalPosition = Vector2D.sum(position, enemy.position);

                Vector2D relativePositionVector = new Vector2D(enemyGlobalPosition.x - missile.position.x,
                        enemyGlobalPosition.y - missile.position.y);

                Double distanceBetween = relativePositionVector.magnitude();

                if (distanceBetween <= enemy.collisionRadius + missile.collisionRadius) {
                    points += missile.missileDamage; // Todo Better points system than just missile damage
                    enemy.takeDamage(missile.missileDamage);
                    missile.takeDamage(Integer.MAX_VALUE);
                    break;
                }
            }
        }

        return points;
    }
}